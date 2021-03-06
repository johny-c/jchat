/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import java.awt.Component;

/**
 *
 * @author johny
 */
public class AppTabComponent extends javax.swing.JPanel {

    ChatTabbedPane tabs;
    Component component;

    public AppTabComponent(ChatTabbedPane tabs, Component component) {
        this.tabs = tabs;
        this.component = component;
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

        tabTitleLabel = new javax.swing.JLabel() {
            public String getText() {
                int i = tabs.indexOfTabComponent(AppTabComponent.this);
                if (i != -1) {
                    return tabs.getTitleAt(i);
                }
                return "";
            }
        }
        ;
        tabCloseButton = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(150, 20));
        setMinimumSize(new java.awt.Dimension(130, 18));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(130, 18));

        tabCloseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/notification/close_button/close16.png"))); // NOI18N
        tabCloseButton.setBorderPainted(false);
        tabCloseButton.setContentAreaFilled(false);
        tabCloseButton.setFocusPainted(false);
        tabCloseButton.setFocusable(false);
        tabCloseButton.setMaximumSize(new java.awt.Dimension(16, 16));
        tabCloseButton.setMinimumSize(new java.awt.Dimension(16, 16));
        tabCloseButton.setPreferredSize(new java.awt.Dimension(16, 16));
        tabCloseButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tabCloseButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tabCloseButtonMouseEntered(evt);
            }
        });
        tabCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tabCloseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(tabCloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(tabCloseButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tabCloseButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabCloseButtonMouseEntered
        tabCloseButton.setBorderPainted(true);
    }//GEN-LAST:event_tabCloseButtonMouseEntered

    private void tabCloseButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabCloseButtonMouseExited
        tabCloseButton.setBorderPainted(false);
    }//GEN-LAST:event_tabCloseButtonMouseExited

    private void tabCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tabCloseButtonActionPerformed
        tabs.removeAppTab(component);
    }//GEN-LAST:event_tabCloseButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton tabCloseButton;
    private javax.swing.JLabel tabTitleLabel;
    // End of variables declaration//GEN-END:variables

}
