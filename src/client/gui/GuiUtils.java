/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.networking.R;
import common.db.entity.UserAccount;
import common.utils.Conventions;
import common.utils.Utils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;

/**
 *
 * @author johny
 */
public class GuiUtils implements Conventions {

    public static void stopAnimations(Object arg, final JLabel... LALs) {
        //final Message m = (Message) arg;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (JLabel label : LALs) {
                    label.setVisible(false);
                }
            }
        });
    }

    static void enableButtons(final JButton... buttons) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (JButton button : buttons) {
                    button.setEnabled(true);
                }
            }
        });
    }

    static void setStatus(final UserAccount.Status newStatus, final JButton userStatusButton, final JLabel userIconLabel) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                userStatusButton.setIcon(getIIcon("status/" + newStatus.toString().toLowerCase() + "24.png"));
                userStatusButton.setToolTipText(newStatus.toString().toLowerCase().replaceAll("_", " "));
                if (newStatus == UserAccount.Status.OFFLINE) {
                    userStatusButton.setText("");
                    userIconLabel.setIcon(GuiUtils.getDefaultUserIcon());
                } else if (newStatus == UserAccount.Status.ONLINE) {
                    userStatusButton.setText(R.getUserAccount().getUsername());
                }
            }
        });
    }

    static void writeToTray(final String content, final JTextPane trayArea) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int offset = trayArea.getStyledDocument().getLength();
                try {
                    trayArea.getStyledDocument().insertString(offset, content + "\n", null);
                } catch (BadLocationException ex) {
                    R.log(ex.toString());
                }
            }
        });
    }

    public static void display(final String string, final JTextPane textPane) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int offset = textPane.getStyledDocument().getLength();
                try {
                    textPane.getStyledDocument().insertString(offset, string, null);
                } catch (BadLocationException e) {
                    R.log(e.toString());
                }
            }
        });
    }

    public static ImageIcon getIIcon(String relativePath) {
        return new ImageIcon(Utils.class.getResource("/icons/" + relativePath));
    }

    public static Dimension getScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        return screenSize;
    }

    public static Dimension getHalfScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        return new Dimension(screenSize.width / 2, screenSize.height / 2);
    }

    public static Dimension getPrefferedFrameSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        return new Dimension(screenSize.width * 2 / 5, screenSize.height * 2 / 3);
    }

    public static Rectangle getPrefferedRectangle() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getPrefferedFrameSize();
        Rectangle preffered;
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;

        preffered = new Rectangle(x, y, frameSize.width, frameSize.height);
        return preffered;
    }

    public static Rectangle getCentralRectangle() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle central = new Rectangle(screenSize.width / 4, screenSize.height / 4, screenSize.width / 2, screenSize.height / 2);

        return central;
    }

    public static Dimension getUbuntuReducedScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenSize.height -= 100;
        screenSize.width -= 200;

        return screenSize;
    }

    static boolean showOnlineActionOnly(Component mf) {
        if (R.getUserAccount().getStatus() == UserAccount.Status.OFFLINE) {
            JOptionPane.showMessageDialog(mf,
                    "You have to log in to perform this action.",
                    "Not logged in",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    // Server file rejection
    static void showFileTransferRejectionDialog(final Component comp) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(comp,
                        "Your file cannot be sent, maybe it's too large.",
                        "File transfer aborted", JOptionPane.ERROR_MESSAGE);
            }

        });
    }

    public static Image getAppIcon() {
        URL url = GuiUtils.class.getResource("/icons/jchat_logo/app_icon.png");
        return Toolkit.getDefaultToolkit().createImage(url);
        //return new ImageIcon(url).getImage();
    }

    public static ImageIcon getRescaledIcon(byte[] data, int width, int height) {

        ImageIcon icon = new ImageIcon(data);
        Image img = icon.getImage();
        img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public static String getAnonymousUserIconPath() {
        return "/icons/user/anonymous_white40.png";

    }

    public static ImageIcon getDefaultUserIcon() {
        URL url = GuiUtils.class
                .getResource("/icons/user/anonymous_white40.png");
        return new ImageIcon(url);
    }

    static void showUserIconTooLargeDialog(Component c, int length) {
        String msg = "The image you selected is too large, \n"
                + "even when compressed. (" + length + " bytes)\n"
                + "Please select an image file up to " + MAX_USER_ICON_SIZE + " bytes.";
        JOptionPane.showMessageDialog(c,
                msg,
                "Image file size too large",
                JOptionPane.ERROR_MESSAGE);
    }

    static void showTooManyConvParticipants(Component aThis, int participantsCounter) {
        String msg = "Files can only be sent to one participant at a time.\n"
                + "This conversation has " + participantsCounter + " participants.\n"
                + "Open a new conversation just with the contact "
                + "you want to send files to.";
        JOptionPane.showMessageDialog(aThis,
                msg,
                "Too many participants !",
                JOptionPane.ERROR_MESSAGE);
    }

    static void showFileSizeTooLarge(Component aThis, long size) {
        String s;
        float sizeMB = (float) (size / (1024 * 1024));
        int maxMB = (int) (MAX_FILE_TRANSFER_SIZE / (1024 * 1024));
        s = "The files you selected have a total size of " + sizeMB + " MB.\n";
        s += "JChat can send up to " + maxMB + " MB.\n";
        s += "Please, select less or smaller files.";
        JOptionPane.showMessageDialog(aThis,
                s,
                "File size limit reached !",
                JOptionPane.ERROR_MESSAGE);
    }

    static void showFileUploadInProgress(Component aThis) {
        String msg = "Wait, there is a file upload already in process !";
        JOptionPane.showMessageDialog(aThis,
                msg,
                "File upload in process",
                JOptionPane.ERROR_MESSAGE);
    }

    static boolean showDialogBeforeUpdate(Component comp) {
        String msg = "Make sure you don't need any open conversations "
                + "or files that are being transferred.\n\n "
                + "Proceed with JChat update?";
        int choice = JOptionPane.showConfirmDialog(comp,
                msg,
                "Update confirmation",
                JOptionPane.YES_NO_CANCEL_OPTION);

        return choice == JOptionPane.YES_OPTION;
    }

    public static class ImageFilter extends FileFilter {

        //Accept all directories and all gif, jpg, tiff, or png files.
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = Utils.getFileExtension(f);
            if (extension != null) {
                if (extension.equals(Utils.tiff)
                        || extension.equals(Utils.tif)
                        || extension.equals(Utils.gif)
                        || extension.equals(Utils.jpeg)
                        || extension.equals(Utils.jpg)
                        || extension.equals(Utils.png)) {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        //The description of this filter
        @Override
        public String getDescription() {
            return "Image files";
        }
    }

}
