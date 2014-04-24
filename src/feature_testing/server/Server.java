package feature_testing.server;

import common.pojos.Conventions;
import static common.pojos.Conventions.SERVER_FILE_UPLOAD_PORT;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class Server implements Conventions {

    private String keystoreName = "jchatKeyStore.jks";
    private char keystorePassword[] = "123456".toCharArray();
    private char keyPassword[] = "123456".toCharArray();
    private KeyStore keystore;
    private KeyManagerFactory kmf;
    private SSLContext sslContext;
    private SSLServerSocketFactory sslServerSocketFactory;
    private static SSLServerSocket sslServerSocket;
    private static SSLServerSocket fileUploadServerSocket;
    private static SSLServerSocket fileDownloadServerSocket;
    //private ExecutorService pool;
    private final static int poolSize = 50;
    private SSLSocket sslsocket;
    private ClientSessionHandler clientHandler;
    private volatile Thread thread;
    private final static int corePoolSize = 2;
    private final static int maxPoolSize = 100;
    private final static long keepAliveTime = 60;
    private final static TimeUnit unit = TimeUnit.SECONDS;
    ThreadFactory threadFactory;
    static ThreadPoolExecutor executorPool;
    JChatRejectedExecutionHandler rejectionHandler;
    private ConcurrentHashMap<Long, ClientSessionHandler> threadUserMap;
    private InterMessageQueue interMessages;
    //static ServerGUI gui;
    private final static Logger LOGGER = Logger.getLogger(Server.class
            .getName());

    public Server() {

        setupSSL();

        //RejectedExecutionHandler implementation
        rejectionHandler = new JChatRejectedExecutionHandler();
        //Get the ThreadFactory implementation to use
        threadFactory = Executors.defaultThreadFactory();
        //creating the ThreadPoolExecutor
        executorPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
                unit, new ArrayBlockingQueue<Runnable>(2), threadFactory, rejectionHandler);
    }

    private void setupSSL() {
        try {
            keystore = KeyStore.getInstance("JKS");
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            keystore.load(new FileInputStream(keystoreName), keystorePassword);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            kmf = KeyManagerFactory.getInstance("SunX509");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            kmf.init(keystore, keyPassword);
        } catch (UnrecoverableKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            sslContext.init(kmf.getKeyManagers(), null, null);
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sslServerSocketFactory = sslContext.getServerSocketFactory();
        try {
            sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(SERVER_PORT);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Utils.printServerSocketInfo(sslserversocket);
        try {
            fileUploadServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(SERVER_FILE_UPLOAD_PORT);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Utils.printServerSocketInfo(fileuploadserversocket);

        try {
            fileDownloadServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(SERVER_FILE_DOWNLOAD_PORT);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Utils.printServerSocketInfo(filedownloadserversocket);

        //System.out.println("Server started:");

    }

    /**
     * Launch the application server.
     */
    public static void main(String[] args) {

        new R();
        //gui = new ServerGUI();
        Server server = new Server();
        //Server.LOGGER.setLevel(Level.ALL);
        //Server.LOGGER.log(Level.INFO, "Opened logger");

        //submit work to the thread pool
        CommandServer cmdServer = new CommandServer(sslServerSocket, executorPool);
        Thread cmdServerThread = new Thread(cmdServer);
        cmdServerThread.start();

        FileDownloadServer fileUploadServer = new FileDownloadServer(fileUploadServerSocket, executorPool);
        Thread fileUploadServerThread = new Thread(fileUploadServer);
        fileUploadServerThread.start();

        FileDownloadServer fileDownloadServer = new FileDownloadServer(fileDownloadServerSocket, executorPool);
        Thread fileDownloadServerThread = new Thread(fileDownloadServer);
        fileDownloadServerThread.run();
    }
}
