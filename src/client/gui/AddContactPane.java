/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.db.util.DB;
import client.networking.NetworkManager;
import client.networking.R;
import common.db.entity.AddContactRequest;
import common.db.entity.UserAccount;
import common.utils.Conventions;
import common.utils.Message;
import common.utils.MessageType;
import common.utils.OQueue;
import common.utils.Utils;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author johny
 */
public class AddContactPane extends javax.swing.JPanel implements Observer, Conventions {

    /**
     * Creates new form AddContactPane
     */
    public AddContactPane() {
        this.columnNames = new String[]{"Username", "Email", "Age"};
        this.tempACRs = new ArrayList<>();
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        newsPaneLabel = new javax.swing.JLabel();
        searchUserField = new javax.swing.JFormattedTextField();
        searchUserButton = new javax.swing.JButton();
        resultsPane = new javax.swing.JPanel();
        resultsPane.setVisible(false);
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        usersFoundTable = new javax.swing.JTable();
        addContactButton = new javax.swing.JButton();
        resultsCounterLabel = new javax.swing.JLabel();
        addContactLAL = new javax.swing.JLabel();
        addContactLAL.setVisible(false);
        addContactSuccessLabel = new javax.swing.JLabel();
        addContactSuccessLabel.setVisible(false);
        invalidInputLabel = new javax.swing.JLabel();
        invalidInputLabel.setVisible(false);
        searchForUserLAL = new javax.swing.JLabel();
        searchForUserLAL.setVisible(false);

        setBackground(COLOR_PAPAYA);
        setName("ADD_CONTACT_PANE"); // NOI18N

        newsPaneLabel.setBackground(new java.awt.Color(60, 59, 55));
        newsPaneLabel.setFont(new java.awt.Font("Serif", 1, 15)); // NOI18N
        newsPaneLabel.setForeground(java.awt.Color.white);
        newsPaneLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        newsPaneLabel.setText("Add a contact");
        newsPaneLabel.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        newsPaneLabel.setOpaque(true);

        searchUserField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchUserFieldKeyReleased(evt);
            }
        });

        searchUserButton.setBackground(COLOR_PASTEL_BLUE);
        searchUserButton.setForeground(java.awt.Color.white);
        searchUserButton.setText("Search for user");
        searchUserButton.setBorderPainted(false);
        searchUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchUserButtonActionPerformed(evt);
            }
        });

        resultsPane.setBackground(java.awt.Color.white);
        resultsPane.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("Liberation Sans", 3, 15)); // NOI18N
        jLabel2.setForeground(java.awt.Color.blue);
        jLabel2.setText("Users found :");

        usersFoundTable.setBackground(resultsPane.getBackground());
        model = new DefaultTableModel(columnNames, 0);
        usersFoundTable.setModel(model);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
        for(int i=0; i<usersFoundTable.getColumnCount(); i++)
        usersFoundTable.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
        usersFoundTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        usersFoundTable.setOpaque(false);
        usersFoundTable.setRowHeight(24);
        usersFoundTable.setSelectionBackground(new java.awt.Color(241, 119, 69));
        usersFoundTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        usersFoundTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(usersFoundTable);

        addContactButton.setBackground(COLOR_PASTEL_BLUE);
        addContactButton.setForeground(java.awt.Color.white);
        addContactButton.setText("Add to my contacts");
        addContactButton.setBorderPainted(false);
        addContactButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addContactButtonActionPerformed(evt);
            }
        });

        resultsCounterLabel.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        resultsCounterLabel.setForeground(new java.awt.Color(230, 151, 13));

        addContactLAL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/loading_circle_animation/21.gif"))); // NOI18N

        addContactSuccessLabel.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        addContactSuccessLabel.setForeground(new java.awt.Color(66, 185, 66));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("client/gui/Bundle"); // NOI18N
        addContactSuccessLabel.setText(bundle.getString("addContactSuccessLabel")); // NOI18N

        javax.swing.GroupLayout resultsPaneLayout = new javax.swing.GroupLayout(resultsPane);
        resultsPane.setLayout(resultsPaneLayout);
        resultsPaneLayout.setHorizontalGroup(
            resultsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, resultsPaneLayout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addGroup(resultsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addContactSuccessLabel)
                    .addGroup(resultsPaneLayout.createSequentialGroup()
                        .addComponent(addContactButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addContactLAL)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(resultsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(resultsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(resultsPaneLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resultsCounterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        resultsPaneLayout.setVerticalGroup(
            resultsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(resultsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(resultsCounterLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addGap(17, 17, 17)
                .addGroup(resultsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addContactButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addContactLAL, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(addContactSuccessLabel)
                .addContainerGap())
        );

        invalidInputLabel.setForeground(java.awt.Color.red);
        invalidInputLabel.setText("Not a username or email");

        searchForUserLAL.setIcon(addContactLAL.getIcon());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(newsPaneLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(120, 120, 120)
                                .addComponent(invalidInputLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(searchUserField, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchUserButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(searchForUserLAL)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(resultsPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(newsPaneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchUserField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchUserButton))
                .addGap(3, 3, 3)
                .addComponent(invalidInputLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchForUserLAL)
                .addGap(4, 4, 4)
                .addComponent(resultsPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchUserButtonActionPerformed
        searchForUser();
    }//GEN-LAST:event_searchUserButtonActionPerformed

    private void addContactButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addContactButtonActionPerformed
        int i = usersFoundTable.getSelectedRow();
        if (i != -1) {
            UserAccount u = responseUsers.get(i);
            if (u.getId().equals(R.getUserAccount().getId())) {
                JOptionPane.showMessageDialog(this,
                        "You cannot add yourself as a contact \n"
                        + "Terrible things would happen ...\n\n"
                        + "(See 'Inception'...)",
                        "Not a good idea",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            AddContactRequest acr = DB.getACRsent(u.getId());
            if (acr != null) {
                JOptionPane.showMessageDialog(this,
                        "A request has already been sent to " + acr.getRecipientName(),
                        "Request sent",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            R.log("Selected user has id= " + u.getId());
            R.log("My id is " + R.getUserAccount().getId());
            List<UserAccount> myContacts = DB.getContacts(R.getUserAccount().getId());
            if (myContacts == null) {
                R.log("My Contacts List is null...");
            }
            R.log("I have " + myContacts.size() + " contacts!");
            for (UserAccount uac : myContacts) {
                R.log("My contact has id= " + uac.getId());
                if (uac.getId().equals(u.getId())) {
                    JOptionPane.showMessageDialog(this,
                            uac.getUsername() + " is already in your contacts ",
                            "Already a contact",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            addContactLAL.setVisible(true);
            acr = new AddContactRequest();
            UserAccount USER = R.getUserAccount();
            acr.setQuesterUserId(USER.getId());
            acr.setQuesterName(USER.getUsername());
            acr.setBody(""); // set all fields null except ids, convId         
            acr.setRecipientUserId(u.getId());
            acr.setRecipientName(u.getUsername());
            acr.setStatus(AddContactRequest.Status.BY_SOURCE);
            acr.setTimeSent(new Date());
            acr.setClientGenId(R.getRandom().nextInt());
            tempACRs.add(acr);
            Message m = new Message(MessageType.ADD_CONTACT_REQUEST, acr);
            NetworkManager.send(m);
        }
    }//GEN-LAST:event_addContactButtonActionPerformed

    private void searchUserFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchUserFieldKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchForUser();
        }
    }//GEN-LAST:event_searchUserFieldKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addContactButton;
    private javax.swing.JLabel addContactLAL;
    private javax.swing.JLabel addContactSuccessLabel;
    private javax.swing.JLabel invalidInputLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel newsPaneLabel;
    private javax.swing.JLabel resultsCounterLabel;
    private javax.swing.JPanel resultsPane;
    private javax.swing.JLabel searchForUserLAL;
    private javax.swing.JButton searchUserButton;
    private javax.swing.JFormattedTextField searchUserField;
    private javax.swing.JTable usersFoundTable;
    // End of variables declaration//GEN-END:variables
    private DefaultTableModel model;
    private final String[] columnNames;
    private String[] row;
    private List<UserAccount> responseUsers;
    private final List<AddContactRequest> tempACRs;
    private String previousUserQuery;

    private void searchForUser() {
        String userInput = searchUserField.getText();
        if (userInput.isEmpty() || userInput.equals(previousUserQuery)) {
            return;
        }

        if (GuiUtils.showOnlineActionOnly(R.getMf())) {
            return;
        }
        boolean inputIsValid = Utils.isValidUsername(userInput) || Utils.isValidEmailAddress(userInput);

        if (inputIsValid) {
            previousUserQuery = userInput;
            searchForUserLAL.setVisible(true);
            invalidInputLabel.setVisible(false);
            addContactSuccessLabel.setVisible(false);
            searchUserButton.setEnabled(false);

            Message searchUserRequest = new Message(MessageType.SEARCH_FOR_USER_REQUEST, userInput);
            NetworkManager.send(searchUserRequest);
        } else {
            invalidInputLabel.setVisible(true);
        }
    }

    private void populateResults(List<UserAccount> responseUsers) {
        int rows = model.getRowCount();
        R.log("Model has " + rows + " rows.");

        // Delete rows from the end to the start
        for (int i = rows - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        for (UserAccount u : responseUsers) {
            row = new String[3];
            row[0] = u.getUsername();
            row[1] = u.getEmail();
            row[2] = (u.getBirthDate() == null) ? "" : Utils.birthdateToAge(u.getBirthDate()) + "";
            model.addRow(row);
            R.log("Adding a new row ");
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!arg.equals(this.getClass().getSimpleName())) {
            if (arg.equals(MessageType.NO_CONNECTION_BROADCAST)) {
                GuiUtils.stopAnimations(arg, addContactLAL, searchForUserLAL);
            }
            return;
        }

        OQueue q = (OQueue) o;
        final Message m = (Message) q.poll();
        AddContactRequest acrIn;

        switch (m.getType()) {
            case SEARCH_FOR_USER_RESPONSE:
                responseUsers = (List<UserAccount>) m.getContent();
                if (responseUsers != null) {
                    R.log("Received a list of users with size = " + responseUsers.size());
                    populateResults(responseUsers);
                }
                break;

            case ADD_CONTACT_REQ_ACK:
                // SLA
                acrIn = (AddContactRequest) m.getContent();
                // Find message locally by client generated cmsg_id
                for (AddContactRequest tempACR : tempACRs) {
                    if (Objects.equals(tempACR.getClientGenId(), acrIn.getClientGenId())) {
                        tempACR.setServerGenId(acrIn.getServerGenId());
                        DB.insert(acrIn);
                        tempACRs.remove(tempACR);
                        break;
                    }
                }
                break;
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                switch (m.getType()) {
                    case SEARCH_FOR_USER_RESPONSE:
                        searchForUserLAL.setVisible(false);
                        resultsPane.setVisible(true);
                        resultsCounterLabel.setText(responseUsers == null ? "0" : responseUsers.size() + "");
                        searchUserButton.setEnabled(true);
                        // Populate usersFoundTable
                        break;

                    case ADD_CONTACT_REQ_ACK:
                        addContactLAL.setVisible(false);
                        addContactSuccessLabel.setVisible(true);
                        break;
                }
            }
        });

    }

}