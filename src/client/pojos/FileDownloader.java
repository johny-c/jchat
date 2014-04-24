package client.pojos;

import common.pojos.Conventions;
import common.pojos.Message;
import common.db.entity.FileTransfer;
import client.gui.NotificationCell;
import java.awt.Component;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.swing.SwingWorker;

public class FileDownloader extends SwingWorker<Integer, Integer> implements Conventions {

    private FileTransfer fileTransfer;
    //private Database db;
    private static final int PROGRESS_CHECKPOINT = 10000;
    private File file;
    private long fileSize;
    private Component gui;
    private SSLSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private byte[] buffer;

    public FileDownloader(Component gui) {
        this.gui = gui;
        R.log("Client: FileDownloader created");
        this.socket = R.getFileDownloadSocket(); // connect to file download socket
    }

    public FileDownloader(Integer fileId, Component gui) {
        this(gui);
        // fetch fileTransfer from db with fileId
        //this.fileTransfer = db.findFileByFileId(fileId);
        this.file = new File(fileTransfer.getSenderPath());
        this.fileSize = file.length();

    }

    public FileDownloader(FileTransfer fileTransfer, Component gui) {
        this(gui);
        this.file = new File(fileTransfer.getSenderPath());
        this.fileSize = file.length();
    }

    @Override
    protected Integer doInBackground() throws Exception {
        R.log("Client: FileDownloader running");
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

        if (response == FILE_START_DOWNLOAD_OK) {
            // File output to local file
            //if(ft.getSourceUserId() ) ft.getId()
            String downloadsFolder = UserSettings.get(UserSettings.DOWNLOAD_FOLDER, "Downloads");
            File destinationFile = new File(downloadsFolder + File.separator + fileTransfer.getFileName());
            R.log(destinationFile.getAbsolutePath());
            destinationFile.getParentFile().mkdirs();
            boolean fileCreated = destinationFile.createNewFile();

            if (fileCreated) {
                R.log("File created successfully");
            }

            try {
                fos = new FileOutputStream(destinationFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Buffered output to local file
            bos = new BufferedOutputStream(fos);
            buffer = new byte[FILE_BUFFER_SIZE];

            int bytesRead = 0;
            int totalBytesRead = 0;
            int progressCounter = 0;

            oos.writeInt(FILE_START_DOWNLOAD_OK);
            oos.flush();

            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) > 0) {
                bos.write(buffer, 0, bytesRead);
                bos.flush();
                totalBytesRead += bytesRead;
                progressCounter += bytesRead;
                if (progressCounter > PROGRESS_CHECKPOINT || totalBytesRead == fileSize) {
                    R.log("Received and wrote another 100KB, totalBytesRead = " + totalBytesRead);
                    setProgress(Math.min((int) ((100.0 * totalBytesRead) / fileSize), 100));
                    progressCounter = 0;
                }

                R.log("Receiving bytes, bytesRead = " + bytesRead);

            }
            R.log("Exited reading loop");
            oos.flush();

            //R.log("Got " + totalBytesRead + " from a file of " + ft.getFileSize() + " bytes.");
            if (totalBytesRead == fileSize) {
                //oos.writeInt(FILE_UPLOAD_SUCCESS);
                //oos.flush();
                R.log("Sent download success message");
            }
            closeStreams();
            R.log("Closed all streams");
        }

        closeStreams();
        return null;
    }

    private void closeStreams() throws IOException {

        bos.close();
        fos.close();
        ois.close();
        oos.close();
        inputStream.close();
        outputStream.close();
        socket.close();

    }

    @Override
    protected void done() {
        R.send(new Message(FILE_DOWNLOAD_COMPLETED, fileTransfer));
    }
}
