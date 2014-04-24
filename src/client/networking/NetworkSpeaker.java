package client.networking;

import common.pojos.Conventions;
import common.pojos.Message;
import common.pojos.Utils;
import client.pojos.R;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.swing.SwingWorker;

public class NetworkSpeaker extends SwingWorker<Void, Boolean> implements Conventions {

    private SSLSocket sslSocket;
    private OutputStream outputStream;
    private BlockingQueue<Message> messagesToSend;
    private String caller = this.getClass().getSimpleName();
    private ObjectOutputStream mout;

    NetworkSpeaker(SSLSocket socket) {
        sslSocket = socket;
        try {
            outputStream = sslSocket.getOutputStream();
        } catch (IOException e) {
            Utils.print(caller, e.toString());
            e.printStackTrace();
        }



        try {
            mout = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mout.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkSpeaker.class.getName()).log(Level.SEVERE, null, ex);
        }
        messagesToSend = new ArrayBlockingQueue<>(CLIENT_MSG_QUEUE_IN_CAPACITY);
    }

    @Override
    protected Void doInBackground() throws Exception {

        Message msg;
        boolean connectionIsNeeded = true;

        while (connectionIsNeeded) {
            msg = messagesToSend.take();
            R.log("Client NetworkSpeaker: I am gonna send :" + msg.getDescription());

            if (msg != null) {
                mout.writeObject(msg);
                mout.flush();
                //Utils.print(caller, "Message written to bufferedWriter");
                if (msg.getCode() == COMMUNICATION_TERMINATION_REQUEST) {
                    connectionIsNeeded = false;
                }
            } else {
                //Utils.print(caller, "Msg is null");
                R.log("NetworkSpeaker: Msg is null");
            }
        }

        mout.close();
        return null;
    }

    /**
     * Method used by NetworkManager puts messages that must be sent to the
     * Server
     *
     * @param msg
     */
    void put(Message msg) {
        //Utils.print(caller, "Gonna offer");
        boolean space = messagesToSend.offer(msg);
        if (space) {
            R.log(caller + " There is space in the queue");
        } else {
            R.log(caller + " No space in the queue");
        }
    }
}
