package client.networking;

import common.utils.Conventions;
import common.utils.Message;
import common.utils.MessageType;
import common.utils.OQueue;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Observer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class NetworkManager implements Conventions {

    private static SSLSocketFactory sslSocketFactory;
    private static SSLSocket sslSocket;
    private static NetworkSpeaker speaker;
    private static NetworkListener listener;
    private static IncomingMessageHandler imh;
    private static BlockingQueue<Message> msgsFromNet; // Producer: NLT, Consumer: IMHT 
    private static BlockingQueue<Message> msgsToNet; // Producers: IMHT, EDT, Consumer: NST
    private static OQueue msgsToGUI; // Producer: IMHT, Consumer: EDT
    private static final int MSGS_FROM_NET_CAPACITY = 32;
    private static final int MSGS_TO_NET_CAPACITY = 32;
    private static final int MSGS_TO_GUI_CAPACITY = 32;
    private static boolean CONNECTED = false;
    public static final String BROADCAST = "broadcast";
    private static InetAddress serverInetAddress;
    private static final String[] ipAddresses = {
        "jchat.hopto.org",
        "fe80::290:f5ff:feef:d40e",
        "localhost",
        "192.168.2.2",
        "192.168.2.16",};

    public synchronized static boolean isConnected() {
        return CONNECTED;
    }

    public synchronized static void setConnected(boolean connected) {
        CONNECTED = connected;
    }

    public NetworkManager() {
        System.setProperty("javax.net.debug", "ssl");
        loadTrustStore();
        sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        msgsToGUI = new OQueue(MSGS_TO_GUI_CAPACITY);
        //subscribers = new ArrayList<>();
    }

    private void loadTrustStore() {

        try {
            String trustStoreName = "jchatTrustStore.jks";
            String trustStorePassword = "123456";
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream keystoreStream = ClassLoader.getSystemResourceAsStream(trustStoreName);
            keystore.load(keystoreStream, trustStorePassword.toCharArray());
            trustManagerFactory.init(keystore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustManagers, null);
            SSLContext.setDefault(sc);
        } catch (NoSuchAlgorithmException | KeyStoreException | IOException | CertificateException | KeyManagementException ex) {
            R.log(ex.toString());
        }

    }

    public static boolean connect() {

        if (!isConnected()) {

            R.log("\n\n CONNECTING TO THE SERVER . . .\n\n");
            R.log("\n\n1. CREATING SSLSOCKET CONNECTION\n\n");

            msgsFromNet = new ArrayBlockingQueue(MSGS_FROM_NET_CAPACITY);
            msgsToNet = new ArrayBlockingQueue(MSGS_TO_NET_CAPACITY);

            R.log("Searching for " + serverInetAddress.toString());
            try {
                serverInetAddress = InetAddress.getByName(R.getAppPrefs().get(SERVER_IP, DEFAULT_SERVER_IP));
            } catch (UnknownHostException e) {
                R.log(e.toString());
                disconnect();
                return false;
            }

            R.log("Connecting to " + serverInetAddress.toString() + " , port: " + SERVER_PORT);
            try {
                sslSocket = (SSLSocket) sslSocketFactory.createSocket(serverInetAddress, SERVER_PORT);
            } catch (IOException e) {
                R.log(e.toString());
                disconnect();
                return false;
            }

            R.log("\n\n2. STARTING NETWORK SPEAKER THREAD \n\n");
            speaker = new NetworkSpeaker(sslSocket, msgsToNet);
            new Thread(speaker).start();

            R.log("\n\n3. STARTING INCOMING MESSAGE HANDLER THREAD \n\n");
            imh = new IncomingMessageHandler(msgsFromNet, msgsToNet, msgsToGUI);
            new Thread(imh).start();

            R.log("\n\n4. STARTING NETWORK LISTENER THREAD \n\n");
            listener = new NetworkListener(sslSocket, msgsFromNet);
            new Thread(listener).start();

            setConnected(true);
        }

        return isConnected();
    }

    public static void send(Message m) {
        if (isConnected()) {
            try {
                R.log(" Putting " + m.getType().toString() + " to msgsToNet Queue");
                msgsToNet.put(m);
                R.log("" + m.getType().toString() + " put to msgsToNet Queue, NST will handle now");

            } catch (InterruptedException ex) {
                R.log(ex.toString());
            }
        }
    }

    public static Message receive() {
        Message m = null;
        if (isConnected()) {
            try {
                m = msgsFromNet.poll(15, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                R.log(ex.toString());
            }
        }
        return m;
    }

    public static void disconnect() {

        R.log("\n\n1. STOPING NETWORK LISTENER THREAD \n\n");
        listener.stop();
        R.log("\n\n2. STOPING INCOMING MESSAGE HANDLER THREAD \n\n");
        imh.stop();
        R.log("\n\n3. STOPING NETWORK SPEAKER THREAD \n\n");
        speaker.stop();

        if (sslSocket != null) {
            try {
                sslSocket.close();
            } catch (IOException ex) {
                R.log(ex.toString());
            }
        }

        msgsFromNet.clear();
        msgsToNet.clear();
        R.log("Network Manager is being set to not connected");
        Message m = new Message(MessageType.NO_CONNECTION_BROADCAST, "");
        broadcast(m);
        setConnected(false);
    }

    public static SSLSocketFactory getSSLSocketfactory() {
        return sslSocketFactory;
    }

    public static void subscribe(Observer o) {
        R.log(o.getClass().getSimpleName() + " subscribing for incoming messages");
        try {
            msgsToGUI.addObserver(o);
        } catch (NullPointerException e) {
            R.log("NPE while subscribing " + o.getClass().getSimpleName());
        }
    }

    public static void unsubscribe(Observer o) {
        msgsToGUI.deleteObserver(o);
    }

    public static void unsubscribeAll() {
        msgsToGUI.deleteObservers();
    }

    private static void broadcast(Message m) {
        msgsToGUI.offerNotify(m, BROADCAST);
        msgsToGUI.poll();
    }

    public static String[] getIpAddresses() {
        return ipAddresses;
    }

}
