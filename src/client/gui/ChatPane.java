/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.db.util.DB;
import client.networking.NetworkManager;
import client.networking.R;
import common.db.entity.ChatMessage;
import common.db.entity.Conversation;
import common.db.entity.ConversationParticipant;
import common.db.entity.FileTransfer;
import common.db.entity.UserAccount;
import common.utils.Conventions;
import common.utils.Message;
import common.utils.MessageType;
import common.utils.OQueue;
import common.utils.Utils;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.prefs.Preferences;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author johny
 */
public class ChatPane extends javax.swing.JPanel implements Conventions, Observer {

    /**
     * Creates new form ChatPane
     */
    ChatPane(Integer convId) {
        userPrefs = R.getUserAccount().getPrefs();
        initComponents();
        conversation = (Conversation) DB.get(convId, Conversation.class);
        tempChatMessages = new ArrayList();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        uploadFileChooser = new javax.swing.JFileChooser();
        saveFolderChooser = new javax.swing.JFileChooser();
        jScrollPane3 = new javax.swing.JScrollPane();
        historyPane = new javax.swing.JTextPane();
        buttonsPane = new javax.swing.JPanel();
        colorChooserButton = new javax.swing.JButton();
        inviteButton = new javax.swing.JButton();
        sendFilesButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        inputPane = new javax.swing.JTextPane();

        uploadFileChooser.setMultiSelectionEnabled(true);

        saveFolderChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setBackground(new java.awt.Color(255, 255, 255));

        historyPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane3.setViewportView(historyPane);

        buttonsPane.setBackground(new java.awt.Color(242, 216, 189));
        buttonsPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        colorChooserButton.setBackground(new java.awt.Color(238, 111, 18));
        colorChooserButton.setForeground(java.awt.Color.white);
        colorChooserButton.setText("Change text color");
        colorChooserButton.setBorderPainted(false);
        colorChooserButton.setFocusPainted(false);
        colorChooserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorChooserButtonActionPerformed(evt);
            }
        });

        inviteButton.setBackground(new java.awt.Color(207, 101, 101));
        inviteButton.setForeground(java.awt.Color.white);
        inviteButton.setText("Invite others");
        inviteButton.setToolTipText("Coming soon...");
        inviteButton.setBorderPainted(false);
        inviteButton.setEnabled(false);
        inviteButton.setFocusPainted(false);
        inviteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inviteButtonActionPerformed(evt);
            }
        });

        sendFilesButton.setBackground(java.awt.Color.darkGray);
        sendFilesButton.setForeground(java.awt.Color.white);
        sendFilesButton.setText("Send files");
        sendFilesButton.setBorderPainted(false);
        sendFilesButton.setFocusPainted(false);
        sendFilesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendFilesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonsPaneLayout = new javax.swing.GroupLayout(buttonsPane);
        buttonsPane.setLayout(buttonsPaneLayout);
        buttonsPaneLayout.setHorizontalGroup(
            buttonsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPaneLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(colorChooserButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(inviteButton)
                .addGap(18, 18, 18)
                .addComponent(sendFilesButton)
                .addGap(33, 33, 33))
        );
        buttonsPaneLayout.setVerticalGroup(
            buttonsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sendFilesButton)
                    .addComponent(inviteButton)
                    .addComponent(colorChooserButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        inputPane.setForeground(new Color(userPrefs.getInt(TEXT_COLOR, Color.BLACK.getRGB())));
        inputPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputPaneKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(inputPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addComponent(buttonsPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(buttonsPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void colorChooserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorChooserButtonActionPerformed
        int savedColor = userPrefs.getInt(TEXT_COLOR, Color.BLACK.getRGB());

        Color newColor = JColorChooser.showDialog(
                ChatPane.this,
                "Choose how your text is colored",
                new Color(savedColor));

        if (newColor != null) {
            userPrefs.putInt(TEXT_COLOR, newColor.getRGB());
            inputPane.setForeground(newColor);
        }
    }//GEN-LAST:event_colorChooserButtonActionPerformed

    private void inviteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inviteButtonActionPerformed
        R.getMf().openInvitationPane();
    }//GEN-LAST:event_inviteButtonActionPerformed

    private void sendFilesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendFilesButtonActionPerformed
        if (pendingFileUpload != null) {
            GuiUtils.showFileUploadInProgress(this);
            return;
        }

        int returnVal = uploadFileChooser.showOpenDialog(ChatPane.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            File[] filesToSend = uploadFileChooser.getSelectedFiles();

            long size = 0;
            for (File f : filesToSend) {
                size += f.length();
            }
            if (size > MAX_FILE_TRANSFER_SIZE) {
                GuiUtils.showFileSizeTooLarge(ChatPane.this, size);

                return;
            }

            List<ConversationParticipant> participants = DB.getConvParticipants(conversation.getServerGenId());
            int participantsCounter = participants.size();
            if (participantsCounter > 2) {
                GuiUtils.showTooManyConvParticipants(ChatPane.this, participantsCounter);
                return;
            }

            FileTransfer ft = new FileTransfer();
            ft.setFilesCount(filesToSend.length);
            ft.setFileSize(size);
            ft.setSourceUserId(R.getUserAccount().getId());
            ft.setSourceName(R.getUserAccount().getUsername());
            if (participants.get(0).getParticipantId().equals(R.getUserAccount().getId())) {
                ft.setTargetUserId(participants.get(1).getParticipantId());
            } else {
                ft.setTargetUserId(participants.get(0).getParticipantId());
            }
            ft.setConversationId(conversation.getServerGenId());
            ft.setTimeSent(new Date());
            ft.setStatus(FileTransfer.Status.BY_SOURCE);
            pendingFileUpload = ft;
            pendingFilesToUpload = filesToSend;
            Message m = new Message(MessageType.FILE_SEND_REQ, ft);
            NetworkManager.send(m);
        }
    }//GEN-LAST:event_sendFilesButtonActionPerformed

    private ChatMessage prepareChatMessageOut(String text) {
        ChatMessage cm = new ChatMessage();
        cm.setClientGenId(R.getRandom().nextInt());
        cm.setBody(text);
        cm.setSourceUserId(R.getUserAccount().getId());
        cm.setTimeSent(new Date());
        cm.setStatus(ChatMessage.Status.BY_SOURCE);
        cm.setConversationId(conversation.getServerGenId());
        cm.setColor(userPrefs.getInt(TEXT_COLOR, DEFAULT_TEXT_COLOR));
        return cm;
    }

    private void inputPaneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputPaneKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String textInput = inputPane.getText();
            if (!Utils.isValidChatMessage(textInput)) {
                JOptionPane.showMessageDialog(ChatPane.this,
                        "Your message cannot be sent, maybe it's too large.",
                        "Message sending aborted", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (textInput.endsWith("\n")) {
                textInput = textInput.substring(0, textInput.length() - 1);
            }

            if (textInput.trim().isEmpty()) {
                return;
            }

            ChatMessage cm = prepareChatMessageOut(textInput);
            tempChatMessages.add(cm);
            Message m = new Message(MessageType.CHAT_MSG_SEND_REQ, cm);
            NetworkManager.send(m);
            inputPane.setText("");
        }

    }//GEN-LAST:event_inputPaneKeyReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPane;
    private javax.swing.JButton colorChooserButton;
    private javax.swing.JTextPane historyPane;
    private javax.swing.JTextPane inputPane;
    private javax.swing.JButton inviteButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JFileChooser saveFolderChooser;
    private javax.swing.JButton sendFilesButton;
    private javax.swing.JFileChooser uploadFileChooser;
    // End of variables declaration//GEN-END:variables

    private final Conversation conversation;
    private final List<ChatMessage> tempChatMessages;
    private Integer lastCMSenderId;
    private FileTransfer pendingFileUpload;
    private File[] pendingFilesToUpload;
    private FileUploadFrame fuf;
    private FileDownloadFrame fdf;
    private InvitationPane invitationPane;
    private final Preferences userPrefs;

    private SimpleAttributeSet[] initAttributes(int color) {
        //Hard-code some attributes.
        SimpleAttributeSet[] attrs = new SimpleAttributeSet[3];

        attrs[0] = new SimpleAttributeSet(); // meta info - source name
        StyleConstants.setFontFamily(attrs[0], "SansSerif");
        StyleConstants.setFontSize(attrs[0], 16);
        StyleConstants.setBold(attrs[0], true);

        attrs[1] = new SimpleAttributeSet(attrs[0]); // cm body
        StyleConstants.setForeground(attrs[1], new Color(color));

        attrs[2] = new SimpleAttributeSet(attrs[0]); // meta info - date
        StyleConstants.setBold(attrs[2], false);

        return attrs;
    }

    private void displayChatMessage(final ChatMessage cm) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StyledDocument doc = historyPane.getStyledDocument();
                String[] cmParts = new String[3];

                cmParts[0] = "\n";
                if (!lastCMSenderId.equals(cm.getSourceUserId())) {
                    cmParts[0] += "\n\n";
                }
                cmParts[0] += cm.getSourceName() + ": ";
                cmParts[1] = cm.getBody() + " ";
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cm.getTimeSent());
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int mins = calendar.get(Calendar.MINUTE);
                int secs = calendar.get(Calendar.SECOND);
                int month = calendar.get(Calendar.MONTH);
                String monthString = Utils.getMonthString(month);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                cmParts[2] = "(" + hours + ":";
                cmParts[2] += (mins < 10) ? "0" + mins : mins;
                cmParts[2] += ":";
                cmParts[2] += (secs < 10) ? "0" + secs : secs;
                cmParts[2] += ", " + day + " " + monthString + ")";
                SimpleAttributeSet[] cmAttrs = initAttributes(cm.getColor());
                try {
                    for (int i = 0; i < cmParts.length; i++) {
                        doc.insertString(doc.getLength(), cmParts[i],
                                cmAttrs[i]);
                    }
                } catch (BadLocationException e) {
                    R.log(e.toString());
                }
            }
        });
    }

    private void displayReport(ChatMessage cm) {
        String s = " (MESSAGE DELIVERED)";
        putStringOnDisplay(s);
    }

    private void displayReport(FileTransfer f) {
        String s = "\n ( FILE(s) DELIVERED to ";
        s += f.getTargetName();
        s += ")\n\n";
        putStringOnDisplay(s);
    }

    private void displayParticipantLeave(ConversationParticipant cpIn) {

        UserAccount part = (UserAccount) DB.get(cpIn.getParticipantId(), UserAccount.class);
        String s = "\n\n - " + part.getUsername() + " left the conversation -\n";
        putStringOnDisplay(s);
    }

    private void displayConversationEnded() {
        String s = "\n - SEEMS LIKE THIS CONVERSATION IS OVER -\n\n";
        putStringOnDisplay(s);
    }

    private void putStringOnDisplay(final String string) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int offset = historyPane.getStyledDocument().getLength();
                try {
                    historyPane.getStyledDocument().insertString(offset, string, null);
                } catch (BadLocationException e) {
                    R.log(e.toString());
                }
            }
        });
    }

    private void uploadFiles() {
        sendFilesButton.setEnabled(false);
        fuf = new FileUploadFrame(pendingFileUpload, pendingFilesToUpload, sendFilesButton);
        fuf.setVisible(true);
    }

    private void showFileDownloadDialog(final FileTransfer ft) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String msg = ft.getSourceName() + " wants to send you "
                        + ft.getFilesCount() + " files.\n"
                        + "Download files?";

                int choice = JOptionPane.showConfirmDialog(R.getMf(),
                        msg, "File(s) for you", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    boolean askSafePlace = userPrefs.getBoolean(ASK_DOWNLOAD_FOLDER, DEFAULT_ASK_DOWNLOAD_FOLDER);
                    if (askSafePlace) {
                        int returnVal = saveFolderChooser.showOpenDialog(ChatPane.this);

                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            downloadFiles(ft, saveFolderChooser.getSelectedFile().getAbsolutePath());
                        }
                    } else {
                        downloadFiles(ft, userPrefs.get(DOWNLOAD_FOLDER, DEFAULT_DOWNLOAD_FOLDER));
                    }

                }
            }

            private void downloadFiles(FileTransfer fileTransfer, String saveFolder) {

                fdf = new FileDownloadFrame(fileTransfer, saveFolder);
                fdf.setVisible(true);
            }
        }
        );
    }

    private void deleteTempChatMessage(Integer clientGenId) {
        for (ChatMessage cm : tempChatMessages) {
            if (cm.getClientGenId().equals(clientGenId)) {
                tempChatMessages.remove(cm);
                break;
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!arg.equals(this.getClass().getSimpleName())) {
            return;
        }

        OQueue q = (OQueue) o;
        Message m = (Message) q.poll();
        if (m == null) {
            return;
        }

        FileTransfer ftIn = null;
        ChatMessage cmIn = null;
        Integer convIdIn;
        ConversationParticipant cpIn = null;

        switch (m.getType()) {
            case FILE_SEND_ACK:
            case FILE_SEND_SERVER_REJECTION:
            case FILE_DOWNLOAD_REPORT:
            case FILE_NOTIFICATION:
                ftIn = (FileTransfer) m.getContent();
                convIdIn = ftIn.getConversationId();
                break;

            case PARTICIPANT_LEFT_CONVERSATION:
                cpIn = (ConversationParticipant) m.getContent();
                convIdIn = cpIn.getConversationId();
                break;

            case CONVERSATION_END:
                convIdIn = (Integer) m.getContent();
                R.log("Conv with id= " + convIdIn
                        + " end, this tab has convid= "
                        + conversation.getServerGenId() + "\n");
                break;

            default:
                cmIn = (ChatMessage) m.getContent();
                convIdIn = cmIn.getConversationId();
                break;
        }

        // If the conversation id matches another tab ignore it
        if (!Objects.equals(convIdIn, conversation.getServerGenId())) {
            return;
        }

        switch (m.getType()) {

            case CHAT_MSG_DELIVERY:
                if (cmIn.getSourceUserId().equals(R.getUserAccount().getId())) {
                    // Sent by me
                    deleteTempChatMessage(cmIn.getClientGenId());
                }
                displayChatMessage(cmIn);
                lastCMSenderId = cmIn.getSourceUserId();
                break;

            case CHAT_MSG_DELIVERY_REPORT:
                displayReport(cmIn);
                break;

            case FILE_SEND_ACK:            // Start file upload
                pendingFileUpload.setId(ftIn.getId());
                uploadFiles();
                break;

            case FILE_SEND_SERVER_REJECTION:
                GuiUtils.showFileTransferRejectionDialog(this);
                break;

            case FILE_DOWNLOAD_REPORT:
                displayReport(ftIn);
                break;

            case FILE_NOTIFICATION:
                showFileDownloadDialog(ftIn);
                break;

            case PARTICIPANT_LEFT_CONVERSATION:
                displayParticipantLeave(cpIn);
                DB.delete(cpIn);
                break;

            case CONVERSATION_END:
                displayConversationEnded();
                inputPane.setEnabled(false);
                sendFilesButton.setEnabled(false);
                Conversation conv = (Conversation) DB.get(conversation.getServerGenId(), Conversation.class);
                conv.setEndTime(new Date());
                conv.setStatus(Conversation.Status.INACTIVE);
                DB.update(conv);
                break;
        }

    }

}
