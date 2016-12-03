/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

/**
 *
 * @author johny
 */


public class PopupMenuListener extends MouseAdapter {

    private final JPopupMenu popup;


    public PopupMenuListener(JPopupMenu popupMenu) {
        popup = popupMenu;
    }


    @Override
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }


    @Override
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

