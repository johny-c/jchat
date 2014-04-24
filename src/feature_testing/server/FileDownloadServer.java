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

public class FileDownloadServer implements Runnable, Observer, Conventions {

    private ConcurrentHashMap<Long, ClientSessionHandler> threadUserMap;
    private InterMessageQueue interMessages;
    private SSLServerSocket serverSocket;
    private ThreadPoolExecutor executorPool;
    private FileDownloadHandler fileDownloadHandler;
    private SSLSocket clientSocket;
    //ServerGUI gui;
    private final static Logger LOGGER = Logger.getLogger(CommandServer.class
            .getName());

    FileDownloadServer(SSLServerSocket sslserversocket, ThreadPoolExecutor executorPool) {
        this.serverSocket = sslserversocket;
        this.executorPool = executorPool;

        //LOGGER.setLevel(Level.ALL);
        //LOGGER.log(Level.INFO, "FileDownloadServer created");
        R.log("FileDownloadServer created");
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
                fileDownloadHandler = new FileDownloadHandler(clientSocket);
                executorPool.execute(fileDownloadHandler);
                clientCounter++;
                R.log("New file download " + clientCounter);
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
