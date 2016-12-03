package common.utils;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public interface Conventions {

    public static final String JCHAT_VERSION = "0.0.1";
    // -- Network --
    // Server details
    //static final String SERVER_NAME = "localhost";
    static final int SERVER_PORT = 9999;
    static final int SERVER_FILE_UPLOAD_PORT = 9998;
    static final int SERVER_FILE_DOWNLOAD_PORT = 9997;
    static final int SERVER_VIDEO_UPLOAD_PORT = 9996;
    static final int SERVER_VIDEO_DOWNLOAD_PORT = 9995;
    static final int SERVER_LATEST_VERSION_PORT = 9990;

    // Messages meta
    public static final int CLIENT_MSG_QUEUE_OUT_CAPACITY = 10;
    public static final int CLIENT_MSG_QUEUE_IN_CAPACITY = 10;
    // Password meta
    public static int HASH_LENGTH_IN_BYTES = 64;
    public static int SALT_LENGTH_IN_BYTES = 64;
    public static final int MIN_PASSWORD_LEN = 4;
    public static final int PASSWORD_ECHO_LENGTH = 16;
    public static final String PASSWORD_ECHO_CHAR = "*";
    // Username meta
    static final int MIN_USERNAME_LEN = 4;
    static final int MAX_USERNAME_LEN = 50;
    // Chat messages meta
    static final int MAX_CHAT_MSG_LEN = 1000;
    // Files meta
    public static final int FILE_BUFFER_SIZE = 1024;
    public static final int MAX_FILE_TRANSFER_SIZE = 100 * 1024 * 1024; // in B
    public static final int MAX_USER_ICON_SIZE = 1000000; // in B

    // UI
    public static final Icon JCHAT_LOGO = new ImageIcon(Utils.getIcon("jchat_logo/jchat_logo_20.png"));
    public static final String LOGIN_TAB_TITLE = "JChat - Login";
    public static final String LOGIN_TAB_TIP = "JChat Login tab";
    public static final String SIGNUP_TAB_TITLE = "JChat - Signup";
    public static final String SIGNUP_TAB_TIP = "JChat Signup tab";
    public static final String SIGNUP_SUCCESS_TAB_TITLE = "JChat - Signup Success";
    public static final String SIGNUP_SUCCESS_TAB_TIP = "JChat Signup Success tab";
    public static final String WELCOME_TAB_TIP = "JChat Welcome tab";
    public static final String PROFILE_TAB_TIP = "JChat Profile tab";
    public static final String CONTACTS_TAB_TITLE = "JChat - Contacts tab";
    public static final String CONTACTS_TAB_TIP = "JChat Contacts tab";
    public static final String ADD_CONTACT_TAB_TITLE = "JChat - Add contact";
    public static final String ADD_CONTACT_TAB_TIP = "Adding a contact";
    public static final String LOGOUT_TAB_TITLE = "JChat - Log out";
    public static final String LOGOUT_TAB_TIP = "Logging out";
    public static final String ACTIVE_TAB_TITLE = "JChat - Active";
    public static final String ACTIVE_TAB_TIP = "Active chatting";
    public static final String PROFILE_UPDATE_SUCCESS_TAB_TITLE = "JChat - Profile Updated";
    public static final String PROFILE_UPDATE_SUCCESS_TAB_TIP = "JChat Profile Updated tab";

    public static final String MAIN_TAB_TITLE = "JChat Main";
    public static final String MAIN_TAB_TIP = "JChat - Main Tab";
    public static final Color COLOR_CRIMSON_RED = new Color(220, 20, 60);
    public static final Color COLOR_LIGHT_BLACK = new Color(60, 59, 55);
    public static final Color COLOR_PAPAYA = new Color(255, 228, 196);
    public static final Color COLOR_PASTEL_GREEN = new Color(65, 179, 129);
    public static final Color COLOR_PASTEL_BLUE = new Color(78, 110, 187);
    public static final Font FONT_UBUNTU_PLAIN_14 = new Font("Ubuntu", Font.PLAIN, 14);
    public static final Font FONT_UBUNTU_BOLD_15 = new Font("Ubuntu", Font.BOLD, 15);
    public static final Font FONT_UBUNTU_BOLD_16 = new Font("Ubuntu", Font.BOLD, 16);
    public static final Font FONT_FUNKY = new Font("Purisa", Font.PLAIN, 20);
    public static final String WELCOME_LABEL = "Welcome to JChat !";
    public static final String JCHAT_PREFS = "jchatPrefs";
    public static final int USER_RICON_WIDTH = 48;
    public static final int USER_RICON_HEIGHT = 48;

    // App Settings key-names (Prefs)
    public static final String SERVER_IP = "serverIp";  // "138.246.2.70";
    public static final String DEFAULT_SERVER_IP = "jchat.hopto.org";
    public static final String REMEMBER_CREDENTIALS = "rememberCredentials";
    public static final boolean DEFAULT_REMEMBER_CREDENTIALS = true;
    public static final String LOGIN_ACCOUNT = "loginAccount";
    public static final int DEFAULT_LOGIN_ACCOUNT = -1;

    // User Settings key-names (UserSettings)
    public static final String TEXT_COLOR = "textColor"; // default black
    public static final int DEFAULT_TEXT_COLOR = Color.BLACK.getRGB();
    public static final String DOWNLOAD_FOLDER = "downloadFolder";
    public static final String DEFAULT_DOWNLOAD_FOLDER = Utils.getAppDir() + File.separator + "Downloads";
    public static final String ASK_DOWNLOAD_FOLDER = "askDownloadFolder";
    public static final boolean DEFAULT_ASK_DOWNLOAD_FOLDER = true;
    public static final String USER_ICON_PATH = "userIconPath";
    public static final String DEFAULT_USER_ICON_PATH = "Anonymous Icon";

}
