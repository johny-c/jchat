package feature_testing.server;

import common.pojos.Conventions;
import common.pojos.Message;
import common.pojos.OQueue;
import common.pojos.Utils;
import common.db.entity.AddContactRequest;
import common.db.entity.ChatMessage;
import common.db.entity.Contact;
import common.db.entity.Conversation;
import common.db.entity.FileTransfer;
import common.db.entity.User;
import common.db.entity.UserContact;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;

public class ClientSessionHandler implements Runnable, Observer, Conventions {

    private SSLSocket sslsocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private ObjectInputStream mis;
    private ObjectOutputStream mout;
    private OQueue outgoingMessages;
    private User user;
    private InterMessageQueue interMessages;
    private ConcurrentHashMap<Long, ClientSessionHandler> threadsLoggedIn;
    private Database db;
    private String caller = this.getClass().getSimpleName();
    //private ServerGUI g;

    ClientSessionHandler(SSLSocket socket, ConcurrentHashMap<Long, ClientSessionHandler> map, InterMessageQueue queue) {
        //g = gui;
        sslsocket = socket;
        outgoingMessages = new OQueue(this);
        user = new User();
        threadsLoggedIn = map;
        interMessages = queue;
        db = new Database();
        Utils.printSocketInfo(sslsocket);
    }


