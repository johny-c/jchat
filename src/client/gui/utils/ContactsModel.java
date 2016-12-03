package client.gui.utils;

import client.db.util.DB;
import client.networking.R;
import common.db.entity.UserAccount;
import common.db.entity.UserIcon;
import common.utils.Conventions;
import common.utils.Message;
import common.utils.OQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

public final class ContactsModel extends AbstractTableModel implements Observer, Conventions {

    private static final boolean DEBUG = true;
    private static final String[] columnNames = {"Icon", "Username", "Email", "Status"};
    private final List<UserAccount> contactsList;
    private final List<UserIcon> iconsList;

    public ContactsModel() {
        contactsList = new ArrayList();
        iconsList = new ArrayList();
        populate();
    }

    void populate() {
        for (UserAccount c : DB.getContacts(R.getUserAccount().getId())) {
            contactsList.add(c);
            UserIcon icon = DB.getUserIcon(c.getId());
            iconsList.add(icon);
        }
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return contactsList.size();
    }

    @Override
    public String getColumnName(int col
    ) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        UserAccount u = contactsList.get(row);

        switch (col) {
            case 0:
                return iconsList.get(row);
            case 1:
                return u.getUsername();
            case 2:
                return u.getEmail();
            case 3:
                return u.getStatus();
        }

        return null;
    }

    /*
     * JTable uses this method to determine the default renderer/ editor for
     * each cell. If we didn't implement this method, then the last column
     * would contain text ("true"/"false"), rather than a check box.
     */
    @Override
    public Class getColumnClass(int c
    ) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public void setValueAt(Object value, int row, int col
    ) {

        if (DEBUG && value != null) {
            R.log("Setting value at " + row + "," + col
                    + " to " + value + " (an instance of "
                    + value.getClass() + ")");
        }

        UserAccount c = contactsList.get(row);
        if (col == findColumn("Icon")) {
            iconsList.set(row, (UserIcon) value);
        } else if (col == findColumn("Username")) {
            c.setUsername((String) value);
        } else if (col == findColumn("Status")) {
            c.setStatus((UserAccount.Status) value);
        } else if (col == findColumn("Email")) {
            c.setEmail((String) value);
        } else {
            return;
        }

        //data[row][col] = value;
        fireTableCellUpdated(row, col); // calls tableChanged()

        if (DEBUG) {
            R.log("New value of data:");
            printDebugData();
        }
    }

    private void printDebugData() {
        int numRows = getRowCount();
        int numCols = getColumnCount();

        for (int i = 0; i < numRows; i++) {
            R.log("    row " + i + ":");
            UserAccount c = contactsList.get(i);
            R.log("UserAccount: " + c.toString());
            for (int j = 0; j < numCols; j++) {
                //R.log("  " + data[i][j]);
            }

        }
        R.log("--------------------------");
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

        UserAccount contactIn;
        UserIcon iconIn;
        int row, col;
        switch (m.getType()) {

            case CONTACT_UPDATE:
                contactIn = (UserAccount) m.getContent();
                // find row of by contact id in contactsList
                row = findRowByContactId(contactIn.getId());

                if (row != -1) {
                    setValueAt(contactIn.getUsername(), row, findColumn("Username"));
                    setValueAt(contactIn.getEmail(), row, findColumn("Email"));
                    setValueAt(contactIn.getStatus(), row, findColumn("Status"));
                    fireTableRowsUpdated(row, row);
                }
                break;

            case CONTACT_ICON_UPDATE:
                iconIn = (UserIcon) m.getContent();
                row = findRowByContactId(iconIn.getUacId());

                if (row != -1) {
                    setValueAt(iconIn, row, findColumn("Icon"));
                }
                break;

            case CONTACT_BROKEN:
                row = findRowByContactId((Integer) m.getContent());
                if (row != -1) {
                    contactsList.remove(row);
                    iconsList.remove(row);
                    fireTableRowsDeleted(row, row);
                }
                break;

            case CONTACT_ADDITION:
                contactIn = (UserAccount) m.getContent();
                contactsList.add(contactIn);

                UserIcon icon = DB.getUserIcon(contactIn.getId());
                iconsList.add(icon);
                row = contactsList.size() - 1;
                fireTableRowsInserted(row, row);
                break;
        }
    }

    private int findRowByContactId(Integer id) {
        int listSize = contactsList.size();
        for (int row = 0; row < listSize; row++) {
            UserAccount c = contactsList.get(row);
            if (c.getId() == null) {
                continue;
            }
            if (c.getId().equals(id)) {
                return row;
            }
        }

        return -1;
    }

    public UserAccount getContact(int row) {
        R.log("Returning contact at row " + row);
        return contactsList.get(row);
    }

    public UserIcon getContactIcon(int row) {
        return iconsList.get(row);
    }

    public boolean remove(UserAccount selectedContact) {
        int row = findRowByContactId(selectedContact.getId());
        if (row != -1) {
            contactsList.remove(row);
            iconsList.remove(row);
            fireTableRowsDeleted(row, row);
            return true;
        }

        JOptionPane.showMessageDialog(null, "Contact was not found in Model..");
        return false;
    }

}
