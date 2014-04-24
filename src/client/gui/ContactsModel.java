package client.gui;

import client.pojos.R;
import common.db.entity.Contact;
import client.db.util.Database;
import common.pojos.Conventions;
import common.pojos.Message;
import common.pojos.OQueue;
import common.pojos.Utils;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class ContactsModel extends AbstractTableModel implements TableModelListener, Observer, Conventions {

    private static final boolean DEBUG = true;
    private String[] columnNames = {"Username", "Status", "ChatButton", "VideoCallButton", "DeleteButton"};
    private List<Contact> data;
    private Database db;

    ContactsModel() {
        db = new Database();
        List<Object> objects = db.select(Contact.class);
        for (Object o : objects) {
            Contact c = (Contact) o;
            data.add(c);
        }
        addTableModelListener(this);
        R.getImh().subscribe(this);
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Contact u = data.get(row);

        switch (col) {
            case 0:
                return u.getUsernameOrEmail();
            case 1:
                return u.getStatus();
            case 2:
                return u.isChatPaneOpened();
            case 3:
                return u.isVideoPaneOpened();
        }

        return null;
        //return data[row][col];
    }

    /*
     * JTable uses this method to determine the default renderer/ editor for
     * each cell. If we didn't implement this method, then the last column
     * would contain text ("true"/"false"), rather than a check box.
     */
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's editable.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        // Note that the data/cell address is constant,
        // no matter where the cell appears onscreen.
		/*
         * if (col < 2) { return false; } else { return true; }
         */
        return false;
    }

    /*
     * Don't need to implement this method unless your table's data can
     * change.
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        if (DEBUG) {
            System.out.println("Setting value at " + row + "," + col
                    + " to " + value + " (an instance of "
                    + value.getClass() + ")");
        }

        Contact u = data.get(row);
        if (col == 0) {
            u.setUsernameOrEmail((String) value);
        } else if (col == 1) {
            u.setStatus((Contact.Status) value);
        } else if (col == 2) {
            u.setChatPaneOpened((Boolean) value);
        } else if (col == 3) {
            u.setVideoPaneOpened((Boolean) value);
        } else {
            return;
        }

        //data[row][col] = value;
        fireTableCellUpdated(row, col);

        if (DEBUG) {
            System.out.println("New value of data:");
            printDebugData();
        }
    }

    private void printDebugData() {
        int numRows = getRowCount();
        int numCols = getColumnCount();

        for (int i = 0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            Contact c = data.get(i);
            System.out.println("Contact: " + c.toString());
            for (int j = 0; j < numCols; j++) {
                //System.out.print("  " + data[i][j]);
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

    // This is the listeners callback from fireTableCellUpdated
    // 
    @Override
    public void tableChanged(TableModelEvent e) {
        // TODO Auto-generated method stub
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel) e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);

        // Do something with the data...
    }

    public Contact getContact(int row) {
        return data.get(row);
    }

    @Override
    public void update(Observable o, Object arg) {
        OQueue q = (OQueue) o;
        Message m = (Message) q.peek();
        if (m == null) {
            return;
        }

        int messageCode = m.getCode();
        if (!Utils.messageIsRelevant(messageCode, this.getClass().getSimpleName())) {
            return;
        }

        q.remove(m);
        Contact contactIn = (Contact) m.getContent();

        switch (messageCode) {

            case CONTACT_STATUS_UPDATE:
                //HibernateUtil.updateContactStatus(contactIn);
                // fireTableRowsUpdated... ?
                break;

            case CONTACT_DELETED_ACK:
                // TODO:
                //HibernateUtil.deleteContact(contactIn.getId());
                // fireTableRowsDeleted... ?
                break;

            case CONTACT_ADDITION:
                // Set server generated cmsg_id
                //db.insert(contactIn);
                // fireTableRowsInserted... ?
                break;
        }
    }
}
