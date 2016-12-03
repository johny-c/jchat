/*
 * Copyright (C) 2014 johny
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package client.gui;

import client.tasks.FileUploader;
import common.db.entity.FileTransfer;
import common.utils.Conventions;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.SwingWorker;

/**
 *
 * @author johny
 */
public class FileUploadFrame extends javax.swing.JFrame implements Conventions, PropertyChangeListener {

    private FileTransfer fileUpload;
    private File[] filesToUpload;
    private FileUploader task;
    private JButton button;

    /**
     * Creates new form FileUploadFrame
     */
    public FileUploadFrame() {
        initComponents();
    }

    FileUploadFrame(FileTransfer pendingFileUpload, File[] pendingFilesToUpload, JButton sendFilesButton) {
        this();
        fileUpload = pendingFileUpload;
        filesToUpload = pendingFilesToUpload;
        button = sendFilesButton;
        task = new FileUploader(fileUpload, filesToUpload, fileUploadProgressLabel, fileUploadProgressArea);
        task.addPropertyChangeListener(FileUploadFrame.this);
        task.execute();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileUploadProgressBar = new javax.swing.JProgressBar();
        fileUploadProgressLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        fileUploadProgressArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(GuiUtils.getCentralRectangle());
        setMinimumSize(new java.awt.Dimension(420, 220));

        fileUploadProgressBar.setStringPainted(true);

        fileUploadProgressLabel.setText("Upload is about to start..");

        fileUploadProgressArea.setEditable(false);
        fileUploadProgressArea.setColumns(20);
        fileUploadProgressArea.setRows(5);
        jScrollPane2.setViewportView(fileUploadProgressArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileUploadProgressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileUploadProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(fileUploadProgressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(fileUploadProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea fileUploadProgressArea;
    private javax.swing.JProgressBar fileUploadProgressBar;
    private javax.swing.JLabel fileUploadProgressLabel;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if ("progress".equals(evt.getPropertyName())) {
            int progress = (Integer) evt.getNewValue();
            fileUploadProgressBar.setValue(progress);
        }

        if ("state".equals(evt.getPropertyName())) {
            if (evt.getNewValue().equals(SwingWorker.StateValue.DONE)) {
                new SwingWorker() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Thread.sleep(3000);
                        return null;
                    }

                    @Override
                    protected void done() {
                        fileUpload = null;
                        button.setEnabled(true);
                        if (isVisible()) {
                            setVisible(false);
                        }
                    }

                }.execute();

            }
        }
    }
}