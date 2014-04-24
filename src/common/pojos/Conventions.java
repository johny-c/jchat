package common.pojos;

import java.awt.Color;
import java.awt.Font;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public interface Conventions {

    Logger logger = Logger.getLogger("Logger");
    // -- Network --
    // Server details
    //static final String SERVER_NAME = "localhost";
    public static final String SERVER_IP = "192.168.2.16";  // "138.246.2.70";
    static final int SERVER_PORT = 9999;
    static final int SERVER_FILE_UPLOAD_PORT = 9998;
    static final int SERVER_FILE_DOWNLOAD_PORT = 9997;
    static final int SERVER_VIDEO_UPLOAD_PORT = 9996;
    static final int SERVER_VIDEO_DOWNLOAD_PORT = 9995;
    // Messages meta
    public static final int CLIENT_MSG_QUEUE_OUT_CAPACITY = 10;
    public static final int CLIENT_MSG_QUEUE_IN_CAPACITY = 10;
    // Password meta
    public static int HASH_LENGTH_IN_BYTES = 64;
    public static int SALT_LENGTH_IN_BYTES = 64;
    public static final int MIN_PASSWORD_LEN = 4;
    // Username meta
    static final int MIN_USERNAME_LEN = 4;
    static final int MAX_USERNAME_LEN = 50;
    // Chat messages meta
    static final int MAX_CHAT_MSG_LEN = 1000;
    // Files meta
    public static final int FILE_BUFFER_SIZE = 1024;
    // -- Message Codes --
    // Login
    public static final int LOGIN_REQUEST = 1;
    public static final int LOGIN_RESPONSE = 2;
    public static final int LOGIN_SUCCESS = 1;
    public static final int BAD_PASSWORD = -1;
    public static final int BAD_USERNAME = -2;
    public static final int LOGIN_FAIL = -3;
    // Signup
    public static final int SIGNUP_REQUEST = 3;
    public static final int SIGNUP_RESPONSE = 4;
    public static final int USERNAME_UNAVAILABLE = -3;
    public static final int SIGNUP_SUCCESS = 5;
    // Populate Contacts List
    public static final int GET_CONTACTS_REQUEST = 6; // AT LOGIN
    public static final int GET_CONTACTS_RESPONSE = 7; // AT LOGIN    
    // Search for User for ACR
    public static final int SEARCH_FOR_USER_REQUEST = 8;
    public static final int SEARCH_FOR_USER_RESPONSE = 9;
    // Add Contact Request
    public static final int ADD_CONTACT_REQUEST = 10; // BY_SOURCE
    public static final int ADD_CONTACT_REQ_ACK = 11; // BY_SERVER
    public static final int ACR_DELIVERY = 12; // BY_SERVER
    public static final int ACR_DELIVERY_ACK = 13; // DELIVERED
    public static final int ACR_DECISION = 14; // REPLIED
    public static final int ACR_DECISION_ACK = 15; // REPLIED
    public static final int ACR_DECISION_DELIVERY = 16;
    public static final int ACR_DECISION_DELIVERY_ACK = 17;
    // Conversations
    public static final int CONVERSATION_ID_REQUEST = 18;
    public static final int CONVERSATION_ID_RESPONSE = 19; // SERVER TO SENDER
    // Chat Messages
    public static final int CHAT_MSG_SEND_REQ = 20;	// SENDER TO SERVER
    public static final int CHAT_MSG_SEND_ACK = 21; // SERVER TO SENDER
    public static final int CHAT_MSG_DELIVERY = 22; // SERVER TO RECIPIENT
    public static final int CHAT_MSG_DELIVERY_ACK = 23; // RECIPIENT TO SERVER
    public static final int CHAT_MSG_DELIVERY_REPORT = 24; // SENT BY SERVER TO SENDER
    public static final int CHAT_MSG_DELIVERY_REPORT_ACK = 25; // SENT BY SENDER TO SERVER
    // File Transfers
    public static final int FILE_SEND_REQ = 26;	// SENDER TO SERVER
    public static final int FILE_SEND_ACK = 27; // SERVER TO SENDER
    public static final int FILE_SEND_REJECTION = -4; // SERVER TO SENDER
    public static final int FILE_DELIVERY = 28; // SERVER TO RECIPIENT
    public static final int FILE_DELIVERY_ACK = 29; // RECIPIENT TO SERVER
    public static final int FILE_DELIVERY_REPORT = 30; // SENT BY SERVER TO SENDER
    public static final int FILE_DELIVERY_REPORT_ACK = 31; // SENT BY SENDER TO SERVER
    public static final int FILE_START_DOWNLOAD_OK = 32; // SENDER TO SERVER
    public static final int FILE_DOWNLOAD_COMPLETED = 33; // RECEIVER TO SERVER
    public static final int FILE_START_UPLOAD_OK = 34; // SERVER TO SENDER (file upload socket)
    public static final int FILE_UPLOAD_COMPLETED = 35; // SENDER TO SERVER
    // Contact updates
    public static final int USER_STATUS_UPDATE = 36; // USER TO SERVER 
    public static final int CONTACT_STATUS_UPDATE = 37; // SERVER TO CONTACTS
    public static final int CONTACT_DELETION = 38; // USER TO SERVER
    public static final int CONTACT_DELETED_ACK = 39; // SERVER TO USER
    public static final int CONTACT_ADDITION = 40; // SERVER TO USER
    public static final int CONTACT_ADDED_ACK = 41; // USER TO SERVER
    // Populate Notifications
    public static final int MISSED_ACRS_REQUEST = 42; // AT LOGIN ?
    public static final int MISSED_ACR_RESPONSE = 43; // AT LOGIN ? 
    public static final int MISSED_ACR_REPORTS_REQUEST = 44; // AT LOGIN ? 
    public static final int MISSED_ACR_DECISION_DELIVERY = 45; // AT LOGIN ?
    public static final int MISSED_CALLS_REQUEST = 46; // AT LOGIN
    public static final int MISSED_CALLS_RESPONSE = 47; // AT LOGIN
    public static final int MISSED_CHAT_REQUEST = 48;// AT LOGIN
    public static final int MISSED_CHAT_RESPONSE = 49; // AT LOGIN
    public static final int MISSED_FILE_REQUEST = 50;
    public static final int MISSED_FILE_RESPONSE = 51;
    // Logout
    public static final int LOGOUT_REQUEST = 52;
    public static final int LOGOUT_RESPONSE = 53;
    // User Sessions
    public static final int NEW_USER_SESSION = 56; // SERVER TO USER
    public static final int NEW_USER_SESSION_ACK = 57; // USER TO SERVER
    public static final int NEW_USER_SESSION_REQUEST = 58; // USER TO SERVER
    // Program meta
    public static final int NEWEST_VERSION_REQUEST = 1000;
    public static final int NEWEST_VERSION_RESPONSE = 1001;
    public static final int JCHAT_FILE_ID_REQUEST = 1002;
    public static final int JCHAT_FILE_ID_RESPONSE = 1003;
    // Termination of communication
    public static final int COMMUNICATION_TERMINATION_REQUEST = 54;	// USED BY APP TO TERMINATE CONNECTION SMOOTHLY
    public static final int COMMUNICATION_TERMINATION_RESPONSE = 55;// USED BY APP TO TERMINATE CONNECTION SMOOTHLY
    // UI
    public static final Icon JCHAT_LOGO = new ImageIcon(Utils.getIcon("jchat_logo/jchat_logo_20.png"));
    public static final String LOGIN_TAB_TITLE = "JChat - Login";
    public static final String LOGIN_TAB_TIP = "JChat Login tab";
    public static final String SIGNUP_TAB_TITLE = "JChat - Signup";
    public static final String SIGNUP_TAB_TIP = "JChat Signup tab";
    public static final String SIGNUP_SUCCESS_TAB_TITLE = "JChat - Signup Success";
    public static final String SIGNUP_SUCCESS_TAB_TIP = "JChat Signup Success tab";
    public static final String CONTACTS_TAB_TITLE = "JChat - Contacts tab";
    public static final String CONTACTS_TAB_TIP = "JChat Contacts tab";
    public static final String ADD_CONTACT_TAB_TITLE = "JChat - Add contact";
    public static final String ADD_CONTACT_TAB_TIP = "Adding a contact";
    public static final String LOGOUT_TAB_TITLE = "JChat - Log out";
    public static final String LOGOUT_TAB_TIP = "Logging out";
    public static final String ACTIVE_TAB_TITLE = "JChat - Active";
    public static final String ACTIVE_TAB_TIP = "Active chatting";
    public static final String MAIN_TAB_TITLE = "JChat Main";
    public static final String MAIN_TAB_TIP = "JChat - Main Tab";
    public static final Color COLOR_CRIMSON_RED = new Color(220, 20, 60);
    public static final Color COLOR_LIGHT_BLACK = new Color(60, 59, 55);
    public static final Color COLOR_PAPAYA = new Color(255, 228, 196);
    public static final Color COLOR_PASTEL_GREEN = new Color(65, 179, 129);
    public static final Color COLOR_PASTEL_BLUE = new Color(78, 110, 187);
    public static final Font FONT_UBUNTU_BOLD_15 = new Font("Ubuntu", Font.BOLD, 15);
    public static final Font FONT_UBUNTU_BOLD_16 = new Font("Ubuntu", Font.BOLD, 16);
    public static final Font FONT_FUNKY = new Font("Purisa", Font.PLAIN, 20);
    public static final String WELCOME = "Welcome to JChat !";
}
