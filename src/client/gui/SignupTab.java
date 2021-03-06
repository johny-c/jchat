/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.db.util.DB;
import client.networking.NetworkManager;
import client.networking.R;
import client.tasks.ConnectionTask;
import common.db.entity.MyAccountRef;
import common.db.entity.UserAccount;
import common.utils.Conventions;
import common.utils.Message;
import common.utils.MessageType;
import common.utils.OQueue;
import common.utils.Utils;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 *
 * @author johny
 */
public class SignupTab extends javax.swing.JPanel implements Conventions, Observer {

    private final List<String> unavailableUsernames;
    private UtilDateModel model;
    private JDatePanelImpl datePanel;
    private JDatePickerImpl datePicker;
    private UserAccount newAccount;

    public SignupTab() {
        unavailableUsernames = new ArrayList<>();
        initComponents();
    }

    // Used for testing , fills the form automatically
    public SignupTab(String user) {
        this();
        usernameField.setText(user);
        passwordField.setText(user);
        repeatPasswordField.setText(user);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        emailField = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        usernameCheckIcon = new javax.swing.JLabel();
        usernameCheckIcon.setVisible(false);
        usernameField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        repeatPasswordField = new javax.swing.JPasswordField();
        repeatPasswordLabel = new javax.swing.JLabel();
        signupRequiredFieldsLabel = new javax.swing.JLabel();
        emailCheckIcon = new javax.swing.JLabel();
        emailCheckIcon.setVisible(false);
        repeatPasswordCheckIcon = new javax.swing.JLabel();
        repeatPasswordCheckIcon.setVisible(false);
        passwordCheckIcon = new javax.swing.JLabel();
        passwordCheckIcon.setVisible(false);
        signupButton = new javax.swing.JButton();
        loginButton = new javax.swing.JButton();
        usernameUnavailableLabel = new javax.swing.JLabel();
        usernameUnavailableLabel.setVisible(false);
        signupLAL = new javax.swing.JLabel();
        signupLAL.setVisible(false);
        birthdateLabel = new javax.swing.JLabel();
        bdPane = new javax.swing.JPanel();
        model = new UtilDateModel();
        datePanel = new JDatePanelImpl(model);
        datePanel.setForeground(Color.WHITE);
        datePicker = new JDatePickerImpl(datePanel);
        datePicker.setSize(284, 36);
        bdPane.add(datePicker);
        createAccountLabel1 = new javax.swing.JLabel();
        signupConnLabel = new javax.swing.JLabel();

        setBackground(java.awt.Color.white);
        setMinimumSize(new java.awt.Dimension(700, 510));

        jLabel2.setFont(new java.awt.Font("Ubuntu", 0, 15)); // NOI18N
        jLabel2.setText("Email:");
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("client/gui/Bundle"); // NOI18N
        jLabel2.setToolTipText(bundle.getString("emailTooltip")); // NOI18N

        emailField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                emailFieldKeyReleased(evt);
            }
        });

        jLabel3.setFont(jLabel2.getFont());
        jLabel3.setText("*Username:");
        jLabel3.setToolTipText(bundle.getString("usernameTooltip")); // NOI18N

        usernameCheckIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/check_ok/ok16.png"))); // NOI18N

        usernameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                usernameFieldKeyReleased(evt);
            }
        });

        jLabel4.setFont(jLabel2.getFont());
        jLabel4.setText("*Password:");
        jLabel4.setToolTipText(bundle.getString("passwordTooltip")); // NOI18N

        passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                passwordFieldKeyReleased(evt);
            }
        });

        repeatPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                repeatPasswordFieldKeyReleased(evt);
            }
        });

        repeatPasswordLabel.setFont(jLabel2.getFont());
        repeatPasswordLabel.setText("*Repeat Password:");
        repeatPasswordLabel.setToolTipText(bundle.getString("repeatPasswordTooltip")); // NOI18N

        signupRequiredFieldsLabel.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        signupRequiredFieldsLabel.setForeground(new java.awt.Color(49, 65, 209));
        signupRequiredFieldsLabel.setText(bundle.getString("signupRequiredFieldsLabel")); // NOI18N

        emailCheckIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/check_ok/ok16.png"))); // NOI18N

        repeatPasswordCheckIcon.setIcon(emailCheckIcon.getIcon());

        passwordCheckIcon.setIcon(emailCheckIcon.getIcon());

        signupButton.setBackground(new java.awt.Color(78, 110, 187));
        signupButton.setForeground(java.awt.Color.white);
        signupButton.setText("Sign up !");
        signupButton.setBorderPainted(false);
        signupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signupButtonActionPerformed(evt);
            }
        });

        loginButton.setBackground(new java.awt.Color(102, 102, 102));
        loginButton.setForeground(java.awt.Color.white);
        loginButton.setText("Cancel");
        loginButton.setBorderPainted(false);
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        usernameUnavailableLabel.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        usernameUnavailableLabel.setForeground(java.awt.Color.red);
        usernameUnavailableLabel.setText(bundle.getString("usernameUnavailableLabel")); // NOI18N

        signupLAL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        signupLAL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/loading_circle_animation/21.gif"))); // NOI18N

        birthdateLabel.setFont(jLabel2.getFont());
        birthdateLabel.setText("Birth Date:");

        javax.swing.GroupLayout bdPaneLayout = new javax.swing.GroupLayout(bdPane);
        bdPane.setLayout(bdPaneLayout);
        bdPaneLayout.setHorizontalGroup(
            bdPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 284, Short.MAX_VALUE)
        );
        bdPaneLayout.setVerticalGroup(
            bdPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        createAccountLabel1.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        createAccountLabel1.setForeground(new java.awt.Color(220, 20, 60));
        createAccountLabel1.setText(bundle.getString("signupTitleLabel")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(repeatPasswordLabel)
                    .addComponent(birthdateLabel)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usernameUnavailableLabel)
                    .addComponent(bdPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(repeatPasswordField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(emailField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(loginButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                            .addComponent(signupButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(usernameField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                            .addComponent(signupRequiredFieldsLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(signupLAL)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(signupConnLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(repeatPasswordCheckIcon)
                            .addComponent(passwordCheckIcon)
                            .addComponent(usernameCheckIcon)
                            .addComponent(emailCheckIcon))))
                .addContainerGap(140, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(createAccountLabel1)
                .addGap(261, 261, 261))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(createAccountLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addComponent(usernameCheckIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameUnavailableLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(emailCheckIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(passwordCheckIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(repeatPasswordCheckIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(repeatPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(repeatPasswordLabel)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(birthdateLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bdPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(signupRequiredFieldsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(signupLAL)
                    .addComponent(signupConnLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(signupButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void signupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signupButtonActionPerformed
        if (evaluateFields()) {
            setPaneState(false);
            sendSignupRequest();
        }
    }//GEN-LAST:event_signupButtonActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        LoginTab loginTab = new LoginTab();
        R.getMf().getT().addTab(LOGIN_TAB_TITLE, JCHAT_LOGO, loginTab, LOGIN_TAB_TIP);
        R.getMf().getT().setSelectedComponent(loginTab);
        R.getMf().getT().remove(SignupTab.this);
    }//GEN-LAST:event_loginButtonActionPerformed

    private void emailFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailFieldKeyReleased
        // TODO add your handling code here:
        evaluateEmail(emailField.getText());
    }//GEN-LAST:event_emailFieldKeyReleased

    private void usernameFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usernameFieldKeyReleased
        evaluateUsername(usernameField.getText());
    }//GEN-LAST:event_usernameFieldKeyReleased

    private void passwordFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordFieldKeyReleased
        evaluatePasswords(passwordField.getPassword(), repeatPasswordField.getPassword());
    }//GEN-LAST:event_passwordFieldKeyReleased

    private void repeatPasswordFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_repeatPasswordFieldKeyReleased
        //R.log("pw: " + new String(passwordField.getPassword()));
        //R.log("pw r: " + new String(repeatPasswordField.getPassword()));
        R.log(Arrays.toString(repeatPasswordField.getPassword()));
        evaluatePasswords(passwordField.getPassword(), repeatPasswordField.getPassword());
    }//GEN-LAST:event_repeatPasswordFieldKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bdPane;
    private javax.swing.JLabel birthdateLabel;
    private javax.swing.JLabel createAccountLabel1;
    private javax.swing.JLabel emailCheckIcon;
    private javax.swing.JFormattedTextField emailField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton loginButton;
    private javax.swing.JLabel passwordCheckIcon;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel repeatPasswordCheckIcon;
    private javax.swing.JPasswordField repeatPasswordField;
    private javax.swing.JLabel repeatPasswordLabel;
    private javax.swing.JButton signupButton;
    private javax.swing.JLabel signupConnLabel;
    private javax.swing.JLabel signupLAL;
    private javax.swing.JLabel signupRequiredFieldsLabel;
    private javax.swing.JLabel usernameCheckIcon;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameUnavailableLabel;
    // End of variables declaration//GEN-END:variables
    private OQueue q;
    private Message m;

    @Override
    public void update(Observable o, Object arg) {
        if (!arg.equals(this.getClass().getSimpleName())) {
            if (arg.equals(MessageType.NO_CONNECTION_BROADCAST)) {
                setPaneState(true);
            }
            return;
        }

        q = ((OQueue) o);
        m = (Message) q.poll();

        R.log("SignupTab received " + m.getType().toString());

        switch (m.getType()) {

            case USERNAME_UNAVAILABLE:
                unavailableUsernames.add(newAccount.getUsername());
                break;
            case ACCOUNT_ID:
                // Store account
                StrongPasswordEncryptor spe = new StrongPasswordEncryptor();
                newAccount.setPassword(spe.encryptPassword(newAccount.getPassword()));
                newAccount.setId((Integer) m.getContent());
                DB.insert(newAccount);

                newAccount.setPassword("");
                R.getAppPrefs().putInt(LOGIN_ACCOUNT, newAccount.getId());
                R.setUserAccount(newAccount);

                // Create account reference
                MyAccountRef mac = new MyAccountRef();
                mac.setAccountId(newAccount.getId());
                DB.insert(mac);
                break;
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                signupLAL.setVisible(false);
                switch (m.getType()) {
                    case USERNAME_UNAVAILABLE:
                        usernameUnavailableLabel.setVisible(true);
                        loginButton.setEnabled(true);
                        signupButton.setEnabled(true);
                        break;

                    case ACCOUNT_ID:
                        AccountCreatedTab signupSuccessTab = new AccountCreatedTab();
                        R.getMf().getT().addTab(SIGNUP_SUCCESS_TAB_TITLE,
                                JCHAT_LOGO,
                                signupSuccessTab,
                                SIGNUP_SUCCESS_TAB_TIP);
                        R.getMf().getT().setSelectedComponent(signupSuccessTab);
                        R.getMf().getT().remove(SignupTab.this);
                        break;
                }
            }

        });

    }

    private void sendSignupRequest() {
        setPaneState(false);
        // Prepare message to be sent
        newAccount = new UserAccount();
        newAccount.setEmail(emailField.getText());
        newAccount.setUsername(usernameField.getText());
        newAccount.setPassword(new String(passwordField.getPassword()));
        Date selectedDate = (Date) datePicker.getModel().getValue();
        if (selectedDate == null) {
            R.log("Birthdate: null");
        } else {
            newAccount.setBirthDate(selectedDate);
            R.log("Birthdate: " + selectedDate.toString());
        }
        //R.setUserAccount(USER); // is then used by SIGNUP_SUCCESS by IMH

        // Overwrite the password with zeros for security
        Arrays.fill(passwordField.getPassword(), '0');
        Arrays.fill(repeatPasswordField.getPassword(), '0');

        // Send signup request to the Server
        ConnectionTask connectionTask = new ConnectionTask(signupConnLabel);
        connectionTask.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("state".equals(evt.getPropertyName())) {
                    if (evt.getNewValue().equals(SwingWorker.StateValue.DONE)) {
                        if (NetworkManager.isConnected()) {
                            signupConnLabel.setText("Sending signup request");
                            Message signupRequest = new Message(MessageType.SIGNUP_REQUEST, newAccount);
                            NetworkManager.send(signupRequest);
                            signupConnLabel.setText("Waiting for response");
                        } else {
                            signupConnLabel.setText("Could not connect to the Server");
                            setPaneState(true);
                        }
                    }
                }
            }
        });
        connectionTask.execute();

    }

    private boolean evaluateFields() {
        if (evaluateUsername(usernameField.getText())) {
            if (evaluateEmail(emailField.getText())) {
                if (evaluatePasswords(passwordField.getPassword(), repeatPasswordField.getPassword())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean evaluateUsername(String text) {
        if (unavailableUsernames.contains(usernameField.getText())) {
            usernameUnavailableLabel.setVisible(true);
            R.log("Username unavailable");
            return false;
        } else {
            usernameUnavailableLabel.setVisible(false);
        }

        boolean valid = Utils.isValidUsername(text);
        usernameCheckIcon.setVisible(valid);
        R.log("Username valid: " + valid);

        return valid;
    }

    private boolean evaluateEmail(String text) {
        boolean valid = text.isEmpty() || Utils.isValidEmailAddress(emailField.getText());
        if (valid) {
            emailCheckIcon.setVisible(true);
        } else {
            emailCheckIcon.setVisible(false);
        }
        return valid;
    }

    private boolean evaluatePasswords(char[] pass1, char[] pass2) {
        String password = new String(pass1);
        boolean valid = Utils.isValidPassword(password);
        if (valid) {
            passwordCheckIcon.setVisible(true);
        } else {
            passwordCheckIcon.setVisible(false);
            repeatPasswordCheckIcon.setVisible(false);
            return false;
        }

        if (passwordEqualsConfirmation(pass1, pass2)) {
            repeatPasswordCheckIcon.setVisible(true);
            valid = true;
        } else {
            repeatPasswordCheckIcon.setVisible(false);
            valid = false;
        }

        return valid;
    }

    private boolean passwordEqualsConfirmation(char[] password1, char[] password2) {
        int len = password1.length;
        if (len != password2.length) {
            return false;
        }

        for (int i = 0; i < len; i++) {
            if (password1[i] != password2[i]) {
                return false;
            }
        }

        return true;
    }

    private void setPaneState(final boolean tru) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                signupButton.setEnabled(tru);
                loginButton.setEnabled(tru);
                signupLAL.setVisible(!tru);
            }
        });
    }

}
