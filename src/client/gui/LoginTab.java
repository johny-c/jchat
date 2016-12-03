package client.gui;

import client.db.util.DB;
import client.networking.NetworkManager;
import client.networking.R;
import client.tasks.ConnectionTask;
import common.db.entity.UserAccount;
import common.db.entity.UserIcon;
import common.db.entity.UserSession;
import common.utils.Conventions;
import common.utils.Message;
import common.utils.MessageType;
import common.utils.OQueue;
import common.utils.Utils;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 *
 * @author johny
 */
public class LoginTab extends javax.swing.JPanel implements Observer, Conventions {

    private UserAccount inputAccount;
    private UserAccount matchingAccount;
    private OQueue q;
    private Message m;

    /**
     * Creates new form LoginTab
     */
    public LoginTab() {
        initComponents();
        // Check if form needs to be filled due to user preference
        boolean shouldFillForm = R.getAppPrefs().getBoolean(REMEMBER_CREDENTIALS, DEFAULT_REMEMBER_CREDENTIALS);
        Integer prefUacId = R.getAppPrefs().getInt(LOGIN_ACCOUNT, DEFAULT_LOGIN_ACCOUNT);
        List<Integer> acids = DB.getMyAccounts();

        if (shouldFillForm) { // user pref is to remember credentials
            if (acids != null) {
                if (acids.size() > 0) {
                    inputAccount = (UserAccount) DB.get(prefUacId, UserAccount.class);
                    if (inputAccount == null) {
                        inputAccount = (UserAccount) DB.get(acids.get(0), UserAccount.class);
                    }

                    // Having a valid account
                    usernameField.setText(inputAccount.getUsername());
                    passwordField.setText(inputAccount.getPassword());
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginWelcomeLabel = new javax.swing.JLabel();
        loginLAL = new javax.swing.JLabel();
        loginLAL.setVisible(false);
        loginButton = new javax.swing.JButton();
        loginRememberMeCheckBox = new javax.swing.JCheckBox();
        createAccountButton = new javax.swing.JButton();
        loginErrorLabel = new javax.swing.JLabel();
        loginErrorLabel.setVisible(false);
        usernameField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        loginConnLabel = new javax.swing.JLabel();

        setBackground(java.awt.Color.white);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("client/gui/Bundle"); // NOI18N
        setToolTipText(bundle.getString("loginTabToolTip")); // NOI18N
        setMinimumSize(new java.awt.Dimension(600, 450));
        setName("Login"); // NOI18N
        setVerifyInputWhenFocusTarget(false);

        loginWelcomeLabel.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        loginWelcomeLabel.setForeground(new java.awt.Color(65, 179, 129));
        loginWelcomeLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        loginWelcomeLabel.setText(bundle.getString("loginWelcomeLabel")); // NOI18N
        loginWelcomeLabel.setAlignmentX(0.5F);
        loginWelcomeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        loginLAL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginLAL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/loading_circle_animation/21.gif"))); // NOI18N
        loginLAL.setText(bundle.getString("loadingAnimationLabel")); // NOI18N

        loginButton.setBackground(new java.awt.Color(65, 179, 129));
        loginButton.setForeground(java.awt.Color.white);
        loginButton.setText(bundle.getString("loginButton")); // NOI18N
        loginButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        loginButton.setBorderPainted(false);
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        loginRememberMeCheckBox.setBackground(getBackground());
        loginRememberMeCheckBox.setSelected(R.getAppPrefs().getBoolean(REMEMBER_CREDENTIALS, DEFAULT_REMEMBER_CREDENTIALS));
        loginRememberMeCheckBox.setText("Remember me");

        createAccountButton.setBackground(new java.awt.Color(78, 110, 187));
        createAccountButton.setForeground(java.awt.Color.white);
        createAccountButton.setText(bundle.getString("loginCreateAccountButton")); // NOI18N
        createAccountButton.setBorderPainted(false);
        createAccountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createAccountButtonActionPerformed(evt);
            }
        });

        loginErrorLabel.setForeground(java.awt.Color.red);
        loginErrorLabel.setText(bundle.getString("loginErrorLabel")); // NOI18N

        usernameField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel1.setFont(new java.awt.Font("Ubuntu", 0, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(60, 59, 55));
        jLabel1.setText(bundle.getString("loginUsernameLabel")); // NOI18N

        jLabel2.setFont(jLabel1.getFont());
        jLabel2.setText(bundle.getString("loginPasswordLabel")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(loginErrorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                        .addContainerGap(66, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(loginWelcomeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(createAccountButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(loginRememberMeCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(loginButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(passwordField)
                            .addComponent(usernameField)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(loginLAL)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(loginConnLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(loginWelcomeLabel)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(12, 12, 12)
                .addComponent(loginErrorLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(loginLAL, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(loginConnLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loginRememberMeCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(createAccountButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleParent(this);
    }// </editor-fold>//GEN-END:initComponents

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        setPaneState(false);
        String usernameInput = usernameField.getText();
        String passwordInput = new String(passwordField.getPassword());

        R.log("Username input: " + usernameInput);
        R.log("Password input: " + passwordInput);
        if (!Utils.isValidUsername(usernameInput)) {
            // Invalid username (length)
            R.log("Invalid username input...");
            failedLogin();
            return;
        }

        List<Integer> storedAccountIds = DB.getMyAccounts();
        if (storedAccountIds == null) // Compare with locally stored credentials
        {
            JOptionPane.showMessageDialog(this,
                    "You must first create a JChat account.",
                    "No account found in Database!",
                    JOptionPane.ERROR_MESSAGE);
            failedLogin();
            return;
        }

        R.log("Found " + storedAccountIds.size() + " accounts in DB");
        UserAccount storedAccount;
        for (Integer accountId : storedAccountIds) {
            storedAccount = (UserAccount) DB.get(accountId, UserAccount.class);
            R.log("Account " + storedAccount.getId());
            R.log("Username " + storedAccount.getUsername());
            R.log("Pw " + storedAccount.getPassword());
            if (usernameInput.equals(storedAccount.getUsername())) { // found account by username
                R.log("Username match !");

                // in case form was auto-filled
                if (passwordInput.equals(storedAccount.getPassword())) {
                    R.log("Form Credentials match Account " + storedAccount.getId());
                    matchingLocalAccountFound(storedAccount);
                    return;
                }

                // in case form was filled by user
                StrongPasswordEncryptor spe = new StrongPasswordEncryptor();
                if (spe.checkPassword(passwordInput, storedAccount.getPassword())) {
                    R.log("User Credentials match Account " + storedAccount.getId());
                    matchingLocalAccountFound(storedAccount);
                    return;
                }
            }
        }

        // No matching account was found
        failedLogin();
    }//GEN-LAST:event_loginButtonActionPerformed

    private void matchingLocalAccountFound(final UserAccount account) {
        setPaneState(false);
        // Credentials match account, send new session request
        account.setPassword("");
        matchingAccount = account;

        // Send new session request, wait for server's response
        ConnectionTask connectionTask = new ConnectionTask(loginConnLabel);
        connectionTask.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("state".equals(evt.getPropertyName())) {
                    if (evt.getNewValue().equals(SwingWorker.StateValue.DONE)) {
                        if (NetworkManager.isConnected()) {
                            loginConnLabel.setText("Sending login request");
                            UserSession us = DB.getLastUserSession(account.getId());
                            R.log("Retreived last user session");
                            Message newSessionRequest = new Message(MessageType.NEW_USER_SESSION_REQUEST, us);
                            NetworkManager.send(newSessionRequest);
                            loginConnLabel.setText("Waiting for response");
                        } else {
                            loginConnLabel.setText("Could not connect to the Server");
                            setPaneState(true);
                        }
                    }
                }
            }
        });
        connectionTask.execute();

    }

    private void createAccountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createAccountButtonActionPerformed
        SignupTab signupTab = new SignupTab();
        R.getMf().getT().addTab("JChat - Signup", JCHAT_LOGO, signupTab, "JChat Signup");
        R.getMf().getT().setSelectedComponent(signupTab);
        R.getMf().getT().remove(LoginTab.this);
    }//GEN-LAST:event_createAccountButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createAccountButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton loginButton;
    private javax.swing.JLabel loginConnLabel;
    private javax.swing.JLabel loginErrorLabel;
    private javax.swing.JLabel loginLAL;
    private javax.swing.JCheckBox loginRememberMeCheckBox;
    private javax.swing.JLabel loginWelcomeLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField usernameField;
    // End of variables declaration//GEN-END:variables
    private WelcomeTab welcomeTab;

    // Called when received a new session-login response from server
    private void successfullLogin() { // UserAccount matchingAccount

        R.setUserAccount(matchingAccount);
        matchingAccount.setPrefs(Preferences.userRoot().node(JCHAT_PREFS + File.separator + matchingAccount.getUsername()));
        R.getAppPrefs().putBoolean(REMEMBER_CREDENTIALS, loginRememberMeCheckBox.isSelected());
        R.getAppPrefs().putInt(LOGIN_ACCOUNT, matchingAccount.getId());
        final UserIcon icon = DB.getUserIcon(matchingAccount.getId());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loginLAL.setVisible(false);
                R.getMf().setUserStatus(UserAccount.Status.ONLINE);
                if (icon != null) {
                    R.getMf().setUserIconLabel(new ImageIcon(icon.getIconData()));
                }
                R.getMf().getSettingsPane().setPaneState(true);

                welcomeTab = new WelcomeTab();
                R.getMf().getT().addTab(
                        "JChat - Welcome",
                        JCHAT_LOGO, welcomeTab, WELCOME_TAB_TIP);
                R.getMf().getT().setSelectedComponent(welcomeTab);
                R.getMf().getT().remove(LoginTab.this);
            }
        });
    }

    private void failedLogin() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // update UI
                setPaneState(true);
                loginErrorLabel.setVisible(true);
            }
        });
    }

    private void setPaneState(final boolean tru) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loginLAL.setVisible(!tru);
                loginButton.setEnabled(tru);
                loginRememberMeCheckBox.setEnabled(tru);
                createAccountButton.setEnabled(tru);
                if (!tru) {
                    loginConnLabel.setText("");
                }
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!arg.equals(this.getClass().getSimpleName())) {
            if (arg.equals(MessageType.NO_CONNECTION_BROADCAST)) {
                setPaneState(true);
            }
            return;
        }

        q = (OQueue) o;
        m = (Message) q.poll();

        R.log("Login Tab receiving " + m.getType());

        switch (m.getType()) {
            case LOGIN_SUCCESS:
                successfullLogin();
                break;
            case LOGIN_FAIL:
                failedLogin();
                break;
        }
    }

}