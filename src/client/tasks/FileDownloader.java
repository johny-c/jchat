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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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

public class FileDownloader extends SwingWorker<Void, String> implements Conventions {

    private static final int PROGRESS_CHECKPOINT = 10000;
    private final FileTransfer fileTransfer;
    private final String saveFolder;
    private final JLabel guiLabel;
    private final JTextArea guiArea;
    private SSLSocketFactory sslSocketFactory;
    private SSLSocket socket;
    private OutputStream os;
    private InputStream is;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private byte[] buffer;
    private File destinationFile;
    private static InetAddress serverInetAddress;

    public FileDownloader(FileTransfer fileTransfer, String saveFolder, JLabel guiLabel, JTextArea guiArea) {
        this.fileTransfer = fileTransfer;
        this.saveFolder = saveFolder;
        this.guiLabel = guiLabel;
        this.guiArea = guiArea;
        R.log("Client: Default FileDownloader created");
    }

    @Override
    protected Void doInBackground() {
        try {
            R.log("Client: FileDownloader running");
            sslSocketFactory = NetworkManager.getSSLSocketfactory(); // connect to file upload socket
            serverInetAddress = InetAddress.getByName(R.getAppPrefs().get(SERVER_IP, DEFAULT_SERVER_IP));
            socket = (SSLSocket) sslSocketFactory.createSocket(serverInetAddress, SERVER_FILE_DOWNLOAD_PORT);

            os = socket.getOutputStream();
            is = socket.getInputStream();

            oos = new ObjectOutputStream(os);
            oos.flush();
            ois = new ObjectInputStream(is);

            oos.writeInt(fileTransfer.getId());
            oos.flush();

            setProgress(0);

            int bytesRead;
            int totalBytesRead;
            int progressBytes;
            String name = "";
            long size;
            for (int i = 0; i < fileTransfer.getFilesCount(); i++) {
                try {

                    oos.write(1);
                    oos.flush();    // requesting file name
                    name = ois.readUTF();

                    destinationFile = new File(saveFolder + File.separator + name);
                    R.log(destinationFile.getAbsolutePath());
                    destinationFile.getParentFile().mkdirs();
                    destinationFile.createNewFile();

                    oos.write(2);
                    oos.flush();    // requesting file size
                    size = ois.readLong();

                    publish("1 Downloading " + name + " , Size: " + size / 1024 + " KB");

                    fos = new FileOutputStream(destinationFile);
                    bos = new BufferedOutputStream(fos);
                    buffer = new byte[FILE_BUFFER_SIZE];

                    bytesRead = 0;
                    totalBytesRead = 0;
                    progressBytes = 0;

                    oos.write(3); // Indicate ready to receive file
                    oos.flush();

                    while ((bytesRead = is.read(buffer, 0, buffer.length)) > 0) {
                        bos.write(buffer, 0, bytesRead);
                        //bos.flush();
                        totalBytesRead += bytesRead;
                        progressBytes += bytesRead;
                        if (progressBytes > PROGRESS_CHECKPOINT || totalBytesRead == size) {
                            R.log("Received and wrote another 100KB, totalBytesRead = " + totalBytesRead);
                            setProgress(Math.min((int) ((100.0 * totalBytesRead) / size), 100));
                            progressBytes = 0;
                        }

                        R.log("Receiving bytes, bytesRead = " + bytesRead);

                    }
                    R.log("Exited reading loop");
                    publish("2 " + name + " downloaded.");

                } catch (IOException ex) {
                    R.log(ex.toString());
                    publish("2 Download of " + name + " was interrupted!");
                    break;
                }

                bos.flush();
            }

        } catch (IOException ex) {
            R.log(ex.toString());
            publish("2 Files Download failed!");

        } finally {
            closeStreams();
            R.log("Closed all streams");
        }

        return null;
    }

    private void closeStreams() {

        try {
            bos.close();
            fos.close();
            ois.close();
            oos.close();
            is.close();
            os.close();
            socket.close();
        } catch (IOException ex) {
            R.log(ex.toString());
            publish("2 Closing streams failed.");
        }

    }

    @Override
    protected void process(List<String> chunks) {
        if (guiLabel == null) {
            return;
        }
        for (String s : chunks) {
            if (s.startsWith("1")) {
                guiLabel.setText(s.substring(2));
            } else {
                guiArea.append(s.substring(2) + "\n");
            }
        }
    }

    @Override
    protected void done() {
        Toolkit.getDefaultToolkit().beep();
        Message m = new Message(MessageType.FILE_DOWNLOAD_COMPLETED, fileTransfer.getId());
        NetworkManager.send(m);
    }

}
