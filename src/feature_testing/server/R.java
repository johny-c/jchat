package feature_testing.server;

import client.pojos.UserSettings;
import client.db.util.Database;
import common.pojos.Conventions;
import common.pojos.JChatLogger;
import java.util.Observer;
import java.util.Random;
import java.util.prefs.Preferences;
import client.networking.IncomingMessageHandler;
import client.networking.NetworkManager;

public class R implements Conventions {

    private static NetworkManager nm;
    private static Database db;
    private static UserSettings settings;
    private static Random random;
    private static JChatLogger logger;
    private static IncomingMessageHandler imh;

    public R() {
        //nm = new NetworkManager();
        //db = new Database();
        random = new Random();
        logger = new JChatLogger();
        System.out.println("Created R, random, loger");
    }

    public static NetworkManager getNm() {
        return nm;
    }

    public static void setNm(NetworkManager nm) {
        R.nm = nm;
    }

    public static Database getDb() {
        return db;
    }

    public static void setDb(Database db) {
        R.db = db;
    }

    public static Random getRandom() {
        return random;
    }

    public static void setRandom(Random random) {
        R.random = random;
    }

    public static Preferences getSettings() {
        return settings.getPrefs();
    }

    public static synchronized void log(String s) {
        //System.out.println("Called R.log");
        logger.log(s);
    }
}
