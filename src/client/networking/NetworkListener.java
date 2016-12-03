package client.networking;

import common.utils.Message;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

public class NetworkListener implements Runnable { //     extends SwingWorker<Void, Void> {

    private final SSLSocket sslsocket;
    private InputStream inputStream;
    private ObjectInputStream mis;
    private final BlockingQueue<Message> messagesReceived;
    private final String threadName = "NETWORK LISTENER THREAD";
    private volatile boolean needed;
    private volatile Thread thisThread;

    NetworkListener(SSLSocket socket, BlockingQueue msgsFromNet) {
        sslsocket = socket;
        messagesReceived = msgsFromNet;
    }

    @Override
    public void run() {

        try {
            Thread.currentThread().setName(threadName);
            thisThread = Thread.currentThread();

            inputStream = sslsocket.getInputStream();
            mis = new ObjectInputStream(inputStream);

            Message incomingMessage;
            needed = true;
            while (needed) {

                try {
                    incomingMessage = (Message) mis.readObject();
                } catch (ClassNotFoundException ex) {
                    R.log(ex.toString());
                    close();
                    return;
                }
                R.log("receiving:  " + incomingMessage.getType().toString());
                boolean o = messagesReceived.offer(incomingMessage);
                R.log("Offered incoming msg to queue : " + o);
            }

            R.log("FINISHED");
        } catch (IOException ex) {
            R.log(ex.toString());
            close();
        }

    }

    public void stop() {
        needed = false;
        thisThread.interrupt();
    }

    private void close() {
        if (mis != null) {
            try {
                mis.close();
            } catch (IOException ex) {
                R.log(ex.toString() + " WHILE CLOSING OBJECT INPUT STREAM");
            }
        }
        NetworkManager.disconnect();
    }

}
