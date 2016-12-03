package client.tasks;

import client.networking.NetworkManager;
import client.networking.R;
import common.db.entity.FileTransfer;
import common.utils.Conventions;
import static common.utils.Conventions.DEFAULT_SERVER_IP;
import static common.utils.Conventions.SERVER_IP;
import common.utils.Message;
import common.utils.MessageType;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.List;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class FileUploader extends SwingWorker<Void, String> implements Conventions {

    private static final int PROGRESS_CHECKPOINT = 10000;
    private final FileTransfer ft;
    private final File[] files;
    private final JLabel guiLabel;
    private final JTextArea guiArea;
    private SSLSocketFactory sslSocketFactory;
    private SSLSocket socket;
    private OutputStream os;
    private InputStream is;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private FileInputStream fis;
    private BufferedInputStream bis;
    private byte[] buffer;
    private static InetAddress serverInetAddress;

    public FileUploader(FileTransfer pendingFileUpload, File[] pendingFilesToUpload, JLabel fileUploadProgressLabel, JTextArea fileUploadProgressArea) {
        this.ft = pendingFileUpload;
        this.files = pendingFilesToUpload;
        this.guiLabel = fileUploadProgressLabel;
        this.guiArea = fileUploadProgressArea;
        R.log("Client: FileUploader created");
    }

    @Override
    protected Void doInBackground() {

        try {
            R.log("Client: FileUploader running");
            sslSocketFactory = NetworkManager.getSSLSocketfactory(); // connect to file upload socket
            serverInetAddress = InetAddress.getByName(R.getAppPrefs().get(SERVER_IP, DEFAULT_SERVER_IP));
            socket = (SSLSocket) sslSocketFactory.createSocket(serverInetAddress, SERVER_FILE_UPLOAD_PORT);

            os = socket.getOutputStream();
            is = socket.getInputStream();

            oos = new ObjectOutputStream(os);
            oos.flush();
            ois = new ObjectInputStream(is);

            setProgress(0);
            R.log("Client: Waiting for server to request filetransfer id");
            ois.read(); // Wait for server to request filetransfer id
            oos.writeInt(ft.getId());
            oos.flush();
            R.log("Client: Sent filetransfer id");
            int bytesWritten;
            int totalBytesWritten;
            int progressBytes;
            for (File file : files) {

                try {
                    R.log("Client: Waiting for server to request filename");
                    ois.read(); // Wait for server to request file name
                    oos.writeUTF(file.getName());
                    oos.flush();
                    R.log("Client: Waiting for server to request file upload");
                    ois.read(); // Wait for server to request file upload

                    publish("1 Sending " + file.getName());

                    fis = new FileInputStream(file.getAbsolutePath());
                    bis = new BufferedInputStream(fis);
                    buffer = new byte[FILE_BUFFER_SIZE];

                    totalBytesWritten = 0;
                    progressBytes = 0;
                    R.log("Starting file upload . . .");
                    while ((bytesWritten = bis.read(buffer, 0, buffer.length)) > 0) {
                        os.write(buffer, 0, bytesWritten);
                        totalBytesWritten += bytesWritten;
                        progressBytes += bytesWritten;

                        if (progressBytes > PROGRESS_CHECKPOINT || totalBytesWritten == file.length()) {
                            setProgress(Math.min((int) ((100.0 * totalBytesWritten) / file.length()), 100));
                            progressBytes = 0;
                        }
                    }

                    os.flush();

                    publish("2 " + file.getName() + " uploaded.");
                } catch (IOException ex) {
                    R.log(ex.toString());
                    publish("2 Upload of " + file.getName() + " was interrupted!");
                    return null;
                }

            }

            publish("2  Upload completed !");
            R.log("Completed file upload . . .");

            os.flush();

            R.log("Flushed outputstream . . .");

        } catch (IOException ex) {
            R.log(ex.toString());
        } finally {
            closeStreams();
            R.log("Closed all streams");
        }
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        for (String s : chunks) {
            if (s.startsWith("1")) {
                guiLabel.setText(s.substring(2));
            } else {
                guiArea.append(s.substring(2) + "\n");
            }
        }
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    protected void done() {
        Toolkit.getDefaultToolkit().beep();
        Message m = new Message(MessageType.FILE_UPLOAD_COMPLETED, ft.getId());
        NetworkManager.send(m);
    }

    private void closeStreams() {
        try {
            bis.close();
            fis.close();
            ois.close();
            is.close();
            oos.close();
            os.close();
            socket.close();
        } catch (IOException ex) {
            R.log(ex.toString());
            publish("2 Closing streams failed.");
        }
    }

}
