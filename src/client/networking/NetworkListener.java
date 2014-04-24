package client.networking;

import common.pojos.Message;
import common.pojos.OQueue;
import common.pojos.Utils;
import client.pojos.R;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.net.ssl.SSLSocket;
import javax.swing.SwingWorker;

public class NetworkListener extends SwingWorker<Void, Message> {

    private SSLSocket sslsocket;
    private InputStream inputStream;
    private ObjectInputStream mis;
    private OQueue messagesReceived;
    private String caller = this.getClass().getSimpleName();
    private final IncomingMessageHandler imh;

    NetworkListener(SSLSocket socket) {
        sslsocket = socket;
        imh = new IncomingMessageHandler();
        messagesReceived = new OQueue(imh);
        R.setIMH(imh);
        try {
            inputStream = sslsocket.getInputStream();
        } catch (IOException e) {
            Utils.print(caller, e.toString());
            e.printStackTrace();
        }

        try {
            mis = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Void doInBackground() throws Exception {
        Message incomingMessage = null;
        while ((incomingMessage = (Message) mis.readObject()) != null) {
            Utils.print(caller, incomingMessage.toString());
            messagesReceived.offer(incomingMessage);
        }

        mis.close();
        return null;
    }
}
