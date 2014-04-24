package client.pojos;

import java.util.prefs.Preferences;

public class UserSettings {

    static {
    }
    private static Preferences prefs;
    private static String appDir;
    // Settings key-names
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TEXT_COLOR = "textColor";
    public static final String DOWNLOAD_FOLDER = "downloadFolder";
    public static final String REMEMBER_CREDENTIALS = "rememberCredentials";
    public static final String STORED_USERNAME = "storedUsername";
    public static final String STORED_ENC_PASSWORD = "encryptedStoredPassword";

    public UserSettings(String appDir) {
        this.appDir = appDir;
        R.log("UserSettings Constructor in = " + appDir);
        prefs = Preferences.userRoot().node(appDir);
    }

    public static void set(String key, String value) {
        prefs.put(key, value);
    }

    public static void set(String key, int value) {
        prefs.putInt(key, value);
    }

    public static String get(String key, String defaultValue) {
        return prefs.get(key, defaultValue);
    }

    public static int get(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    public static boolean get(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    public void vogellaExample() {

        // This will define a node in which the preferences can be stored
        prefs = Preferences.userRoot().node(this.getClass().getName());
        String ID1 = "Test1";
        String ID2 = "Test2";
        String ID3 = "Test3";
        // First we will get the values
        // Define a boolean value
        System.out.println(prefs.getBoolean(ID1, true));
        // Define a string with default "Hello World
        System.out.println(prefs.get(ID2, "Hello World"));
        // Define a integer with default 50
        System.out.println(prefs.getInt(ID3, 50));

        // now set the values
        prefs.putBoolean(ID1, false);
        prefs.put(ID2, "Hello Europa");
        prefs.putInt(ID3, 45);

        // Delete the preference settings for the first value
        prefs.remove(ID1);

    }

    public static void main(String[] args) {
        UserSettings test = new UserSettings("");
        test.vogellaExample();

        // Run the program twice. The value of "ID1" should be written with its 
        // default value (true) to the command line, as the preference value
        // was deleted at the end of the method. The value of "ID2" and "ID3"
        // should have changed after the first call. 
    }

    public Preferences getPrefs() {
        return prefs;
    }
}
