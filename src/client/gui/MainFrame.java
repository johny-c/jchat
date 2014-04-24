/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.pojos.R;
import common.pojos.Conventions;
import common.pojos.Message;
import common.pojos.OQueue;
import common.pojos.Utils;
import common.db.entity.User;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author johny
 */
public class MainFrame extends javax.swing.JFrame implements Observer, Conventions, ActionListener {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        R.getImh().subscribe(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        userStatusPopupMenu = new javax.swing.JPopupMenu();
        statusOnlineMenuItem = new javax.swing.JMenuItem();
        statusAwayMenuItem = new javax.swing.JMenuItem();
        statusBusyMenuItem = new javax.swing.JMenuItem();
        statusAppearOfflineMenuItem = new javax.swing.JMenuItem();
        menuPane = new javax.swing.JLayeredPane();
        notificationsButton = new javax.swing.JButton();
        settingsButton = new javax.swing.JButton();
        contactsButton = new javax.swing.JButton();
        notificationsCounterBackground = new javax.swing.JLabel();
        notificationsCounterBackground.setVisible(false);
        notificationsCounterLabel = new javax.swing.JLabel();
        notificationsCounterLabel.setVisible(false);
        loadingNotificationsAnimationLabel = new javax.swing.JLabel();
        loadingNotificationsAnimationLabel.setVisible(false);
        loadingContactsAnimationLabel = new javax.swing.JLabel();
        loadingContactsAnimationLabel.setVisible(false);
        addContactButton = new javax.swing.JButton();
        logoutButton = new javax.swing.JButton();
        popupCardPane = new javax.swing.JPanel();
        popupCardPane.setVisible(false);
        tabbedPane = new javax.swing.JTabbedPane();
        trayPane = new javax.swing.JPanel();
        userLabel = new javax.swing.JLabel();
        userStatusButton = new javax.swing.JButton();

        statusOnlineMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/status/online16.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("client/gui/Bundle"); // NOI18N
        statusOnlineMenuItem.setText(bundle.getString("statusOnlineMenuItemText")); // NOI18N
        statusOnlineMenuItem.setActionCommand(common.db.entity.User.Status.ONLINE.toString());
        statusOnlineMenuItem.addActionListener(this);
        userStatusPopupMenu.add(statusOnlineMenuItem);

        statusAwayMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/status/away16.png"))); // NOI18N
        statusAwayMenuItem.setText(bundle.getString("statusAwayMenuItemText")); // NOI18N
        statusAwayMenuItem.setActionCommand(common.db.entity.User.Status.AWAY.toString());
        statusAwayMenuItem.addActionListener(this);
        userStatusPopupMenu.add(statusAwayMenuItem);

        statusBusyMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/status/busy16.png"))); // NOI18N
        statusBusyMenuItem.setText(bundle.getString("statusBusyMenuItemText")); // NOI18N
        statusBusyMenuItem.setActionCommand(common.db.entity.User.Status.BUSY.toString());
        statusBusyMenuItem.addActionListener(this);
        userStatusPopupMenu.add(statusBusyMenuItem);

        statusAppearOfflineMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/status/offline16.png"))); // NOI18N
        statusAppearOfflineMenuItem.setText(bundle.getString("statusAppearOfflineMenuItemText")); // NOI18N
        statusAppearOfflineMenuItem.setActionCommand(common.db.entity.User.Status.APPEAR_OFFLINE.toString());
        statusAppearOfflineMenuItem.addActionListener(this);
        userStatusPopupMenu.add(statusAppearOfflineMenuItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JChat");
        setBackground(new java.awt.Color(60, 59, 55));
        setMinimumSize(new java.awt.Dimension(100, 100));

        menuPane.setBackground(COLOR_PAPAYA);
        menuPane.setForeground(java.awt.Color.orange);
        menuPane.setOpaque(true);

        notificationsButton.setBackground(new Color(0,0,0,0));
        notificationsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/news/news48.png"))); // NOI18N
        notificationsButton.setToolTipText("News");
        notificationsButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(61, 36, 201), new java.awt.Color(239, 8, 8), new java.awt.Color(242, 222, 11), new java.awt.Color(14, 212, 47)));
        notificationsButton.setBorderPainted(false);
        notificationsButton.setContentAreaFilled(false);
        notificationsButton.setFocusPainted(false);
        notificationsButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        notificationsButton.setMaximumSize(new java.awt.Dimension(50, 50));
        notificationsButton.setMinimumSize(new java.awt.Dimension(48, 48));
        notificationsButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/news/news32.png"))); // NOI18N
        notificationsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notificationsButtonActionPerformed(evt);
            }
        });
        notificationsButton.setBounds(3, 10, 48, 48);
        menuPane.add(notificationsButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        settingsButton.setForeground(new java.awt.Color(230, 25, 25));
        settingsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/settings/settings48.png"))); // NOI18N
        settingsButton.setToolTipText("Settings");
        settingsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        settingsButton.setBorderPainted(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setFocusPainted(false);
        settingsButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/settings/settings32.png"))); // NOI18N
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });
        settingsButton.setBounds(3, 184, 48, 48);
        menuPane.add(settingsButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        contactsButton.setBackground(new Color(0,0,0,0));
        contactsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/contacts/contacts48.png"))); // NOI18N
        contactsButton.setToolTipText("Contacts");
        contactsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        contactsButton.setBorderPainted(false);
        contactsButton.setContentAreaFilled(false);
        contactsButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/contacts/contacts32.png"))); // NOI18N
        contactsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactsButtonActionPerformed(evt);
            }
        });
        contactsButton.setBounds(3, 68, 48, 48);
        menuPane.add(contactsButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        notificationsCounterBackground.setBackground(new java.awt.Color(94, 203, 64));
        notificationsCounterBackground.setForeground(java.awt.Color.white);
        notificationsCounterBackground.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        notificationsCounterBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/notification/counter/plain24.png"))); // NOI18N
        notificationsCounterBackground.setBounds(27, 37, 24, 24);
        menuPane.add(notificationsCounterBackground, javax.swing.JLayeredPane.PALETTE_LAYER);

        notificationsCounterLabel.setFont(new java.awt.Font("Liberation Sans", 1, 12)); // NOI18N
        notificationsCounterLabel.setForeground(java.awt.Color.white);
        notificationsCounterLabel.setText("15");
        notificationsCounterLabel.setBounds(34, 43, 14, 12);
        menuPane.add(notificationsCounterLabel, javax.swing.JLayeredPane.MODAL_LAYER);

        loadingNotificationsAnimationLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/loading_circle_animation/21.gif"))); // NOI18N
        loadingNotificationsAnimationLabel.setBounds(12, 20, 32, 32);
        menuPane.add(loadingNotificationsAnimationLabel, javax.swing.JLayeredPane.POPUP_LAYER);
        loadingContactsAnimationLabel.setBounds(10, 78, 32, 32);
        menuPane.add(loadingContactsAnimationLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        addContactButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/add_contact/add-contact48.png"))); // NOI18N
        addContactButton.setBorderPainted(false);
        addContactButton.setContentAreaFilled(false);
        addContactButton.setFocusPainted(false);
        addContactButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/add_contact/add-contact32.png"))); // NOI18N
        addContactButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addContactButtonActionPerformed(evt);
            }
        });
        addContactButton.setBounds(3, 126, 48, 48);
        menuPane.add(addContactButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        logoutButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/logout/logout-icon48.png"))); // NOI18N
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/logout/logout-icon32.png"))); // NOI18N
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });
        logoutButton.setBounds(3, 242, 48, 48);
        menuPane.add(logoutButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        popupCardPane.setBorder(null);
        popupCardPane.setMaximumSize(new java.awt.Dimension(300, 5000));
        popupCardPane.setMinimumSize(new java.awt.Dimension(250, 620));
        popupCardPane.setPreferredSize(new java.awt.Dimension(300, 620));
        popupCardPane.setLayout(new java.awt.CardLayout());

        tabbedPane.setBackground(java.awt.Color.darkGray);
        tabbedPane.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(164, 62, 62), null, null));
        tabbedPane.setMinimumSize(new java.awt.Dimension(600, 620));
        tabbedPane.setOpaque(true);
        tabbedPane.setPreferredSize(new java.awt.Dimension(600, 620));

        trayPane.setBackground(java.awt.Color.darkGray);
        trayPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trayPaneMouseClicked(evt);
            }
        });

        userLabel.setForeground(java.awt.Color.white);
        userLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/minions/0005-Minion-Hello-icon.png"))); // NOI18N
        userLabel.setText("Username");

        userStatusButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/status/offline24.png"))); // NOI18N
        userStatusButton.setBorderPainted(false);
        userStatusButton.setContentAreaFilled(false);
        userStatusButton.setFocusPainted(false);
        userStatusButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/status/offline16.png"))); // NOI18N
        userStatusButton.addMouseListener(new PopupListener(userStatusPopupMenu));

        javax.swing.GroupLayout trayPaneLayout = new javax.swing.GroupLayout(trayPane);
        trayPane.setLayout(trayPaneLayout);
        trayPaneLayout.setHorizontalGroup(
            trayPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(trayPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userStatusButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        trayPaneLayout.setVerticalGroup(
            trayPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(trayPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(trayPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(userLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addComponent(userStatusButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(menuPane, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(popupCardPane, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
                    .addComponent(trayPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuPane)
            .addComponent(popupCardPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(trayPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuPane.getAccessibleContext().setAccessibleParent(this);
        popupCardPane.getAccessibleContext().setAccessibleParent(menuPane);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void contactsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactsButtonActionPerformed
        // TODO add your handling code here:
        if (contactsPane == null) {
            loadingContactsAnimationLabel.setVisible(true);
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    contactsPane = new ContactsPane();
                    return null;
                }

                @Override
                protected void done() {
                    loadingContactsAnimationLabel.setVisible(false);
                    popupCardPane.add(contactsPane, contactsPane.getName());
                    showOrHide(contactsPane);
                }
            }.execute();

            System.out.println("Creating newsPane with main frame ref");
        } else {
            showOrHide(contactsPane);
        }
    }//GEN-LAST:event_contactsButtonActionPerformed

    private void notificationsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notificationsButtonActionPerformed
        // TODO add your handling code here:
        if (newsPane == null) {
            loadingNotificationsAnimationLabel.setVisible(true);
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    newsPane = new NewsPane(MainFrame.this);
                    return null;
                }

                @Override
                protected void done() {
                    loadingNotificationsAnimationLabel.setVisible(false);
                    popupCardPane.add(newsPane, newsPane.getName());
                    showOrHide(newsPane);
                }
            }.execute();

            System.out.println("Creating newsPane with main frame ref");
        } else {
            showOrHide(newsPane);
        }

    }//GEN-LAST:event_notificationsButtonActionPerformed

    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
        // TODO add your handling code here:
        if (settingsPane == null) {
            settingsPane = new SettingsPane();
            popupCardPane.add(settingsPane, settingsPane.getName());
        }
        showOrHide(settingsPane);

    }//GEN-LAST:event_settingsButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        Message m = new Message(LOGOUT_REQUEST);
        R.getNm().send(m);
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void addContactButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addContactButtonActionPerformed
        // TODO add your handling code here:
        if (addContactPane == null) {
            addContactPane = new AddContactPane();
            popupCardPane.add(addContactPane, addContactPane.getName());
        }
        showOrHide(addContactPane);
    }//GEN-LAST:event_addContactButtonActionPerformed

    private void trayPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trayPaneMouseClicked
    }//GEN-LAST:event_trayPaneMouseClicked

    private void showOrHide(JPanel pane) {
        if (popupCardPane.isVisible() && currentlyShown.equals(pane.getName())) {
            popupCardPane.setVisible(false);
            return;
        }

        CardLayout cl = (CardLayout) (popupCardPane.getLayout());
        cl.show(popupCardPane, pane.getName());
        currentlyShown = pane.getName();
        if (!popupCardPane.isVisible()) {
            popupCardPane.setVisible(true);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        R r = new R();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mf = new MainFrame();
                mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mf.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addContactButton;
    private javax.swing.JButton contactsButton;
    private javax.swing.JLabel loadingContactsAnimationLabel;
    private javax.swing.JLabel loadingNotificationsAnimationLabel;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLayeredPane menuPane;
    private javax.swing.JButton notificationsButton;
    private javax.swing.JLabel notificationsCounterBackground;
    private javax.swing.JLabel notificationsCounterLabel;
    private javax.swing.JPanel popupCardPane;
    private javax.swing.JButton settingsButton;
    private javax.swing.JMenuItem statusAppearOfflineMenuItem;
    private javax.swing.JMenuItem statusAwayMenuItem;
    private javax.swing.JMenuItem statusBusyMenuItem;
    private javax.swing.JMenuItem statusOnlineMenuItem;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JPanel trayPane;
    private javax.swing.JLabel userLabel;
    private javax.swing.JButton userStatusButton;
    private javax.swing.JPopupMenu userStatusPopupMenu;
    // End of variables declaration//GEN-END:variables
    private NewsPane newsPane;
    private ContactsPane contactsPane;
    private SettingsPane settingsPane;
    private AddContactPane addContactPane;
    private InvitationPane invitationPane;
    private String currentlyShown = "";
    private OQueue q;
    private Message m;

    void putTab(JPanel tab) {
        tabbedPane.addTab(tab.getName(), JCHAT_LOGO, tab, tab.getToolTipText());
    }

    JTabbedPane getT() {
        return tabbedPane;
    }

    void updateNotificationsCounter(int counter) {
        notificationsCounterLabel.setText("" + counter + "");
        if (counter == 0) {
            notificationsCounterBackground.setVisible(false);
            notificationsCounterLabel.setVisible(false);
            popupCardPane.setVisible(false);
        } else {
            if (!notificationsCounterBackground.isVisible()) {
                notificationsCounterBackground.setVisible(true);
            }
            if (!notificationsCounterLabel.isVisible()) {
                notificationsCounterLabel.setVisible(true);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!arg.equals(this.getClass().getSimpleName())) {
            return;
        }

        OQueue q = (OQueue) o;
        final Message m = (Message) q.poll();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                switch (m.getCode()) {

                    case LOGOUT_RESPONSE:
                        userStatusButton.setIcon(Utils.getIIcon("status/offline24.png"));
                        break;
                }
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
        String newStatus = source.getActionCommand();
        R.getU().setStatus(User.Status.valueOf(newStatus));

        Message m = new Message(USER_STATUS_UPDATE, R.getU());
        R.getNm().send(m);
    }

    public void openInvitationPane() {
    }

    public void addTab(String name, Icon JCHAT_LOGO, LoginTab tab, String toolTipText) {
        tabbedPane.addTab(name, JCHAT_LOGO, tab, toolTipText);
    }

    void changeUserStatusIcon(final String status) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                userStatusButton.setIcon(Utils.getIIcon("status/" + status + "24.png"));
            }
        });

    }

    class PopupListener extends MouseAdapter {

        JPopupMenu popup;

        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(),
                        e.getX(), e.getY());
            }
        }
    }
}
