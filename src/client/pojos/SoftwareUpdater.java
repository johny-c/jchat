package client.pojos;

import common.db.entity.FileTransfer;
import common.pojos.Conventions;
import java.awt.Component;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

public class SoftwareUpdater extends SwingWorker<Integer, Integer> implements Conventions {

    private FileTransfer fileTransfer;
    private Component gui;
    private FileDownloader softwareDownloader;
    private FileUnzipper unzipper;
    private String newVersionFileName;

    public SoftwareUpdater(JFrame softwareUpdateFrame) {

        gui = softwareUpdateFrame;

        R.log("Client: SoftwareUpdater created");
    }

    @Override
    protected Integer doInBackground() throws Exception {

        // Download new version

        // Unzip new version

        // Copy old version's DB and Preferences to new version's folder

        // 
        String newAppDir = UserSettings.get(UserSettings.DOWNLOAD_FOLDER, "");
        String appDir = R.getAppDir();
        unzipper = new FileUnzipper(newAppDir + File.separator + newVersionFileName, appDir);
        unzipper.execute();




        return null;
    }

    @Override
    public void done() {
    }
}
