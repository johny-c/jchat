package feature_testing.server;

import common.pojos.Conventions;
import common.pojos.Utils;
import common.db.entity.FileTransfer;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

class FileUploadHandler implements Runnable, Conventions {

    private SSLSocket sslsocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private byte[] buffer;
    private FileTransfer fileTransfer;

    FileUploadHandler(SSLSocket socket) {
        //g = gui;
        sslsocket = socket;
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
            // FIRST GET THE FILE'S INFO

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



            // IF FILE INFO IS OK, PREPARE TO RECEIVE THE FILE
            if (o != null) {
                if (o.getClass().equals(FileTransfer.class)) {
                    fileTransfer = (FileTransfer) o;
                } else {
                    R.log("Sth went down...Object is not FileTransfer");
                    closeStreams();
                    return;
                }

                if (Utils.isValidFile(fileTransfer)) {
                    // File output to local file
                    //if(ft.getSourceUserId() ) ft.getId()
                    File destinationFile = new File("JCHAT_STORAGE/Uploads/" + "UserId" + "/" + fileTransfer.getFileName());
                    R.log(destinationFile.getAbsolutePath());
                    destinationFile.getParentFile().mkdirs();
                    boolean fileCreated = destinationFile.createNewFile();
                    fileTransfer.setServerPath(destinationFile.getAbsolutePath());

                    if (fileCreated) {
                        R.log("File created successfully");
                    }

                    try {
                        fos = new FileOutputStream(destinationFile);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(FileUploadHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }


                    // Buffered output to local file
                    bos = new BufferedOutputStream(fos);
                    buffer = new byte[FILE_BUFFER_SIZE];
                    int bytesRead = 0;
                    int totalBytesRead = 0;
                    int progressCounter = 0;

                    // SEND SIGNAL THAT SERVER IS READY FOR FILE UPLOAD
                    oos.writeInt(FILE_START_UPLOAD_OK);
                    oos.flush();
                    R.log("Sent signal server is ready to receive file upload");


                    while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) > 0) {
                        bos.write(buffer, 0, bytesRead);
                        bos.flush();
                        totalBytesRead += bytesRead;
                        progressCounter += bytesRead;
                        if (progressCounter > 100000 || totalBytesRead == fileTransfer.getFileSize()) {
                            R.log("Received and wrote another 100KB, totalBytesRead = " + totalBytesRead);
                            progressCounter = 0;
                        }

                        R.log("Receiving bytes, bytesRead = " + bytesRead);

                    }
                    R.log("Exited reading loop");
                    oos.flush();

                    //R.log("Got " + totalBytesRead + " from a file of " + ft.getFileSize() + " bytes.");
                    if (totalBytesRead == fileTransfer.getFileSize()) {
                        //oos.writeInt(FILE_UPLOAD_SUCCESS);
                        //oos.flush();
                        R.log("Sent upload success message");
                    }
                    closeStreams();
                    R.log("Closed all streams");
                }
            } else {
                R.log("Sth went down... object is null");
                closeStreams();
            }
        } catch (IOException ex) {
            Logger.getLogger(FileUploadHandler.class.getName()).log(Level.SEVERE, null, ex);
            try {
                closeStreams();
            } catch (IOException ex1) {
                Logger.getLogger(FileUploadHandler.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }



    }

    private void closeStreams() throws IOException {

        bos.close();
        fos.close();
        ois.close();
        oos.close();
        inputStream.close();
        outputStream.close();
        sslsocket.close();

    }
}
