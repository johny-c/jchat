package client.networking;

import common.db.entity.UserAccount;
import common.utils.Conventions;
import common.utils.Message;
import common.utils.MessageType;
import common.utils.Utils;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

public class NetworkSpeaker implements Runnable, Conventions {

    private final SSLSocket sslSocket;
    private OutputStream outputStream;
    private ObjectOutputStream mout;
    private final BlockingQueue<Message> messagesToSend;
    private final String threadName = "NETWORK SPEAKER THREAD";
    private volatile boolean needed;
    private volatile Thread thisThread;

    NetworkSpeaker(SSLSocket socket, BlockingQueue msgsToNet) {
        sslSocket = socket;
        messagesToSend = msgsToNet;
    }

    @Override
    public void run() {

        try {
            Thread.currentThread().setName(threadName);
            thisThread = Thread.currentThread();

            outputStream = sslSocket.getOutputStream();
            mout = new ObjectOutputStream(outputStream);
            mout.flush();

            Message msg;
            needed = true;
            while (needed) {
                R.log("Waiting for a message to be sent");

                try {
                    msg = messagesToSend.take();
                } catch (InterruptedException ex) {
                    R.log(ex.toString());
                    close();
                    return;
                }

                R.log("I am gonna send: " + msg.getType().toString());
                if (msg.getContent() instanceof UserAccount) {
                    R.log("The user object I send:\n");
                    Utils.printInfo((UserAccount) msg.getContent());
                }
                mout.writeObject(msg);
                mout.flush();
                mout.reset();
                R.log("Message is flushed");

                if (msg.getType() == MessageType.LOGOUT_REQUEST) {
                    NetworkManager.disconnect();
                }

            }
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
        if (mout != null) {
            try {
                mout.flush();
                mout.reset();
                mout.close();
            } catch (IOException ex) {
                R.log(ex.toString() + " WHILE CLOSING OBJECT OUTPUT STREAM");
            }
        }
        NetworkManager.disconnect();
    }

}
