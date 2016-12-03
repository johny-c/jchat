package common.utils;

import client.networking.R;
import common.db.entity.ChatMessage;
import common.db.entity.FileTransfer;
import common.db.entity.UserAccount;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.swing.ImageIcon;

public class Utils implements Conventions {

    //private static final Pattern IPV4_PATTERN
    //        = Pattern.compile("^(25[0-5]|2[0-4]\d|[0-1]?\d?\d)(\.(25[0-5]|2[0-4]\d|[0-1]?\d?\d)){3}$");
    private static final Pattern IPV6_STD_PATTERN
            = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN
            = Pattern.compile(
                    "^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";
    private static final Map<Integer, String> months = new HashMap();

    static {
        months.put(Calendar.JANUARY, "January");
        months.put(Calendar.FEBRUARY, "February");
        months.put(Calendar.MARCH, "March");
        months.put(Calendar.APRIL, "April");
        months.put(Calendar.MAY, "May");
        months.put(Calendar.JUNE, "June");
        months.put(Calendar.JULY, "July");
        months.put(Calendar.AUGUST, "August");
        months.put(Calendar.SEPTEMBER, "September");
        months.put(Calendar.OCTOBER, "October");
        months.put(Calendar.NOVEMBER, "November");
        months.put(Calendar.DECEMBER, "December");
    }

    public static boolean isNewerVersion(String newestVersion, String JCHAT_VERSION) {
        // regex \\. for dot, simple . matches any character
        String newestFields[] = newestVersion.split("\\.");
        String currentFields[] = JCHAT_VERSION.split("\\.");

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

        return newestMinorUpdate > currentMinorUpdate;
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

    public static boolean isValidFile(FileTransfer ft) {
        return true;
    }

    public static String getEchoPassword() {
        String temp = "";
        for (int i = 0; i < PASSWORD_ECHO_LENGTH; i++) {
            temp += PASSWORD_ECHO_CHAR;
        }
        return temp;
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

    public static boolean isIPv4Address(final String input) {
        // regex (\\.) for dot because simple dot matches any character
        String[] chunks = input.split("\\.");
        if (chunks.length != 4) {
            R.log("Invalid IPv4 Chunks length = " + chunks.length);
            return false;
        }

        for (String chunk : chunks) {
            int num = Integer.valueOf(chunk);
            if (num < 0 || num > 255) {
                R.log("Invalid IP Chunk < 0 || > 255 = " + chunk);
                return false;
            }
        }

        R.log("Valid IPv4 input");
        return true;
        //return IPV4_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6StdAddress(final String input) {

        return IPV6_STD_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6HexCompressedAddress(final String input) {

        return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6Address(final String input) {

        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
    }

    public static boolean isValidIP(String ip) {

        R.log("Checking IP: " + ip);
        ip = ip.trim();

        if (isIPv6Address(ip)) {
            R.log("Valid IPv6 address");
            return true;
        }

        if (isIPv4Address(ip)) {
            R.log("Valid IPv4 address");
            return true;
        }

        try {
            InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            R.log(ex.toString() + "\n\nInvalid IP address-host : " + ip);
            return false;
        }

        R.log("Valid IP input");
        return true;
    }

    public static void printSocketInfo(SSLSocket so) {
        String s = "Socket class: " + so.getClass();
        s += "\n   Remote address = "
                + so.getInetAddress().toString();
        s += "\n   Remote port = " + so.getPort();
        s += "\n   Local socket address = "
                + so.getLocalSocketAddress().toString();
        s += "\n   Local address = "
                + so.getLocalAddress().toString();
        s += "\n   Local port = " + so.getLocalPort();
        s += "\n   Need client authentication = "
                + so.getNeedClientAuth();
        SSLSession ss = so.getSession();
        s += "\n   Cipher suite = " + ss.getCipherSuite();
        s += "\n   Protocol = " + ss.getProtocol();
        R.log(s);
    }

    /**
     * Print Info about the Server Socket
     *
     * @param socket
     */
    public static void printServerSocketInfo(SSLServerSocket socket) {
        String s = "Server socket class: " + socket.getClass();
        s += "\n   Socker address = "
                + socket.getInetAddress().toString();
        s += "\n   Socker port = "
                + socket.getLocalPort();
        s += "\n   Need client authentication = "
                + socket.getNeedClientAuth();
        s += "\n   Want client authentication = "
                + socket.getWantClientAuth();
        s += "\n   Use client mode = "
                + socket.getUseClientMode();
        R.log(s);
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
        return Utils.class.getResource("/icons/" + relativePath);
    }

    public static void printInfo(UserAccount u) {
        String s = "\n\n" + Thread.currentThread().getName();
        s += "\n -- User Entry --\n";
        s += "\nid: " + u.getId();
        s += "\nusername: " + u.getUsername();
        s += "\nemail: " + u.getEmail();
        s += "\nreg date: " + u.getRegDate();
        s += "\nstatus: " + u.getStatus();
        R.log(s);
    }

    public static int birthdateToAge(Date birthDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(birthDate);
        int age = Calendar.getInstance().get(Calendar.YEAR) - c.get(Calendar.YEAR);
        return age;
    }

    public static File getCurrentJar() {
        String currentDir = null;
        try {
            currentDir = Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException ex) {
            R.log("The path to the jar file is corrupt.");
        }

        File f = new File(currentDir);
        return f;
    }

    public static String getAppDir() {
        return getCurrentJar().getParent();
    }

    public static void printMessageProtocol() {
        for (MessageType mt : MessageType.values()) {
            R.log(mt.ordinal() + "  " + mt.toString() + "\n");
        }
    }

    public static void printThreads() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        Thread[] list = new Thread[20];
        int n = group.enumerate(list);
        if (list == null) {
            R.log("List is null");
            return;
        } else {
            R.log("List contains " + n + " threads");
        }
        int counter = 1;
        String s = "- - ACTIVE THREADS - -\n";
        for (Thread t : list) {
            if (t != null) {
                s += "\n" + (counter++) + "  " + t.getName();

                if (t.isAlive()) {
                    s += " Alive";
                }
            }
        }
        R.log(s + "\n\n");
    }

    public static String formatCMList(List<ChatMessage> cmList) {
        String s = "";
        for (ChatMessage cm : cmList) {
            s += Utils.formatCM(cm);
        }

        return s;
    }

    public static String formatCM(ChatMessage cm) {
        StringBuilder builder = new StringBuilder();
        builder.append(cm.getTimeSent().toString());
        builder.append(" - ");
        builder.append(cm.getSourceName());
        builder.append(": ");
        builder.append(cm.getBody());
        builder.append("\n\n");
        return builder.toString();
    }

    public static String getUserAccountString(UserAccount c) {
        String s = "";
        s += "Username:  " + c.getUsername() + "\n";
        s += "Email: " + c.getEmail() + "\n";
        s += "Status: " + c.getStatus().toString() + "\n\n";
        return s;
    }

    public static String getMonthString(int month) {
        String s = "";
        return months.get(month);
    }


    /*
     * Get the extension of a file.
     */
    public static String getFileExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static void printPrefs(Preferences prefs, String prefType) {
        R.log("\n" + prefType
                + " prefs named " + prefs.name()
                + " preferences stored in " + prefs.absolutePath() + " :\n\n");
        try {
            int i = 1;
            for (String s : prefs.keys()) {
                R.log("Preference " + (i++) + " : " + s + " = " + prefs.get(s, ""));
            }
        } catch (BackingStoreException ex) {
            R.log(ex.toString());
        }
    }

    public static byte[] getFileInBytes(String filePath) {
        File file = new File(filePath);
        byte[] bFile = new byte[(int) file.length()];
        FileInputStream fileInputStream;

        try {
            fileInputStream = new FileInputStream(file);
            //convert file into array of bytes
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (IOException e) {
            R.log(e.toString());
        }

        return bFile;
    }

    public static byte[] getIconInBytes(ImageIcon rIcon) {
        byte[] result = null;
        //String encodedString;

        try {
            BufferedImage bi = Utils.toBufferedImage(rIcon.getImage());
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(bi, "png", baos);
                baos.flush();
                //byte[] dataToEncode = baos.toByteArray();
                //encodedString = Base64.encode(dataToEncode);
                result = baos.toByteArray();
            }
        } catch (IOException ex) {
            R.log(ex.toString() + " Could not get rescaled icon in bytes");
        }
        return result;
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

}
