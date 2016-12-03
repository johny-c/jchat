package client.networking;

import client.db.util.DB;
import client.gui.AddContactPane;
import client.gui.ChatPane;
import client.gui.ContactsPane;
import client.gui.LoginTab;
import client.gui.NewsPane;
import client.gui.SettingsPane;
import client.gui.SignupTab;
import client.gui.UserProfileTab;
import client.gui.utils.ContactsModel;
import common.db.entity.AddContactRequest;
import common.db.entity.ChatMessage;
import common.db.entity.Conversation;
import common.db.entity.ConversationParticipant;
import common.db.entity.FileTransfer;
import common.db.entity.Notification;
import common.db.entity.UserAccount;
import common.db.entity.UserContact;
import common.db.entity.UserIcon;
import common.db.entity.UserSession;
import common.utils.Conventions;
import common.utils.Message;
import common.utils.MessageType;
import common.utils.OQueue;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class IncomingMessageHandler implements Runnable, Conventions {

    private final BlockingQueue<Message> incomingMessages;
    private final BlockingQueue<Message> outgoingMessages;
    private final OQueue messagesForGui;
    private final String threadName = "INCOMING MESSAGE HANDLER THREAD";
    private volatile boolean needed;
    private volatile Thread thisThread;

    public IncomingMessageHandler(BlockingQueue msgsFromNet, BlockingQueue msgsToNet, OQueue messagesToGui) {
        incomingMessages = msgsFromNet;
        outgoingMessages = msgsToNet;
        messagesForGui = messagesToGui;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(threadName);
        thisThread = Thread.currentThread();

        Message message;
        needed = true;
        while (needed) {

            R.log("Waiting for a message from the Network Listener");
            try {
                message = incomingMessages.take();
            } catch (InterruptedException ex) {
                R.log(ex.toString());
                break;
            }

            R.log("Client IMH took: " + message.getType().toString());
            handleMessage(message);
        }

        R.log("FINISHED");
    }

    public void stop() {
        needed = false;
        thisThread.interrupt();
    }

    private void handleACRR(AddContactRequest acrIn) {
        acrIn.setStatus(AddContactRequest.Status.DELIVERED);
        acrIn.setTimeDelivered(new Date());
        DB.insert(acrIn);
        Notification n = new Notification(Notification.Type.ACR_RECEIVED, acrIn);
        // Send message to MainFrame - NewsPane
        // Or handle automatically via NewsModel
        n.setId(DB.insert(n));
        Message m = new Message(MessageType.ACR_DELIVERY, n);
        messagesForGui.offerNotify(m, NewsPane.class.getSimpleName());
    }

    private void handleACRD(AddContactRequest acrIn) {
        DB.update(acrIn);
        Notification n = new Notification(Notification.Type.ACR_DECISION_REPORT, acrIn);
        n.setId(DB.insert(n));
        Message m = new Message(MessageType.ACR_DECISION_DELIVERY, n);
        messagesForGui.offerNotify(m, NewsPane.class.getSimpleName());
    }

    private void handleFileIn(FileTransfer fileIn, boolean liveTransfer) {
        fileIn.setStatus(FileTransfer.Status.RECIPIENT_NOTIFIED);
        fileIn.setTimeNotified(new Date());
        DB.insert(fileIn);

        UserAccount sender = (UserAccount) DB.get(fileIn.getSourceUserId(), UserAccount.class);
        fileIn.setSourceName(sender.getUsername());
        Message m;
        if (!liveTransfer) {
            Notification n = new Notification(Notification.Type.FILE_RECEIVED, fileIn);
            n.setId(DB.insert(n));
            m = new Message(MessageType.FILE_NOTIFICATION, n);
            R.log("Offering FILE NOTIFICATION: " + n.getClass().getSimpleName());
            messagesForGui.offerNotify(m, NewsPane.class.getSimpleName());
        } else {
            m = new Message(MessageType.FILE_NOTIFICATION, fileIn);
            messagesForGui.offerNotify(m, ChatPane.class.getSimpleName());
        }
    }

    private void handleFileDownloadReport(FileTransfer fileTransfer) {
        UserAccount receiver = (UserAccount) DB.get(fileTransfer.getTargetUserId(), UserAccount.class);
        FileTransfer fileIn = (FileTransfer) DB.get(fileTransfer.getId(), FileTransfer.class);
        fileIn.setStatus(FileTransfer.Status.FILE_DOWNLOADED);
        fileIn.setTimeDownloaded(fileTransfer.getTimeDownloaded());
        fileIn.setTargetName(receiver.getUsername());
        DB.update(fileIn);
        Notification n = new Notification(Notification.Type.FILE_DOWNLOAD_REPORT, fileIn);
        n.setId(DB.insert(n));
        Message m = new Message(MessageType.FILE_DOWNLOAD_REPORT, n);
        messagesForGui.offerNotify(m, NewsPane.class.getSimpleName());
    }

    private void printSessions(UserSession us) {
        String s = "Receiving NEW SESSION\n\n";
        s += "OLD SESSION:\n";
        s += "User account = " + R.getUserAccount().getId();
        s += printSession(DB.getLastUserSession(R.getUserAccount().getId()));
        s += "NEW SESSION:\n";
        s += printSession(us);
        R.log(s);
    }

    private String printSession(UserSession us) {
        if (us == null) {
            return "\n-- NO PREVIOUS SESSION STORED --\n";
        }
        String s = "\n";
        s += "id: " + us.getId() + "\n";
        s += "userId: " + us.getUserId() + "\n";
        s += "lastSessionId: " + us.getLastSessionId() + "\n\n";
        return s;
    }

    private void handleMessage(Message message) {

        try {
            Integer ctId, acrId;
            UserAccount ctIn;
            UserIcon iconIn;
            UserContact uc;
            AddContactRequest acrIn;
            ChatMessage cmIn;
            FileTransfer fileIn;
            List<AddContactRequest> acrsIn;
            List<FileTransfer> filesIn;
            List<ChatMessage> cmsIn;
            Notification n;
            Message outgoing;
            switch (message.getType()) {
                // Received from Network Listener in case of an exception
                case NO_CONNECTION_BROADCAST:
                    message.setContent("Connection error");
                    messagesForGui.offerNotify(message, MessageType.NO_CONNECTION_BROADCAST);
                    messagesForGui.poll();
                    needed = false;
                    // No need to reply
                    break;

                // Signup
                case USERNAME_UNAVAILABLE:
                    messagesForGui.offerNotify(message, SignupTab.class.getSimpleName());
                    messagesForGui.offerNotify(message, UserProfileTab.class.getSimpleName());
                    break;

                case UPDATE_PROFILE_SUCCESS:
                    messagesForGui.offerNotify(message, UserProfileTab.class.getSimpleName());
                    break;

                case DELETE_ACCOUNT_SUCCESS:
                    DB.deleteAccount(R.getUserAccount());
                    messagesForGui.offerNotify(message, UserProfileTab.class.getSimpleName());
                    break;

                case ACCOUNT_ID:
                    messagesForGui.offerNotify(message, SignupTab.class.getSimpleName());
                    break;

                // Login
                case LOGIN_SUCCESS:
                case LOGIN_FAIL:
                    messagesForGui.offerNotify(message, LoginTab.class.getSimpleName());
                    break;

                case NEW_USER_SESSION:
                    UserSession us = (UserSession) message.getContent();
                    printSessions(us);

                    DB.deleteOldSessions(R.getUserAccount().getId());
                    DB.insert(us);
                    outgoing = new Message(MessageType.NEW_USER_SESSION_ACK, us.getId());
                    outgoingMessages.put(outgoing);
                    break;

                // Populate Notifications
                case MISSED_ACRS:
                    acrsIn = (List<AddContactRequest>) message.getContent();
                    for (AddContactRequest acrr : acrsIn) {
                        handleACRR(acrr);
                        outgoing = new Message(MessageType.ACR_DELIVERY_ACK, acrr.getServerGenId());
                        outgoingMessages.put(outgoing);
                    }
                    break;

                case ACR_DELIVERY: // BY_SERVER
                    acrIn = (AddContactRequest) message.getContent();
                    handleACRR(acrIn);

                    outgoing = new Message(MessageType.ACR_DELIVERY_ACK, acrIn.getServerGenId());
                    outgoingMessages.put(outgoing);
                    break;

                case MISSED_ACR_DECISIONS:
                    acrsIn = (List<AddContactRequest>) message.getContent();
                    for (AddContactRequest acrd : acrsIn) {
                        handleACRD(acrd);
                        outgoing = new Message(MessageType.ACR_DECISION_DELIVERY_ACK, acrd.getServerGenId());
                        outgoingMessages.put(outgoing);
                    }
                    break;

                case ACR_DECISION_DELIVERY:
                    acrIn = (AddContactRequest) message.getContent();
                    handleACRD(acrIn);
                    outgoing = new Message(MessageType.ACR_DECISION_DELIVERY_ACK, acrIn.getServerGenId());
                    outgoingMessages.put(outgoing);
                    break;

                case MISSED_CALLS: // AT LOGIN
                    break;

                case MISSED_CHATS: // AT LOGIN
                    cmsIn = (List<ChatMessage>) message.getContent();
                    for (ChatMessage cm : cmsIn) {
                        DB.insert(cm);
                    }
                    n = new Notification(Notification.Type.MISSED_CHAT, cmsIn);
                    n.setId(DB.insert(n));
                    message.setContent(n);
                    messagesForGui.offerNotify(message, NewsPane.class.getSimpleName());

                    // Send message to MainFrame - NewsPane
                    // Or handle automatically via NewsModel
                    //DB.insert(Notification);
                    for (ChatMessage cm : cmsIn) {
                        outgoing = new Message(MessageType.CHAT_MSG_DELIVERY_ACK, cm);
                        outgoingMessages.put(outgoing);
                    }
                    break;

                case MISSED_FILES:
                    filesIn = (List<FileTransfer>) message.getContent();
                    for (FileTransfer ft : filesIn) {
                        handleFileIn(ft, false);
                        outgoing = new Message(MessageType.FILE_NOTIFICATION_ACK, ft.getId());
                        outgoingMessages.put(outgoing);
                    }
                    break;

                case FILE_NOTIFICATION:
                    fileIn = (FileTransfer) message.getContent();
                    handleFileIn(fileIn, true);
                    outgoing = new Message(MessageType.FILE_NOTIFICATION_ACK, fileIn.getId());
                    outgoingMessages.put(outgoing);
                    break;

                case FILE_SEND_ACK: // Ok to start uploading the file
                    // SLA
                    fileIn = (FileTransfer) message.getContent();
                    DB.insert(fileIn);
                    R.log("IMH indeed a FILE_SEND_ACK, sending to GUI");
                    messagesForGui.offerNotify(message, ChatPane.class.getSimpleName());
                    // No need to reply
                    break;

                case FILE_SEND_SERVER_REJECTION:
                    //fileIn = (FileTransfer) message.getContent();
                    messagesForGui.offerNotify(message, ChatPane.class.getSimpleName());
                    // No need to reply
                    break;

                case FILE_DOWNLOAD_REPORT: // SENT BY SERVER TO SENDER
                    fileIn = (FileTransfer) message.getContent();
                    handleFileDownloadReport(fileIn);
                    messagesForGui.offerNotify(message, ChatPane.class.getSimpleName());
                    outgoing = new Message(MessageType.FILE_DOWNLOAD_REPORT_ACK, fileIn.getId());
                    outgoingMessages.put(outgoing);
                    break;

                // Search for User for ACR
                case SEARCH_FOR_USER_RESPONSE:
                    messagesForGui.offerNotify(message, AddContactPane.class.getSimpleName());
                    // No need to reply
                    break;

                // Add Contact Request
                case ADD_CONTACT_REQ_ACK: // BY_SERVER
                    //acrIn = (AddContactRequest) message.getContent();
                    messagesForGui.offerNotify(message, AddContactPane.class.getSimpleName());
                    // No need to reply
                    break;

                case ACR_DECISION_ACK: // Not implemented
                    acrId = (Integer) message.getContent();
                    AddContactRequest acr = (AddContactRequest) DB.get(acrId, AddContactRequest.class);
                    acr.setStatus(AddContactRequest.Status.REPLIED);
                    DB.update(acr);
                    messagesForGui.offerNotify(message, NewsPane.class.getSimpleName());
                    // SLA
                    // No need to reply
                    break;

                // Conversations
                case NEW_CONVERSATION_DELIVERY: // SERVER TO SENDER
                    Conversation conv = (Conversation) message.getContent();
                    DB.insert(conv);
                    for (UserAccount part : conv.getParticipants()) {
                        UserAccount p = (UserAccount) DB.get(part.getId(), UserAccount.class);
                        ConversationParticipant cp = new ConversationParticipant();
                        cp.setConversationId(conv.getServerGenId());
                        cp.setParticipantId(part.getId());
                        DB.insert(cp);
                    }

                    messagesForGui.offerNotify(message, ContactsPane.class.getSimpleName());
                    // No need to reply
                    break;

                case PARTICIPANT_JOINED_CONVERSATION:
                    messagesForGui.offerNotify(message, ChatPane.class.getSimpleName());
                    break;

                case PARTICIPANT_LEFT_CONVERSATION:
                    messagesForGui.offerNotify(message, ChatPane.class.getSimpleName());
                    break;

                case CONVERSATION_END:
                    messagesForGui.offerNotify(message, ChatPane.class.getSimpleName());
                    break;

                case CHAT_MSG_DELIVERY: // SERVER TO RECIPIENT
                    cmIn = (ChatMessage) message.getContent();
                    cmIn.setStatus(ChatMessage.Status.DELIVERED);
                    cmIn.setTimeDelivered(new Date());
                    DB.insert(cmIn);
                    messagesForGui.offerNotify(message, ChatPane.class.getSimpleName());
                    outgoing = new Message(MessageType.CHAT_MSG_DELIVERY_ACK, cmIn.getServerGenId());
                    outgoingMessages.put(outgoing);
                    break;

                case CHAT_MSG_DELIVERY_REPORT: // SENT BY SERVER TO SENDER
                    cmIn = (ChatMessage) message.getContent();
                    cmIn = (ChatMessage) DB.get(cmIn.getServerGenId(), ChatMessage.class);
                    cmIn.setStatus(ChatMessage.Status.DELIVERY_REPORTED);
                    cmIn.setTimeReported(new Date());
                    DB.update(cmIn);
                    messagesForGui.offerNotify(message, ChatPane.class.getSimpleName());

                    outgoing = new Message(MessageType.CHAT_MSG_DELIVERY_REPORT_ACK, cmIn.getServerGenId());
                    outgoingMessages.put(outgoing);
                    //message.setContent(cmIn);
                    break;

                // Contact updates
                case CONTACT_UPDATE:
                    ctIn = (UserAccount) message.getContent();
                    DB.update(ctIn);
                    messagesForGui.offerNotify(message, ContactsModel.class.getSimpleName());
                    // No need to reply
                    break;

                case CONTACT_ICON_UPDATE:
                    iconIn = (UserIcon) message.getContent();

                    UserIcon icon = DB.getUserIcon(iconIn.getUacId());
                    if (icon != null) {
                        icon.setIconData(iconIn.getIconData());
                        DB.update(icon);
                    } else {
                        DB.insert(iconIn);
                    }
                    messagesForGui.offerNotify(message, ContactsModel.class.getSimpleName());
                    // No need to reply
                    break;

                case CONTACT_BROKEN: // Someone deleted me as a contact
                    ctId = (Integer) message.getContent();

                    ctIn = (UserAccount) DB.get(ctId, UserAccount.class);
                    if (ctIn != null) {
                        DB.delete(ctIn);
                        iconIn = DB.getUserIcon(ctIn.getId());
                        if (iconIn != null) {
                            DB.delete(iconIn);
                        }
                    }

                    uc = DB.getUserContact(R.getUserAccount().getId(), ctId);
                    if (uc != null) {
                        DB.delete(uc);
                    }

                    messagesForGui.offerNotify(message, ContactsModel.class.getSimpleName());
                    outgoing = new Message(MessageType.CONTACT_BROKEN_ACK, ctId);
                    outgoingMessages.put(outgoing);
                    break;

                case CONTACT_ADDITION: // I receive contact information
                    ctIn = (UserAccount) message.getContent();
                    DB.insert(ctIn);

                    uc = new UserContact();
                    uc.setUserId(R.getUserAccount().getId());
                    uc.setContactId(ctIn.getId());
                    DB.insert(uc);

                    messagesForGui.offerNotify(message, ContactsModel.class.getSimpleName());
                    DB.deleteACRs(R.getUserAccount().getId(), ctIn.getId());

                    outgoing = new Message(MessageType.CONTACT_ADDED_ACK, ctIn.getId());
                    outgoingMessages.put(outgoing);
                    break;

                // Logout
                case LOGOUT_RESPONSE: // I logged out successfully
                    needed = false;
                    // No need to reply
                    break;

                case NEWEST_VERSION_RESPONSE:
                    messagesForGui.offerNotify(message, SettingsPane.class.getSimpleName());
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
        } catch (InterruptedException ex) {
            R.log(ex.toString());
            // Putting msg to outgoing queue failed
        }
    }

}
