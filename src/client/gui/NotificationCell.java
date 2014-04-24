/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.pojos.R;
import client.pojos.FileDownloader;
import common.db.entity.AddContactRequest;
import common.db.entity.Notification;
import common.pojos.Conventions;
import common.pojos.Message;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 *
 * @author johny
 */
public class NotificationCell extends javax.swing.JPanel implements Conventions, PropertyChangeListener {

    /**
     * Creates new form NotificationCell
     */
    public NotificationCell() {
        initComponents();

    }

    NotificationCell(final Notification notif) {
        this();

        String title = "", body = "";
        switch (notif.getType()) {
            case ACRR:
                title = "New Add Contact Request";
                body = notif.getRelatedUsername();
                body += " wants to add you as a contact!";

                acceptButton = new JButton("Accept");
                acceptButton.setBackground(COLOR_PASTEL_GREEN);
                acceptButton.setForeground(Color.WHITE);
                acceptButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        notif.setStatus(Notification.Status.HANDLED);
                        R.getDb().updateNotificationStatus(notif);
                        AddContactRequest acr = R.getDb().findACRByNotificationId(notif.getId());
                        acr.setStatus(AddContactRequest.Status.REPLIED);
                        acr.setReply(true);
                        Message m = new Message(ACR_DECISION, acr);
                        R.getNm().send(m);
                        removeSelf();
                    }
                });
                buttonsPane.add(acceptButton);

                declineButton = new JButton("Decline");
                declineButton.setBackground(COLOR_CRIMSON_RED);
                declineButton.setForeground(Color.WHITE);
                declineButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        notif.setStatus(Notification.Status.HANDLED);
                        R.getDb().updateNotificationStatus(notif);
                        Message m = new Message(ACR_DECISION);
                        AddContactRequest acr = R.getDb().findACRByNotificationId(notif.getId());
                        acr.setStatus(AddContactRequest.Status.REPLIED);
                        acr.setReply(false);
                        m.setContent(acr);
                        R.getNm().send(m);
                        removeSelf();
                    }
                });

                buttonsPane.add(declineButton);


                reviewLaterButton = new JButton("Review later");
                reviewLaterButton.setBackground(COLOR_PASTEL_BLUE);
                reviewLaterButton.setForeground(Color.WHITE);

                reviewLaterButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        notif.setStatus(Notification.Status.READ);
                        R.getDb().updateNotificationStatus(notif);
                    }
                });

                buttonsPane.add(reviewLaterButton);

                break;
            case ACRA:
                title = "Request accepted";
                body = notif.getRelatedUsername();
                body += " accepted your add contact request! You are now connected.";
                break;
            case ACRDR:
                title = "Delivery Report";
                body = notif.getRelatedUsername();
                body += " got your add contact request.";
                break;
            case MISSED_CHAT:
                title = "Missed chat";
                body = " . . . with " + notif.getRelatedUsername();

                viewMissedChatButton = new JButton("View missed messages");
                viewMissedChatButton.setBackground(COLOR_PASTEL_BLUE);
                viewMissedChatButton.setForeground(Color.WHITE);
                viewMissedChatButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        notif.setStatus(Notification.Status.HANDLED);
                        R.getDb().updateNotificationStatus(notif);
                        Message m = new Message(MISSED_CHAT_REQUEST);
                        Integer convId = R.getDb().findConversationIdByNotificationId(notif.getId());
                        if (convId == null) {
                            m.setContent(convId);
                            R.getNm().send(m);
                        }
                    }
                });

                buttonsPane.add(viewMissedChatButton);

                break;
            case MISSED_CALL:
                title = "Missed call";
                body = " . . . from " + notif.getRelatedUsername();
                break;
            case FILE_SENT:
                title = "File transfer";
                body = "You 've got a file from " + notif.getRelatedUsername();


                downloadFileProgressBar = new JProgressBar();
                add(downloadFileProgressBar);

                downloadFileButton = new JButton("Download file");
                downloadFileButton.setBackground(COLOR_LIGHT_BLACK);
                downloadFileButton.setForeground(Color.WHITE);
                downloadFileButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        notif.setStatus(Notification.Status.HANDLED);
                        downloadFile(notif.getEventId());
                        //HibernateUtil.updateNotificationStatus(notif);
                        //Message m = new Message(MISSED_FILE_REQUEST);
                        //Integer fileId = HibernateUtil.findFileIdByNotificationId(notif.getId());
                        //if (fileId == null) {
                        //   m.setContent(fileId);
                        //   R.getNm().send(m);
                        //}
                    }

                    private void downloadFile(Integer fileId) {
                        downloadFileButton.setEnabled(false);
                        //progressBar = new JProgressBar(0, 100);
                        downloadFileProgressBar.setValue(0);
                        downloadFileProgressBar.setStringPainted(true);
                        FileDownloader task = new FileDownloader(fileId, NotificationCell.this);
                        task.addPropertyChangeListener(NotificationCell.this);
                        task.execute();
                    }
                });

                buttonsPane.add(downloadFileButton);
                break;
        }


        titleLabel.setText(title);
        textPane.setText(body);
        setToolTipText(notif.getTimeStamp().toString());

        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notif.setStatus(Notification.Status.READ);
                //HibernateUtil.updateNotificationStatus(notif);
                removeSelf();
            }
        });



    }

    private void removeSelf() {
        // Ancestors: JPanel, ViewPort, ScrollPane, NewsPane
        Object o = getParent().getParent().getParent().getParent();
        //System.out.println("p2: " + o.getClass().getSimpleName());
        NewsPane np = (NewsPane) o;
        np.removeCell(NotificationCell.this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();
        closeButton = new javax.swing.JButton();
        buttonsPane = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();

        setBackground(java.awt.Color.white);
        setMaximumSize(new java.awt.Dimension(350, 150));
        setMinimumSize(new java.awt.Dimension(350, 150));

        titleLabel.setText("title");

        textPane.setEditable(false);
        textPane.setDisabledTextColor(java.awt.Color.darkGray);
        jScrollPane1.setViewportView(textPane);

        closeButton.setBackground(new Color(0,0,0,0));
        closeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/notification/close_button/close24.png"))); // NOI18N
        closeButton.setBorderPainted(false);
        closeButton.setName("closeButton"); // NOI18N
        closeButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/notification/close_button/close_red_in24.png"))); // NOI18N
        closeButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/notification/close_button/close_red_in24.png"))); // NOI18N

        buttonsPane.setBackground(getBackground());
        buttonsPane.setOpaque(false);

        jSeparator1.setBackground(jSeparator1.getForeground());
        jSeparator1.setForeground(java.awt.Color.blue);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                            .addComponent(buttonsPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(32, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(titleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(titleLabel)
                    .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonsPane, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPane;
    private javax.swing.JButton closeButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextPane textPane;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
    private JButton acceptButton;
    private JButton declineButton;
    private JButton viewMissedChatButton;
    private JButton downloadFileButton;
    private JButton reviewLaterButton;
    private JProgressBar downloadFileProgressBar;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            downloadFileProgressBar.setValue(progress);
        }

        if ("state" == evt.getPropertyName()) {
            if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
                downloadFileProgressBar.setVisible(false);
                //downloadFileButton.setEnabled(true);
                //R.getNm().send(new Message(FILE_DOWNLOAD_COMPLETED, ftToDo));
            }
        }
    }
}
