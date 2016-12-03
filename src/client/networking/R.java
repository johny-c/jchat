package client.networking;

import client.db.util.DB;
import client.gui.MainFrame;
import client.gui.LoginTab;
import client.gui.SignupTab;
import common.db.entity.UserAccount;
import common.utils.Conventions;
import common.utils.Utils;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.prefs.Preferences;
import javax.swing.SwingUtilities;

public class R implements Conventions {

    private static BlockingQueue LOGS_QUEUE;
    private static JChatLogger JCHAT_LOGGER;
    private static UserAccount USER_ACCOUNT;
    private static Preferences APP_PREFS;
    private static Random RANDOM;
    private static MainFrame MAINFRAME; // EDT Thread

    private static void startLogger() {
        LOGS_QUEUE = new ArrayBlockingQueue(100);
        JCHAT_LOGGER = new JChatLogger(LOGS_QUEUE);
        new Thread(JCHAT_LOGGER).start();
    }

    // Synhronized because logger object is accessed by many - all threads
    public static synchronized void log(String s) {
        LOGS_QUEUE.offer(Thread.currentThread().getName() + "  " + s);
    }

    private static void createUserAccount() {
        USER_ACCOUNT = new UserAccount();
        USER_ACCOUNT.setStatus(UserAccount.Status.OFFLINE);
    }

    public static synchronized void setUserAccount(UserAccount changedUser) {
        R.log("setting the USER account");
        USER_ACCOUNT = changedUser;
    }

    // Accessed by EDT and NetworkListener 
    public static synchronized UserAccount getUserAccount() {
        R.log("getting the USER account");
        return USER_ACCOUNT;
    }

    private static void loadAppPrefs() {
        APP_PREFS = Preferences.userRoot().node(JCHAT_PREFS);
        Utils.printPrefs(APP_PREFS, "App");
    }

    // Accessed by EDT and other Task Threads
    public static synchronized Preferences getAppPrefs() {
        return APP_PREFS;
    }

    public static Random getRandom() {
        return RANDOM;
    }

    // Only accessed from EDT
    public static MainFrame getMf() {
        return MAINFRAME;
    }

    public static void terminate() {
        JCHAT_LOGGER.stop();
        MAINFRAME.dispose();
        System.exit(0);
    }

    public static void main(final String[] args) {

        Thread.currentThread().setName("MAIN THREAD");

        startLogger();

        createUserAccount();

        DB database = new DB(); // Create or load db if it does not exist

        loadAppPrefs();

        RANDOM = new Random(); // Seed from current time (program startup)

        NetworkManager networkManager = new NetworkManager(); // NST, IMHT, NLT

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MAINFRAME = new MainFrame();
                NetworkManager.subscribe(MAINFRAME);

                if (args.length > 0) {
                    SignupTab signupTab = new SignupTab(args[0]);
                    MAINFRAME.getT().addTab(signupTab.getName(), JCHAT_LOGO, signupTab, SIGNUP_TAB_TIP);
                } else {
                    LoginTab loginTab = new LoginTab();
                    MAINFRAME.getT().addTab(loginTab.getName(), JCHAT_LOGO, loginTab, LOGIN_TAB_TIP);
                }

                MAINFRAME.pack();
                MAINFRAME.setVisible(true);
            }
        });

        Utils.printThreads();
    }
}