    /*
     * Runs a thread of communication with one client (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            outputStream = sslsocket.getOutputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            mout = new ObjectOutputStream(outputStream);
        } catch (IOException ex) {
            Logger.getLogger(ClientSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            mout.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            inputStream = sslsocket.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(ClientSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            mis = new ObjectInputStream(inputStream);
        } catch (IOException ex) {
            Logger.getLogger(ClientSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }



        // DB Conect

        Object obj = null;
        Message m = null;
        try {
            while ((obj = mis.readObject()) != null) {
                m = (Message) obj;
                // Receive and handle requests
                //System.out.println("Server: Receiving following: " + obj.toString());

                if (m.getCode() == COMMUNICATION_TERMINATION_REQUEST) {
                    outgoingMessages.offer(new Message(COMMUNICATION_TERMINATION_RESPONSE));
                    break;
                }

                handleRequest(m);


            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ClientSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }



        try {
            mis.close();
            mout.close();
            sslsocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //db.close();
    }


    /*
     * Handle the incoming requests appropriately
     */
    private void handleRequest(Message m) throws SQLException {
        int msgCode = m.getCode();
        Message response = null;
        InterMessage im;

        ChatMessage cmIn, cmOut;
        User userIn, userOut;
        Contact ctIn, ctOut;
        FileTransfer fIn;
        Conversation convIn, convOut;
        Message msgOut;
        AddContactRequest acrIn, acrOut;

        R.log("Server: Handling request " + m.getDescription());

        switch (msgCode) {
            case LOGIN_REQUEST:

                break;

            case SIGNUP_REQUEST:

                break;


            case SEARCH_FOR_USER_REQUEST:

                break;

            case ADD_CONTACT_REQUEST:
                acrIn = (AddContactRequest) m.getContent();
                acrIn.setQuesterUserId(user.getId());
                acrIn.setStatus(AddContactRequest.Status.BY_SERVER);
                acrIn.setQuesterName(user.getUsernameOrEmail());
                acrIn.setTimeByServer(new Date());
                Integer acrId = db.insert(acrIn);
                acrIn.setServerGenId(acrId);
                acrIn.setBody(""); // set all fields null except ids, convId
                response = new Message(ADD_CONTACT_REQ_ACK, acrIn);
                outgoingMessages.offer(response);

                acrOut = (AddContactRequest) db.get(acrId, AddContactRequest.class);
                msgOut = new Message(ACR_DELIVERY, acrOut);
                im = new InterMessage();
                im.setSourceUserId(user.getId());
                im.setTargetUserId(acrOut.getRecipientUserId());
                im.setMessage(msgOut);
                interMessages.offer(im);
                break;

            case ACR_DELIVERY_ACK:
                acrIn = (AddContactRequest) m.getContent();
                acrIn.setStatus(AddContactRequest.Status.DELIVERED);
                //db.update(acrIn);
                break;

            case ACR_DECISION:
                acrIn = (AddContactRequest) m.getContent();
                acrIn.setStatus(AddContactRequest.Status.REPLIED);
                //db.update(acrIn);

                acrOut = (AddContactRequest) db.get(acrIn.getServerGenId(), AddContactRequest.class);
                msgOut = new Message(ACR_DECISION_DELIVERY, acrOut);
                im = new InterMessage();
                im.setSourceUserId(user.getId());
                im.setTargetUserId(acrOut.getQuesterUserId());
                im.setMessage(msgOut);
                interMessages.offer(im);

                if (acrIn.getReply()) { // User accepted ACR   
                    User quester = (User) db.get(acrOut.getQuesterUserId(), User.class);
                    User recipient = (User) db.get(acrOut.getRecipientUserId(), User.class);

                    UserContact uc1 = new UserContact();
                    uc1.setUserId(quester.getId());
                    uc1.setContactId(recipient.getId());
                    uc1.setStatus(false);
                    db.insert(uc1);

                    UserContact uc2 = new UserContact();
                    uc2.setUserId(recipient.getId());
                    uc2.setContactId(quester.getId());
                    uc2.setStatus(false);
                    db.insert(uc2);

                    response = new Message(CONTACT_ADDITION, (Contact) quester);
                    outgoingMessages.offer(response);

                    msgOut = new Message(CONTACT_ADDITION, (Contact) recipient);
                    im = new InterMessage();
                    im.setTargetUserId(quester.getId());
                    im.setMessage(msgOut);
                    interMessages.offer(im);
                }
                break;

            case ACR_DECISION_DELIVERY_ACK:
                acrIn = (AddContactRequest) m.getContent();
                acrIn.setStatus(AddContactRequest.Status.REPLY_REPORTED);
                //db.update(acrIn);
                break;

            case CONTACT_ADDED_ACK:
                // Contact has been added
                ctIn = (Contact) m.getContent();
                UserContact uc = (UserContact) db.select(UserContact.class, "where userId1 = " + user.getId() + " and userId2 = " + ctIn.getId());
                uc.setStatus(true);
                db.update(uc);
                break;

            case CONVERSATION_ID_REQUEST:
                convIn = (Conversation) m.getContent();
                Integer convId = db.insert(convIn);
                convOut = (Conversation) db.get(convId, Conversation.class);
                msgOut = new Message(CONVERSATION_ID_RESPONSE, convOut);
                outgoingMessages.offer(msgOut);
                break;

            case FILE_SEND_REQ:
                fIn = (FileTransfer) m.getContent();
                //fileToSaveSize = fIn.getFileSize();
                // check if size is ok
                if (Utils.isValidFile(fIn)) {
                    response = new Message(FILE_SEND_ACK, fIn);

                } else {
                    response = new Message(FILE_SEND_REJECTION, fIn);

                }
                outgoingMessages.offer(response);
                break;

            case FILE_UPLOAD_COMPLETED:
                fIn = (FileTransfer) m.getContent();
                //db.insert(fileIn, status byserver);
                msgOut = new Message(FILE_DELIVERY, fIn);

                im = new InterMessage();
                im.setSourceUserId(user.getId());
                im.setTargetUserId(fIn.getTargetUserId());
                im.setMessage(msgOut);
                interMessages.offer(im);
                break;

            case FILE_DELIVERY_ACK:
                fIn = (FileTransfer) m.getContent();
                fIn.setStatus(FileTransfer.Status.RECIPIENT_NOTIFIED);
                //db.update(fIn);
                break;


            case FILE_DOWNLOAD_COMPLETED:
                fIn = (FileTransfer) m.getContent();
                //db.insert(fileIn, status downloaded);
                msgOut = new Message(FILE_DELIVERY_REPORT, fIn);

                im = new InterMessage();
                im.setSourceUserId(fIn.getTargetUserId());
                im.setTargetUserId(fIn.getSourceUserId());
                im.setMessage(msgOut);
                interMessages.offer(im);
                break;

            case FILE_DELIVERY_REPORT_ACK:
                fIn = (FileTransfer) m.getContent();
                fIn.setStatus(FileTransfer.Status.DOWNLOAD_REPORTED);
                //db.update(fIn);
                break;


            case LOGOUT_REQUEST:

                break;

            case CHAT_MSG_SEND_REQ:
                ChatMessage cm = (ChatMessage) m.getContent();
                Integer cmId = db.insert(cm);
                cm.setServerGenId(cmId);
                cm.setBody(""); // set all fields null except ids, convId
                response = new Message(CHAT_MSG_SEND_ACK, cm);
                outgoingMessages.offer(response);

                cmOut = (ChatMessage) db.get(cmId, ChatMessage.class);
                msgOut = new Message(CHAT_MSG_DELIVERY, cmOut);
                im = new InterMessage();
                im.setSourceUserId(user.getId());
                im.setTargetUserId(cm.getTargetUserId());
                im.setMessage(msgOut);
                interMessages.offer(im);
                break;

            case CHAT_MSG_DELIVERY_ACK:
                cmIn = (ChatMessage) m.getContent();
                cmIn.setStatus(ChatMessage.Status.DELIVERED);
                // db.update(cmIn);
                msgOut = new Message(CHAT_MSG_DELIVERY_REPORT, cmIn);
                im = new InterMessage();
                im.setSourceUserId(user.getId());
                im.setTargetUserId(cmIn.getSourceUserId());
                im.setMessage(msgOut);
                interMessages.offer(im);
                break;

            case CHAT_MSG_DELIVERY_REPORT_ACK:
                cmIn = (ChatMessage) m.getContent();
                cmIn.setStatus(ChatMessage.Status.DELIVERY_REPORTED);
                // db.update(cmIn);                
                break;

            case USER_STATUS_UPDATE:
                User newStatus = (User) m.getContent();

                List<Integer> contactsIds = db.select(UserContact.class, "where userId1 = " + user.getId());
                for (Integer contactId : contactsIds) {
                    msgOut = new Message(CONTACT_STATUS_UPDATE, newStatus);
                    im = new InterMessage();
                    im.setSourceUserId(user.getId());
                    im.setTargetUserId(contactId);
                    im.setMessage(msgOut);
                    interMessages.offer(im);
                }
                break;

            case CONTACT_DELETION:
                ctIn = (Contact) m.getContent();
                db.delete(UserContact.class, "where userId1 = " + user.getId() + " and userId2 = " + ctIn.getId());
                response = new Message(CONTACT_DELETED_ACK, ctIn);
                outgoingMessages.offer(response);
                break;

            case MISSED_CALLS_REQUEST:
                break;

            // Up to MAX_MISSED_CHAT_LENGTH (e.g. 1000 chars)				
            case MISSED_CHAT_REQUEST: // at login time

                break;

            default:
                response = new Message(COMMUNICATION_TERMINATION_RESPONSE, "WTF???");
                outgoingMessages.offer(response);

        }

    }

    /*
     * Called every time a Message is put in the Observable MessageQueue responses
     * Writes the message to the buffered writer so that it gets transmitted to the client 
     * via the socket connection
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        R.log(caller + " Observing a message has been put to the " + o.getClass().getSimpleName() + " to be sent to a client");
        Message m = (Message) ((OQueue) o).poll();
        try {
            synchronized (mout) {
                mout.writeObject(m);
                mout.flush();
            }
            R.log(caller + " Written " + m.getDescription() + " to ObjectOutputStream of Socket.\n");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Used by other threads - clients that are simultaneously online they send
     * to the current thread - client through this method Synchronized because
     * it might be accessed by many other threads
     *
     * @param msg
     */
    public synchronized void putInQueue(Message msg) {
        outgoingMessages.offer(msg);
    }
}
