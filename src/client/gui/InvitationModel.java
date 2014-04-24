package client.gui;

import client.pojos.R;
import common.pojos.Conventions;
import common.db.entity.Contact;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

class InvitationModel extends AbstractTableModel implements TableModelListener, Observer, Conventions {

    private String[] columnNames = {"Username", "Status", "Invite"};
    private List<Contact> data;

    public InvitationModel() {
        List<Object> objects = R.getDb().select(Contact.class);
        for (Object o : objects) {
            Contact c = (Contact) o;
            data.add(c);
        }
        addTableModelListener(this);
        R.getImh().subscribe(this);
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
        Contact u = data.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return u.getUsernameOrEmail();
            case 1:
                return u.getStatus();
            case 2:
                return u.isChatPaneOpened();
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

    public Contact getContact(int row) {
        return data.get(row);
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
