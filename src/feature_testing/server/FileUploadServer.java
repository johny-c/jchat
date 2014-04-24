package feature_testing.server;

import common.pojos.Conventions;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

public class FileUploadServer implements Runnable, Observer, Conventions {

    private ConcurrentHashMap<Long, ClientSessionHandler> threadUserMap;
    private InterMessageQueue interMessages;
    private SSLServerSocket serverSocket;
    private ThreadPoolExecutor executorPool;
    private FileUploadHandler fileUploadHandler;
    private SSLSocket clientSocket;
    //ServerGUI gui;
    private final static Logger LOGGER = Logger.getLogger(CommandServer.class
            .getName());

    FileUploadServer(SSLServerSocket sslserversocket, ThreadPoolExecutor executorPool) {
        this.serverSocket = sslserversocket;
        this.executorPool = executorPool;

        //LOGGER.setLevel(Level.ALL);
        //LOGGER.log(Level.INFO, "FileUploadServer created");
        R.log("FileUploadServer created");
    }

    @Override
    public void run() {
        R.log("File Server started\n\n");
        threadUserMap = new ConcurrentHashMap<Long, ClientSessionHandler>();
        interMessages = new InterMessageQueue();
        interMessages.addObserver(this);


        int clientCounter = 0;
        try {
            for (;;) {
                clientSocket = (SSLSocket) serverSocket.accept();
                fileUploadHandler = new FileUploadHandler(clientSocket);
                executorPool.execute(fileUploadHandler);
                clientCounter++;
                R.log("New file upload " + clientCounter);
            }
        } catch (IOException ex) {
            //shut down the pool
            executorPool.shutdown();
            try {
                executorPool.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
