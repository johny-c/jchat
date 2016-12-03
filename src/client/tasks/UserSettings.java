package client.tasks;

import client.networking.R;
import java.util.prefs.Preferences;

public class UserSettings {

    private static Preferences prefs;
    private static String appDir;

    public UserSettings(String appDir) {
        this.appDir = appDir;
        R.log("UserSettings Constructor in = " + appDir);
        prefs = Preferences.userRoot().node(appDir);
    }

    public static Preferences getPrefs() {
        return prefs;
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

}
