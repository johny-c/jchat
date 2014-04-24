package feature_testing.server;

import common.pojos.Conventions;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

public class CommandServer implements Runnable, Observer, Conventions {

    private ConcurrentHashMap<Long, ClientSessionHandler> threadUserMap;
    private InterMessageQueue interMessages;
    private SSLServerSocket serverSocket;
    private static ThreadPoolExecutor executorPool;
    private ClientSessionHandler clientHandler;
    private SSLSocket clientSocket;
    //ServerGUI gui;
    private final static Logger LOGGER = Logger.getLogger(CommandServer.class
            .getName());

    CommandServer(SSLServerSocket sslserversocket, ThreadPoolExecutor executorPool) {
        this.serverSocket = sslserversocket;
        this.executorPool = executorPool;
        //this.gui = gui;
        R.log("CommandServer created");
        //LOGGER.log(Level.INFO, "Created");
    }

    @Override
    public void run() {
        R.log("Command Server starts running\n\n");
        threadUserMap = new ConcurrentHashMap<Long, ClientSessionHandler>();
        interMessages = new InterMessageQueue();
        interMessages.addObserver(this);


        int clientCounter = 0;
        try {
            for (;;) {
                clientSocket = (SSLSocket) serverSocket.accept();
                clientHandler = new ClientSessionHandler(clientSocket, threadUserMap, interMessages);
                executorPool.execute(clientHandler);
                clientCounter++;
                R.log("New client " + clientCounter);
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
        // New InterMessage
        InterMessage m = (InterMessage) arg;
        if (m != null) {
            ClientSessionHandler client = threadUserMap.get(m.getTargetUserId());
            if (client == null) {
                System.out.println("Recipient is offline");
            } else {
                System.out.println("Sent inter message to online user");
                client.putInQueue(m.getMessage());
            }
        } else {
            System.out.println("Message iis null");
        }
    }
}
