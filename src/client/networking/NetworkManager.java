package client.networking;

import common.pojos.Conventions;
import common.pojos.Message;
import common.pojos.OQueue;
import client.pojos.R;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class NetworkManager implements Conventions {

    private SSLSocketFactory sslsocketfactory;
    private SSLSocket sslsocket;
    private NetworkSpeaker speaker;
    private NetworkListener listener;
    private OQueue incomingMessages;
    private IncomingMessageHandler imh;
    private String caller = this.getClass().getSimpleName();

    public NetworkManager() {
        String trustStoreName = "jchatTrustStore.jks";
        String trustStorePassword = "123456";
        // String keyPassword = "123456";

        // no keystore required, just verifying the servers cert
        // set necessary truststore properties - using JKS
        System.setProperty("javax.net.ssl.trustStore", trustStoreName);
        System.setProperty("javax.net.ssl.trustStorePassword",
                trustStorePassword);
        //System.setProperty("java.protocol.handler.pkgs",
        //		"com.sun.net.ssl.internal.www.protocol");
        System.setProperty("javax.net.debug", "ssl");
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    }

    SSLSocket getSSLSocket() {
        return sslsocket;
    }

    public SSLSocket getFileUploadSocket() {
        SSLSocket fileSocket = null;
        try {

            fileSocket = (SSLSocket) sslsocketfactory.createSocket(InetAddress.getByName(SERVER_IP), SERVER_FILE_UPLOAD_PORT);
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return fileSocket;
    }

    public synchronized void connect() {
        if (sslsocket == null) {
            try {
                sslsocket = (SSLSocket) sslsocketfactory.createSocket(
                        InetAddress.getByName(SERVER_IP), SERVER_PORT);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //Utils.printSocketInfo(sslsocket);
            startListening();
        }
    }

    public synchronized void send(Message m) {
        if (speaker == null) {
            R.log(caller + " Speaker was null");
            speaker = new NetworkSpeaker(sslsocket);
            speaker.execute();
        }
        R.log(caller + " Putting msg in queue to send");
        speaker.put(m);
        R.log(caller + " Msg put in queue to send to Server");
    }

    private synchronized void startListening() {
        if (listener == null) {
            listener = new NetworkListener(sslsocket);
            listener.execute();
        }

    }

    public SSLSocket getFileDownloadSocket() {
        SSLSocket fileSocket = null;
        try {
            fileSocket = (SSLSocket) sslsocketfactory.createSocket(InetAddress.getByName(SERVER_IP), SERVER_FILE_DOWNLOAD_PORT);
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return fileSocket;
    }
}
