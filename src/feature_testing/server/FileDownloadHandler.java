package feature_testing.server;

import common.pojos.Conventions;
import common.pojos.Utils;
import common.db.entity.FileTransfer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

class FileDownloadHandler implements Runnable, Conventions {

    private SSLSocket sslsocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private FileInputStream fis;
    private BufferedInputStream bis;
    private byte[] buffer;
    private FileTransfer fileTransfer;

    FileDownloadHandler(SSLSocket clientSocket) {
        sslsocket = clientSocket;
        Utils.printSocketInfo(sslsocket);

        try {
            outputStream = sslsocket.getOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(FileUploadHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            inputStream = sslsocket.getInputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            oos = new ObjectOutputStream(outputStream);
            oos.flush();
            ois = new ObjectInputStream(inputStream);

            Object o = null;
            try {
                o = ois.readObject();
            } catch (IOException ex) {
                Logger.getLogger(FileUploadHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FileUploadHandler.class.getName()).log(Level.SEVERE, null, ex);
            }


            // IF FILE INFO IS OK, PREPARE TO SEND THE FILE
            if (o != null) {
                if (o.getClass().equals(FileTransfer.class)) {
                    fileTransfer = (FileTransfer) o;
                } else {
                    R.log("Sth went down...Object is not FileTransfer");
                    closeStreams();
                    return;
                }

                if (Utils.isValidFile(fileTransfer)) {
                    // SEND SIGNAL THAT SERVER IS READY FOR FILE DOWNLOAD
                    oos.writeInt(FILE_START_DOWNLOAD_OK);
                    oos.flush();
                    R.log("Sent signal server is ready to send file");

                    int response = ois.readInt();
                    if (response == FILE_START_UPLOAD_OK) {

                        // File input from local file
                        fis = new FileInputStream(fileTransfer.getServerPath());

                        // Buffered input from File input
                        bis = new BufferedInputStream(fis);
                        buffer = new byte[FILE_BUFFER_SIZE];

                        int bytesWritten = 0;
                        int totalBytesWritten = 0;
                        R.log("Starting file sending . . .");
                        while ((bytesWritten = bis.read(buffer, 0, buffer.length)) > 0) {
                            outputStream.write(buffer, 0, bytesWritten);
                            //outputStream.flush();
                            totalBytesWritten += bytesWritten;
                        }
                        R.log("Completed file sending . . .");

                        outputStream.flush();
                        R.log("Flushed outputstream . . .");
                        // Until here ok
                        //int ok = ois.readInt();
                        //R.log("Read incoming int . . . ok = " + ok);

                        closeStreams();
                        R.log("Closed all streams");
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileDownloadHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeStreams() throws IOException {

        bis.close();
        fis.close();
        ois.close();
        oos.close();
        inputStream.close();
        outputStream.close();
        sslsocket.close();

    }
}
