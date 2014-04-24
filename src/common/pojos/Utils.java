package common.pojos;

import common.db.entity.FileTransfer;
import common.db.entity.User;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import client.gui.LoginTab;
import client.gui.SignupTab;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import javax.swing.ImageIcon;

public class Utils implements Conventions {

    private static int SCREEN_WIDTH = -1;
    private static int SCREEN_HEIGHT = -1;
    private static Map<Integer, List<String>> PANEL_RELEVANT_MESSAGES = new HashMap<>();
    private final static Logger LOGGER = Logger.getLogger(Utils.class
            .getName());

    public static boolean isNewerVersion(String newestVersion, String JCHAT_VERSION) {
        String newestFields[] = newestVersion.split(".");
        String currentFields[] = JCHAT_VERSION.split(".");

        int newestMajorRelease = Integer.parseInt(newestFields[0]);
        int currentMajorRelease = Integer.parseInt(currentFields[0]);

        if (newestMajorRelease > currentMajorRelease) {
            return true;
        }

        int newestMinorRelease = Integer.parseInt(newestFields[1]);
        int currentMinorRelease = Integer.parseInt(currentFields[1]);

        if (newestMinorRelease > currentMinorRelease) {
            return true;
        }

        int newestMinorUpdate = Integer.parseInt(newestFields[2]);
        int currentMinorUpdate = Integer.parseInt(currentFields[2]);

        if (newestMinorUpdate > currentMinorUpdate) {
            return true;
        }

        return false;
    }

    public synchronized static void print(String caller, String msg) {
        Date d = new Date();
        LOGGER.log(Level.INFO, d + " " + caller + " " + msg);

    }

    public static boolean isValidPassword(String input) {
        char[] password = input.toCharArray();

        if (password.length < MIN_PASSWORD_LEN) {
            return false;
        }

        boolean hasNumber = false, hasLetter = false;
        for (int i = 0; i < password.length; i++) {
            if (Character.isLetter(password[i])) {
                hasLetter = true;
            } else if (Character.isDigit(password[i])) {
                hasNumber = true;
            }

            if (hasLetter && hasNumber) {
                return true;
            }
        }

        return false;
    }

    public static boolean messageIsRelevant(int messageCode, String panelName) {
        List<String> relevantPanes = PANEL_RELEVANT_MESSAGES.get(messageCode);
        if (relevantPanes.contains(panelName)) {
            return true;
        }
        return false;
    }

    public static boolean isValidFile(FileTransfer ft) {
        return true;
    }

    public void loadPaneRelevantMessages() {
        // FileInputStream
        // load into map
    }

    public static boolean isValidEmailAddress(String string) {
        int len = string.trim().length();
        // \b = word boundary
        // [A-Z0-9._%+-] = any of these characters 
        // + = one or more times
        // @ = @ once
        // [A-Z0-9.-] = any of these characters 
        // + = one or more times
        // . = . once
        // [A-Z]{2,4} = 2-4 characters from the [A-Z] set
        String emailRegex = "\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b";
        Pattern p = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);

        if (len > 5 && len < 255) {
            if (p.matcher(string).matches()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidUsername(String string) {
        int len = string.trim().length();
        if (len < MIN_USERNAME_LEN) {
            return false;
        }

        int lenwsp = string.length();
        if (lenwsp > MAX_USERNAME_LEN) {
            return false;
        }

        return true;
    }

    public static void printSocketInfo(SSLSocket s) {
        LOGGER.log(Level.INFO, "Socket class: " + s.getClass());
        LOGGER.log(Level.INFO, "   Remote address = "
                + s.getInetAddress().toString());
        LOGGER.log(Level.INFO, "   Remote port = " + s.getPort());
        LOGGER.log(Level.INFO, "   Local socket address = "
                + s.getLocalSocketAddress().toString());
        LOGGER.log(Level.INFO, "   Local address = "
                + s.getLocalAddress().toString());
        LOGGER.log(Level.INFO, "   Local port = " + s.getLocalPort());
        LOGGER.log(Level.INFO, "   Need client authentication = "
                + s.getNeedClientAuth());
        SSLSession ss = s.getSession();
        LOGGER.log(Level.INFO, "   Cipher suite = " + ss.getCipherSuite());
        LOGGER.log(Level.INFO, "   Protocol = " + ss.getProtocol());
    }

    /**
     * Print Info about the Server Socket
     */
    public static void printServerSocketInfo(SSLServerSocket s) {
        LOGGER.log(Level.INFO, "Server socket class: " + s.getClass());
        LOGGER.log(Level.INFO, "   Socker address = "
                + s.getInetAddress().toString());
        LOGGER.log(Level.INFO, "   Socker port = "
                + s.getLocalPort());
        LOGGER.log(Level.INFO, "   Need client authentication = "
                + s.getNeedClientAuth());
        LOGGER.log(Level.INFO, "   Want client authentication = "
                + s.getWantClientAuth());
        LOGGER.log(Level.INFO, "   Use client mode = "
                + s.getUseClientMode());
        LOGGER.log(Level.INFO, "\n\n\n");
    }

    public static boolean isValidChatMessage(String chatMessage) {
        // TODO Auto-generated method stub
        // Exclude forbidden characters (delimiters)
        // Check size < MAX_CHAT_MSG_LEN and > 0
        if (chatMessage.length() <= 0 || chatMessage.length() > MAX_CHAT_MSG_LEN) {
            return false;
        }

        return true;
    }

    public static URL getIcon(String relativePath) {
        return Utils.class.getResource("/resources/icons/" + relativePath);
    }

    public static ImageIcon getIIcon(String relativePath) {
        return new ImageIcon(Utils.class.getResource("/resources/icons/" + relativePath));
    }

    public static void getScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        SCREEN_WIDTH = (int) screenSize.getWidth();
        SCREEN_HEIGHT = (int) screenSize.getHeight();
    }

    public static int getScreenWidth() {
        if (SCREEN_WIDTH == -1) {
            getScreenSize();
        }
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight() {
        if (SCREEN_WIDTH == -1) {
            getScreenSize();
        }
        return SCREEN_HEIGHT;
    }

    public static void printInfo(User u) {
        LOGGER.log(Level.INFO, "\n -- User Entry --\n");
        LOGGER.log(Level.INFO, "id: " + u.getId());
        LOGGER.log(Level.INFO, "username: " + u.getUsername());
        LOGGER.log(Level.INFO, "email: " + u.getEmail());
        LOGGER.log(Level.INFO, "reg date: " + u.getRegDate());
        LOGGER.log(Level.INFO, "status: " + u.getStatus());
        LOGGER.log(Level.INFO, "\n");
    }

    public static int birthdateToAge(Date birthDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(birthDate);
        int age = Calendar.getInstance().get(Calendar.YEAR) - c.get(Calendar.YEAR);
        return age;
    }

    public static String getAppDir() {
        String currentDir = null;
        try {
            currentDir = Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException ex) {
            //logger.info("The path to the jar file is corrupt.");
        }

        //logger.info("R found curDir = " + currentDir);
        File f = new File(currentDir);
        //String appDir = f.getParent(); // <== dist dir = JChat (App) dir
        return f.getParent();
    }

}
