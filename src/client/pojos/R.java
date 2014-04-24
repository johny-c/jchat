package client.pojos;

import client.db.util.Database;
import client.gui.LoginTab;
import client.gui.MainFrame;
import client.networking.IncomingMessageHandler;
import client.networking.NetworkManager;
import common.db.entity.TestEntity;
import common.db.entity.User;
import common.pojos.Conventions;
import common.pojos.Message;
import common.pojos.Utils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.net.ssl.SSLSocket;

public class R implements Conventions {

    public static final String JCHAT_VERSION = "0.0.1";
    private static NetworkManager nm;
    private static User u;
    private static Database db;
    private static MainFrame mf;
    private static UserSettings settings;
    private static Random random;
    private static IncomingMessageHandler imh;
    private static Logger logger;
    private static String appDir;

    static {
        u = new User();
        u.setStatus(User.Status.OFFLINE);
        db = new Database();
        random = new Random();
    }

    public static SSLSocket getFileDownloadSocket() {
        return nm.getFileDownloadSocket();
    }

    public static void send(Message message) {
        nm.send(message);
    }

    public static NetworkManager getNm() {
        return nm;
    }

    public static void setNm(NetworkManager nm) {
        R.nm = nm;
    }

    public static User getU() {
        return u;
    }

    public static void setU(User u) {
        R.u = u;
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

    public static MainFrame getMf() {
        return mf;
    }

    public static void setMf(MainFrame mf) {
        R.mf = mf;
    }

    // Set by NetworkListener
    public static void setIMH(IncomingMessageHandler imh) {
        R.imh = imh;
    }

    public static IncomingMessageHandler getImh() {
        return imh;
    }

    public static String getAppDir() {
        return appDir;
    }

    public static void setAppDir(String appDir) {
        R.appDir = appDir;
    }

    public static synchronized void log(String s) {
        //System.out.println("Called R.log");
        logger.info(s);
    }

    public static void main(String[] args) {
        R r = new R();

        R.loadLogger();
        R.loadSettings();
        //R.loadNetworkManager();
        //R.loadGUILauncher();
        R.testDB();
    }

    private static void loadLogger() {
        logger = Logger.getLogger("JChatLog");
        FileHandler fh;
        try {
            // This block configure the logger with handler and formatter  
            fh = new FileHandler(Utils.getAppDir() + File.separator + "log.txt");
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
            fh.setFormatter(new SimpleFormatter());

            // the following statement is used to log any messages  
            logger.info("Logging START\n\n");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadSettings() {
        appDir = Utils.getAppDir();
        R.log("JChat Root Directory: " + appDir);
        settings = new UserSettings(appDir);
    }

    private static void loadNetworkManager() {
        nm = new NetworkManager();
        nm.connect();
    }

    private static void loadGUILauncher() {
        mf = new MainFrame();
        LoginTab tab = new LoginTab();
        mf.addTab(tab.getName(), JCHAT_LOGO, tab, tab.getToolTipText());
        mf.setVisible(true);
    }

    private static void testDB() {
        logger.info("\n\n\n\n\nBefore\n");
        printAllEntries();

        db.deleteAll(TestEntity.class);
        logger.info("\n\n\n\n\nDeleted all\n");
        printAllEntries();

        for (TestEntity.Status tes : TestEntity.Status.values()) {
            TestEntity c = new TestEntity();
            c.setName(tes.toString() + " Person");
            c.setStatus(tes);
            db.insert(c);
        }
        logger.info("\n\n\n\n\nInserted some entries\n");

        printAllEntries();

    }

    public static void releaseAll() {
        db = null;
        nm = null;
        settings = null;
        imh = null;
        logger = null;
    }

    private static void printAllEntries() {
        List<Object> lc = db.select(TestEntity.class);
        for (Object o : lc) {
            TestEntity c2 = (TestEntity) o;
            logger.log(Level.INFO, "{0}  {1}  {2}\n\n", new Object[]{c2.getId(), c2.getName(), c2.getStatus()});
        }
    }

}
