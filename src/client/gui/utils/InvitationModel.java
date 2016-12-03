package client.gui.utils;

import client.db.util.DB;
import common.db.entity.UserAccount;
import common.utils.Conventions;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class InvitationModel extends AbstractTableModel implements TableModelListener, Conventions {

    private final String[] columnNames = {"Username", "Status", "Invite"};
    private List<UserAccount> data;
    private DB db; // Create cursor connection

    public InvitationModel() {
        List<Object> objects = db.select(UserAccount.class);
        for (Object o : objects) {
            UserAccount c = (UserAccount) o;
            data.add(c);
        }
        addTableModelListener(this);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        UserAccount u = data.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return u.getUsername();
            case 1:
                return u.getStatus();
        }

        return null;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's editable.
     */
    public boolean isCellEditable(int row, int col) {
        // Note that the data/cell address is constant,
        // no matter where the cell appears onscreen.

        if (col < 2) {
            return false;
        } else {
            return true;
        }
    }

    public UserAccount getContact(int row) {
        return data.get(row);
    }

}
