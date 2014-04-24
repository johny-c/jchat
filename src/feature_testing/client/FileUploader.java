package feature_testing.client;

import common.pojos.Conventions;
import common.db.entity.FileTransfer;
import client.gui.ChatPane;
import client.pojos.R;
import java.awt.Component;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class FileUploader extends SwingWorker<Integer, Integer> implements Conventions {

    private static final int PROGRESS_CHECKPOINT = 10000;
    private FileTransfer fileTransfer;
    private File file;
    private long fileSize;
    private Component gui;
    private SSLSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private FileInputStream fis;
    private BufferedInputStream bis;
    private byte[] buffer;

    FileUploader(FileTransfer fileTransfer, Component gui) {
        this.fileTransfer = fileTransfer;
        this.file = new File(fileTransfer.getSenderPath());
        this.fileSize = file.length();
        this.gui = gui;
        R.log("Client: FileUploader created");
        this.socket = R.getNm().getFileUploadSocket(); // connect to file upload socket
    }

    @Override
    protected Integer doInBackground() throws IOException {
        R.log("Client: FileUploader running");
        if (socket == null) {
            return -4;
        }

        outputStream = socket.getOutputStream();
        oos = new ObjectOutputStream(outputStream);
        oos.writeObject(fileTransfer);
        oos.flush();

        inputStream = socket.getInputStream();
        ois = new ObjectInputStream(inputStream);

        //Initialize progress property.
        setProgress(0);
        int response = ois.readInt();
        if (response == FILE_START_UPLOAD_OK) {

            // File input from local file
            fis = new FileInputStream(fileTransfer.getSenderPath());

            // Buffered input from File input
            bis = new BufferedInputStream(fis);
            buffer = new byte[FILE_BUFFER_SIZE];

            int bytesWritten = 0;
            int totalBytesWritten = 0;
            int progressCounter = 0;
            R.log("Starting file upload . . .");
            while ((bytesWritten = bis.read(buffer, 0, buffer.length)) > 0) {
                outputStream.write(buffer, 0, bytesWritten);
                //outputStream.flush();
                totalBytesWritten += bytesWritten;
                progressCounter += bytesWritten;

                if (progressCounter > PROGRESS_CHECKPOINT || totalBytesWritten == fileSize) {
                    setProgress(Math.min((int) ((100.0 * totalBytesWritten) / fileSize), 100));
                    progressCounter = 0;
                }
            }
            R.log("Completed file upload . . .");

            outputStream.flush();
            R.log("Flushed outputstream . . .");
            // Until here ok
            //int ok = ois.readInt();
            //R.log("Read incoming int . . . ok = " + ok);

            closeStreams();
            R.log("Closed all streams");
            //if (ok == FILE_UPLOAD_SUCCESS) {
            return 1;
            //}

        }
        return -2;
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        int code = 0;
        try {
            code = get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        switch (code) {
            case 1:
                Toolkit.getDefaultToolkit().beep();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ChatPane.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            case 0:
                R.log("Could not get code...");
                break;

            case -1:
                JOptionPane.showMessageDialog(gui,
                        "The file you want to send is not found!",
                        "File not found!", JOptionPane.ERROR_MESSAGE);
                break;

            case -2:
                JOptionPane.showMessageDialog(gui, "File upload interrupted!",
                        "Sorry, something went wrong while sending your file.",
                        JOptionPane.ERROR_MESSAGE);
                break;

            case -3:
                JOptionPane.showMessageDialog(gui, "File upload failed!",
                        "Sorry, something went exceptionally wrong while sending your file.",
                        JOptionPane.ERROR_MESSAGE);
                break;

            case -4:
                JOptionPane.showMessageDialog(gui, "File upload failed!",
                        "Sorry, could not create socket connection for file upload.",
                        JOptionPane.ERROR_MESSAGE);
                break;

        }
    }

    private void closeStreams() throws IOException {
        bis.close();
        fis.close();
        ois.close();
        inputStream.close();
        oos.close();
        outputStream.close();
        socket.close();
    }
}
