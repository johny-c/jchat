package client.pojos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.SwingWorker;

public class FileUnzipper extends SwingWorker<Integer, Integer> {

    private String zipFilePath;
    private String outputDirPath;

    public FileUnzipper(String zipFilePath, String outputDirPath) {
        this.zipFilePath = zipFilePath;
        this.outputDirPath = outputDirPath;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        byte[] buffer = new byte[1024];

        try {

            //create output directory is not exists
            File folder = new File(outputDirPath);
            if (!folder.exists()) {
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(zipFilePath));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(outputDirPath + File.separator + fileName);

                R.log("file unzip : " + newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void done() {
        R.log("Done");
    }
}
