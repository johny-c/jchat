/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.db.util.DB;
import client.networking.NetworkManager;
import client.networking.R;
import common.db.entity.AddContactRequest;
import common.db.entity.ChatMessage;
import common.db.entity.FileTransfer;
import common.db.entity.Notification;
import common.utils.Conventions;
import common.utils.Message;
import common.utils.MessageType;
import common.utils.Utils;
import java.awt.Color;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JFileChooser;

/**
 *
 * @author johny
 */
public class NotificationCell extends javax.swing.JPanel implements Conventions {

    private NewsPane newsPane;

    /**
     * Creates new form NotificationCell
     *
     * @param np
     */
    public NotificationCell(NewsPane np) {
        newsPane = np;
        initComponents();

    }

    NotificationCell(final Notification notif, NewsPane np) {
        this(np);

        String title = "", body = "";
        switch (notif.getType()) {
            case ACR_RECEIVED:
                title = "New Contact Request";
                body = notif.getRelatedUsername() == null ? "An anonymous user" : notif.getRelatedUsername();
                body += " wants to add you as a contact!";

                acceptButton = new JButton("Accept");
                acceptButton.setBackground(COLOR_PASTEL_GREEN);
                acceptButton.setForeground(Color.WHITE);
                acceptButton.setFocusPainted(false);
                //acceptButton
                acceptButton.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        if (GuiUtils.showOnlineActionOnly(R.getMf())) {
                            return;
                        }
                        buttonsPane.setEnabled(false);
                        notif.setStatus(Notification.Status.HANDLED);
                        DB.update(notif);

                        AddContactRequest acr = (AddContactRequest) DB.get(notif.getEventId(), AddContactRequest.class);
                        acr.setStatus(AddContactRequest.Status.REPLIED);
                        acr.setReply(true);
                        acr.setTimeReplied(new Date());
                        DB.update(acr);

                        Message m = new Message(MessageType.ACR_DECISION, acr);
                        NetworkManager.send(m);
                        removeSelf();
                    }

                });
                buttonsPane.add(acceptButton);

                declineButton = new JButton("Decline");
                declineButton.setBackground(COLOR_CRIMSON_RED);
                declineButton.setForeground(Color.WHITE);
                declineButton.setFocusPainted(false);
                declineButton.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        if (GuiUtils.showOnlineActionOnly(R.getMf())) {
                            return;
                        }
                        buttonsPane.setEnabled(false);
                        notif.setStatus(Notification.Status.HANDLED);
                        DB.update(notif);

                        AddContactRequest acr = (AddContactRequest) DB.get(notif.getEventId(), AddContactRequest.class);
                        acr.setStatus(AddContactRequest.Status.REPLIED);
                        acr.setReply(false);
                        acr.setTimeReplied(new Date());
                        DB.update(acr);

                        Message m = new Message(MessageType.ACR_DECISION, acr);
                        NetworkManager.send(m);
                        removeSelf();
                    }

                });

                buttonsPane.add(declineButton);

                reviewLaterButton = new JButton("Review later");
                reviewLaterButton.setBackground(COLOR_PASTEL_BLUE);
                reviewLaterButton.setForeground(Color.WHITE);
                reviewLaterButton.setFocusPainted(false);
                reviewLaterButton.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        buttonsPane.setEnabled(false);
                        notif.setStatus(Notification.Status.READ);
                        DB.update(notif);

                        AddContactRequest acr = (AddContactRequest) DB.get(notif.getEventId(), AddContactRequest.class);
                        acr.setStatus(AddContactRequest.Status.DELIVERED);
                        DB.update(acr);

                        //R.getMf().putInHistory(notif);
                        //removeSelf();
                    }

                });

                buttonsPane.add(reviewLaterButton);
                break;

            case ACR_DECISION_REPORT:
                AddContactRequest acr = (AddContactRequest) DB.get(notif.getEventId(), AddContactRequest.class);
                title = "Request replied";
                body = notif.getRelatedUsername();
                body += acr.getReply() ? " accepted " : " rejected ";
                body += " your request";
                body += acr.getReply() ? " ! You are now connected." : ".";
                break;

            case MISSED_CHAT:
                title = "Missed chat";
                body = " . . . with " + notif.getRelatedUsername();

                viewMissedChatButton = new JButton("View missed messages");
                viewMissedChatButton.setBackground(COLOR_PASTEL_BLUE);
                viewMissedChatButton.setForeground(Color.WHITE);
                viewMissedChatButton.setFocusPainted(false);
                viewMissedChatButton.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        buttonsPane.setEnabled(false);
                        notif.setStatus(Notification.Status.HANDLED);
                        DB.update(notif);

                        Integer convId = notif.getEventId();
                        List<ChatMessage> cmList = DB.getUnreadChatMessages(convId);
                        String s = Utils.formatCMList(cmList);
                        GuiUtils.display(s, textPane);
                    }
                });

                buttonsPane.add(viewMissedChatButton);
                break;

            case MISSED_CALL:
                title = "Missed call";
                body = " . . . from " + notif.getRelatedUsername();
                break;

            case FILE_RECEIVED:
                title = "File transfer";
                body = "You 've got a file from " + notif.getRelatedUsername();

                downloadFileButton = new JButton("Download file");
                downloadFileButton.setBackground(COLOR_LIGHT_BLACK);
                downloadFileButton.setForeground(Color.WHITE);
                downloadFileButton.setFocusPainted(false);
                downloadFileButton.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        if (GuiUtils.showOnlineActionOnly(R.getMf())) {
                            return;
                        }

                        Preferences userPrefs = R.getUserAccount().getPrefs();
                        FileTransfer ft = (FileTransfer) DB.get(notif.getEventId(), FileTransfer.class);
                        String saveFolder;
                        boolean askSafePlace = userPrefs.getBoolean(ASK_DOWNLOAD_FOLDER, DEFAULT_ASK_DOWNLOAD_FOLDER);
                        if (askSafePlace) {
                            int returnVal = saveFolderChooser.showOpenDialog(R.getMf());

                            if (returnVal == JFileChooser.APPROVE_OPTION) {
                                saveFolder = saveFolderChooser.getSelectedFile().getAbsolutePath();
                            } else {
                                return;
                            }
                        } else {
                            saveFolder = userPrefs.get(DOWNLOAD_FOLDER, DEFAULT_DOWNLOAD_FOLDER);
                        }

                        downloadFileButton.setEnabled(false);
                        FileDownloadFrame fdf = new FileDownloadFrame(ft, saveFolder);
                        fdf.setVisible(true);
                        notif.setStatus(Notification.Status.HANDLED);
                    }

                });

                buttonsPane.add(downloadFileButton);
                break;

            case FILE_DOWNLOAD_REPORT:
                title = "File download report";
                body = notif.getRelatedUsername();
                body += " downloaded the files you sent.";
                break;
        }

        titleLabel.setText(title);
        textPane.setText(body);
        setToolTipText(notif.getTimeStamp().toString());

        closeButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notif.setStatus(Notification.Status.READ);
                //R.getMf().putInHistory(notif);
                removeSelf();
            }

        });

    }

    private void removeSelf() {
        // Ancestors: JPanel, ViewPort, ScrollPane, NewsPane
        //Object o = getParent().getParent().getParent().getParent();

        //System.out.println("p2: " + o.getClass().getSimpleName());
        //NewsPane np = (NewsPane) o;
        //np.removeCell(NotificationCell.this);
        newsPane.removeCell(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        saveFolderChooser = new javax.swing.JFileChooser();
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
        closeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/notification/close_button/close24.png"))); // NOI18N
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setName("closeButton"); // NOI18N
        closeButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/notification/close_button/close_red_in24.png"))); // NOI18N
        closeButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/notification/close_button/close_red_in24.png"))); // NOI18N
        closeButton.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/notification/close_button/close_red_in24.png"))); // NOI18N

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
    private javax.swing.JFileChooser saveFolderChooser;
    private javax.swing.JTextPane textPane;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
    private JButton acceptButton;
    private JButton declineButton;
    private JButton viewMissedChatButton;
    private JButton downloadFileButton;
    private JButton reviewLaterButton;

}
