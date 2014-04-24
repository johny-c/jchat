package client.networking;

import common.db.entity.AddContactRequest;
import common.db.entity.ChatMessage;
import common.db.entity.Contact;
import common.db.entity.Conversation;
import common.db.entity.FileTransfer;
import common.db.entity.Notification;
import common.db.entity.UserSession;
import client.db.util.Database;
import common.pojos.ChatMessagesList;
import common.pojos.Conventions;
import common.pojos.Message;
import common.pojos.OQueue;
import feature_testing.client.ClientGUI;
import client.gui.AddContactPane;
import client.gui.ChatPane;
import client.gui.LoginTab;
import client.gui.NewsPane;
import client.gui.SignupTab;
import client.gui.ContactsModel;
import client.gui.ContactsPane;
import client.gui.MainFrame;
import client.gui.SettingsPane;
import client.gui.SoftwareUpdateFrame;
import client.pojos.R;
import client.pojos.UserSettings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import org.jasypt.util.password.StrongPasswordEncryptor;

public class IncomingMessageHandler implements Observer, Conventions {

    private OQueue incomingMessages;
    private List<String> guiObservers;
    private Message message;
    private Database db;
    private final OQueue messagesForGui;

    public IncomingMessageHandler() {
        messagesForGui = new OQueue();
        guiObservers = new ArrayList<>();
        db = R.getDb();
    }

