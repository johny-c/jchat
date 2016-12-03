/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.db.util.DB;
import client.networking.R;
import common.db.entity.Notification;
import common.db.entity.UserAccount;
import common.utils.Conventions;
import common.utils.Message;
import common.utils.OQueue;
import common.utils.Utils;
import java.awt.Component;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author johny
 */
public class NewsPane extends javax.swing.JPanel implements Observer, Conventions {

    private int counter = 0;

    /**
     * Creates new form NewsPane
     */
    public NewsPane() {
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
        jScrollPane2 = new javax.swing.JScrollPane();
        notificationsListPane = new javax.swing.JPanel();

        setName("NEWSPANE"); // NOI18N
        setPreferredSize(new java.awt.Dimension(250, 544));

        newsPaneLabel.setBackground(new java.awt.Color(60, 59, 55));
        newsPaneLabel.setFont(new java.awt.Font("Serif", 1, 15)); // NOI18N
        newsPaneLabel.setForeground(java.awt.Color.white);
        newsPaneLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        newsPaneLabel.setText("News");
        newsPaneLabel.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        newsPaneLabel.setOpaque(true);

        notificationsListPane.setBackground(new java.awt.Color(182, 47, 97));
        notificationsListPane.setLayout(new javax.swing.BoxLayout(notificationsListPane, javax.swing.BoxLayout.PAGE_AXIS));
        jScrollPane2.setViewportView(notificationsListPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(newsPaneLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(newsPaneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleParent(this);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel newsPaneLabel;
    private static javax.swing.JPanel notificationsListPane;
    // End of variables declaration//GEN-END:variables
    //private DefaultListModel<Notification> model;
    private DB db;
    private OQueue q;
    private Message m;

    @Override
    public void update(Observable o, Object arg) {
        if (!arg.equals(this.getClass().getSimpleName())) {
            return;
        }

        q = (OQueue) o;
        m = (Message) q.poll();

        R.log("NewsPane seeing message " + m.getType());

        if (m.getContent() instanceof UserAccount) {
            Utils.printInfo((UserAccount) m.getContent());
        }

        Notification n = (Notification) m.getContent();

        R.log("NewsPane creating cell ");

        NotificationCell cell = new NotificationCell(n, this);

        R.log("NewsPane adding cell ");

        addCell(cell);

        R.log("NewsPane added cell ");
    }

    class NotificationCellRenderer implements ListCellRenderer, Conventions {

        public NotificationCellRenderer() {
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            //Status = BY_SERVER, DELIVERED, READ, HANDLED
            //If status != READ || HANDLED
            NotificationCell component = new NotificationCell((Notification) value, NewsPane.this);
            //component.setBackground(cellHasFocus ? COLOR_LIGHT_BLACK : Color.WHITE);
            //component.setForeground(cellHasFocus ? Color.white : COLOR_LIGHT_BLACK);
            return component;
        }
    }

    private void addCell(NotificationCell nc) {
        notificationsListPane.add(nc);
        counter++;
        R.getMf().updateNotificationsCounter(counter);
        revalidate();
        repaint();
    }

    public void removeCell(NotificationCell nc) {
        notificationsListPane.remove(nc);
        counter--;
        R.getMf().updateNotificationsCounter(counter);
        revalidate();
        repaint();
    }
}