    @Override
    public void update(Observable o, Object arg) {
        incomingMessages = (OQueue) o;
        message = (Message) incomingMessages.poll();
        R.log("Client IMH receives: " + message.getDescription());
        Integer responseInt;
        Contact contactIn;
        AddContactRequest acrIn;
        ChatMessage cmIn;
        FileTransfer fileIn;
        Notification n;
        Message outgoing;


        switch (message.getCode()) {
            // Signup
            case SIGNUP_RESPONSE:
                responseInt = (Integer) message.getContent();
                if (responseInt == SIGNUP_SUCCESS) {
                    StrongPasswordEncryptor spe = new StrongPasswordEncryptor();
                    UserSettings.set(UserSettings.STORED_ENC_PASSWORD, spe.encryptPassword(R.getU().getPassword()));
                    UserSettings.set(UserSettings.STORED_USERNAME, R.getU().getUsername());
                    R.getU().setPassword("");
                }
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(SignupTab.class.getSimpleName());
                break;

            // Login
            case LOGIN_RESPONSE:
                responseInt = (Integer) message.getContent();
                // Send message to LoginTab
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(LoginTab.class.getSimpleName());
                break;

            case NEW_USER_SESSION:
                UserSession us = (UserSession) message.getContent();
                db.deleteAll(UserSession.class);
                db.insert(us);
                outgoing = new Message(NEW_USER_SESSION_ACK, us.getId());
                R.getNm().send(outgoing);
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(LoginTab.class.getSimpleName());
                break;


            /*
             // Populate Contacts List
             case GET_CONTACTS_RESPONSE: // AT LOGIN 
             List<Contact> contactsIn = (List<Contact>) message.getContent();
             for (Contact c : contactsIn) {
             db.insert(c);
             }
             // Send message to MainFrame - ContactsPane
             // Or handle automatically via ContactsModel
             msgsForGui.offerDontNotify(GET_CONTACTS_RESPONSE);
             msgsForGui.notifyObservers(ContactsModel.class.getSimpleName());
             break;
             */
//public enum Type {

//        ACRR, ACRA, ACRDR, MISSED_CHAT, MISSED_CALL, FILE_SENT
            //   }


            // Populate Notifications
            case MISSED_ACR_RESPONSE: // AT LOGIN ? 
            case ACR_DELIVERY: // BY_SERVER
                acrIn = (AddContactRequest) message.getContent();
                acrIn.setStatus(AddContactRequest.Status.DELIVERED);
                acrIn.setTimeDelivered(new Date());
                db.insert(acrIn);
                n = new Notification(Notification.Type.ACRR, acrIn);
                // Send message to MainFrame - NewsPane
                // Or handle automatically via NewsModel   
                n.setId(db.insert(n));
                message.setContent(n);
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(NewsPane.class.getSimpleName());

                outgoing = new Message(ACR_DELIVERY_ACK, acrIn);
                R.getNm().send(outgoing);
                break;

            case MISSED_ACR_DECISION_DELIVERY: // AT LOGIN ?
            case ACR_DECISION_DELIVERY:
                acrIn = (AddContactRequest) message.getContent();
                acrIn.setStatus(AddContactRequest.Status.REPLY_REPORTED);
                db.insert(acrIn);
                n = new Notification(Notification.Type.ACRDR, acrIn);
                n.setId(db.insert(n));
                message.setContent(n);
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(NewsPane.class.getSimpleName());
                // NM.send ACR_DECISION_DELIVERY_ACK: // REPLIED
                outgoing = new Message(ACR_DECISION_DELIVERY_ACK);
                outgoing.setContent(acrIn);
                R.getNm().send(outgoing);
                break;

            case MISSED_CALLS_RESPONSE: // AT LOGIN
                break;

            case MISSED_CHAT_RESPONSE: // AT LOGIN
                ChatMessagesList cmsIn = (ChatMessagesList) message.getContent();
                for (ChatMessage cm : cmsIn) {
                    db.insert(cm);
                }
                n = new Notification(Notification.Type.MISSED_CHAT, cmsIn.get(0));
                // Send message to MainFrame - NewsPane
                // Or handle automatically via NewsModel   
                n.setId(db.insert(n));
                message.setContent(n);
                messagesForGui.offerDontNotify(message);

                // Send message to MainFrame - NewsPane
                // Or handle automatically via NewsModel   
                //db.insert(Notification);
                messagesForGui.notifyObservers(NewsPane.class.getSimpleName());
                break;

            case MISSED_FILE_RESPONSE:
            case FILE_DELIVERY:
                fileIn = (FileTransfer) message.getContent();
                fileIn.setStatus(FileTransfer.Status.RECIPIENT_NOTIFIED);
                fileIn.setTimeNotified(new Date());
                db.insert(fileIn);

                n = new Notification(Notification.Type.FILE_SENT, fileIn);
                n.setEventId(fileIn.getId());
                n.setId(db.insert(n));
                message.setContent(n);
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(NewsPane.class.getSimpleName());
                // notify ChatPane for file delivery, NewsPane for missed file
                outgoing = new Message(FILE_DELIVERY_ACK, fileIn);
                R.getNm().send(outgoing);
                break;

            case FILE_SEND_ACK: // Ok to start uploading the file
                // SLA
                fileIn = (FileTransfer) message.getContent();
                //db.insert(fileIn);
                R.log("IMH indeed a FILE_SEND_ACK, sending to GUI");
                messagesForGui.offerDontNotify(message);
                //msgsForGui.notifyObservers(ChatPane.class.getSimpleName());
                messagesForGui.notifyObservers(ClientGUI.class.getSimpleName());
                // No need to reply
                break;

            case FILE_SEND_REJECTION:
                fileIn = (FileTransfer) message.getContent();
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(ChatPane.class.getSimpleName());
                // No need to reply
                break;

            case FILE_DELIVERY_REPORT: // SENT BY SERVER TO SENDER
                fileIn = (FileTransfer) message.getContent();
                fileIn.setStatus(FileTransfer.Status.RECIPIENT_NOTIFIED);
                db.update(fileIn);
                message.setContent(fileIn);
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(ChatPane.class.getSimpleName());
                outgoing = new Message(FILE_DELIVERY_REPORT_ACK);
                outgoing.setContent(fileIn);
                R.getNm().send(outgoing);
                break;

            // Search for User for ACR
            case SEARCH_FOR_USER_RESPONSE:
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(AddContactPane.class.getSimpleName());
                // No need to reply
                break;

            // Add Contact Request
            case ADD_CONTACT_REQ_ACK: // BY_SERVER
                acrIn = (AddContactRequest) message.getContent();
                db.insert(acrIn);
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(AddContactPane.class.getSimpleName());
                // No need to reply
                break;

            case ACR_DECISION_ACK:
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(NewsPane.class.getSimpleName());
                // SLA
                // No need to reply
                break;

            // Conversations               
            case CONVERSATION_ID_RESPONSE: // SERVER TO SENDER
                Conversation conv = (Conversation) message.getContent();
                db.insert(conv);
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(ContactsPane.class.getSimpleName());
                // No need to reply
                break;

            // Chat Messages
            case CHAT_MSG_SEND_ACK: // SERVER TO SENDER
                cmIn = (ChatMessage) message.getContent();
                db.insert(cmIn);
                //message.setContent(cmIn);
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(ChatPane.class.getSimpleName());
                // No need to reply
                break;

            case CHAT_MSG_DELIVERY: // SERVER TO RECIPIENT
                cmIn = (ChatMessage) message.getContent();
                //cmIn.setStatus(ChatMessage.Status.DELIVERED);
                cmIn.setTimeDelivered(new Date());
                db.insert(cmIn);
                // NM.send CHAT_MSG_DELIVERY_ACK: // RECIPIENT TO SERVER
                cmIn.setBody("");
                outgoing = new Message(CHAT_MSG_DELIVERY_ACK, cmIn);
                R.getNm().send(outgoing);

                message.setContent(cmIn);
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(ChatPane.class.getSimpleName());
                break;

            case CHAT_MSG_DELIVERY_REPORT: // SENT BY SERVER TO SENDER
                cmIn = (ChatMessage) message.getContent();
                //cmIn.setStatus(ChatMessage.Status.DELIVERY_REPORTED);
                db.update(cmIn);
                outgoing = new Message(CHAT_MSG_DELIVERY_REPORT_ACK, cmIn);
                R.getNm().send(outgoing);

                //message.setContent(cmIn);
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(ChatPane.class.getSimpleName());
                break;

            // Contact updates
            case CONTACT_STATUS_UPDATE: // Some user changed his status
                contactIn = (Contact) message.getContent();
                db.update(contactIn);
                message.setContent(contactIn.getId());
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(ContactsModel.class.getSimpleName());
                // No need to reply
                break;

            case CONTACT_DELETED_ACK: // I deleted a contact successfully
                contactIn = (Contact) message.getContent();
                db.delete(contactIn);
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(ContactsModel.class.getSimpleName());
                // No need to reply
                break;

            case CONTACT_ADDITION: // I receive contact information
                contactIn = (Contact) message.getContent();
                db.insert(contactIn);
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(ContactsModel.class.getSimpleName());
                // No need to reply
                outgoing = new Message(CONTACT_ADDED_ACK, contactIn);
                R.getNm().send(outgoing);
                break;


            // Logout
            case LOGOUT_RESPONSE: // I logged out successfully
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(MainFrame.class.getSimpleName());
                // No need to reply
                break;

            case NEWEST_VERSION_RESPONSE:
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(SettingsPane.class.getSimpleName());
                // No need to reply
                break;

            case JCHAT_FILE_ID_RESPONSE:
                messagesForGui.offerDontNotify(message);
                messagesForGui.notifyObservers(SoftwareUpdateFrame.class.getSimpleName());
                // No need to reply
                break;
            // Termination of communication
            case COMMUNICATION_TERMINATION_REQUEST:	// USED BY APP TO TERMINATE CONNECTION SMOOTHLY
                break;
            case COMMUNICATION_TERMINATION_RESPONSE:// USED BY APP TO TERMINATE CONNECTION SMOOTHLY
                break;

            default: // UNKNOWN_MESSAGE_TYPE = Dangerous (not from JChat App)
                // Close connection immediately
                break;
        }

    }

    public void subscribe(Observer observer) {
        messagesForGui.addObserver(observer);
        guiObservers.add(observer.getClass().getSimpleName());
        printListeners();
    }

    private void printListeners() {
        int n = messagesForGui.countObservers();
        R.log("These " + n + " folk(s) are listening for messages from server");
        for (String s : guiObservers) {
            R.log(s);
        }
    }

    public void unsubscribe(Observer observer) {
        // Add corresponding UI Component as observer of the responses
        messagesForGui.deleteObserver(observer);
        guiObservers.remove(observer.getClass().getSimpleName());
        printListeners();
    }
}
