package client.gui.utils;

import client.gui.GuiUtils;
import common.db.entity.UserAccount;
import common.db.entity.UserIcon;
import common.utils.Conventions;
import static common.utils.Conventions.FONT_UBUNTU_BOLD_15;
import common.utils.Utils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.net.URL;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public interface TableRenderers {

    static class IconRenderer extends DefaultTableCellRenderer implements Conventions {

        ImageIcon imgIcon;

        public IconRenderer() {
            super();
        }

        @Override
        public void setValue(Object value) {
            UserIcon icon = (UserIcon) value;

            if (icon == null) {
                imgIcon = GuiUtils.getDefaultUserIcon();
            } else {
                imgIcon = new ImageIcon(icon.getIconData());
            }
            setIcon(imgIcon);
        }
    }

    static class UsernameRenderer extends DefaultTableCellRenderer implements Conventions {

        private final static Font FONT = FONT_UBUNTU_BOLD_15;
        private final static Color COLOR = Color.DARK_GRAY;

        public UsernameRenderer() {
            super();
        }

        @Override
        public void setValue(Object value) {
            setFont(FONT);
            setForeground(COLOR);
            //setBackground(BG_COLOR);
            setHorizontalAlignment(CENTER);
            setText((value == null) ? "" : value.toString());
            setToolTipText("username");
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // makes grid column line disappear
        }
    }

    static class EmailRenderer extends DefaultTableCellRenderer implements Conventions {

        private final static Font FONT = FONT_UBUNTU_PLAIN_14;
        private final static Color COLOR = Color.DARK_GRAY;

        public EmailRenderer() {
            super();
        }

        @Override
        public void setValue(Object value) {
            setFont(FONT);
            setForeground(COLOR);
            //setBackground(BG_COLOR);
            setHorizontalAlignment(LEFT);
            setText((value == null) ? "" : value.toString());
            setToolTipText("email");
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // makes grid column line disappear
        }
    }

    static class StatusRenderer extends DefaultTableCellRenderer implements Conventions {

        public StatusRenderer() {
            super();
        }

        @Override
        public void setValue(Object value) {
            String status;
            UserAccount.Status statusValue = (UserAccount.Status) value;

            switch (statusValue) {
                case ONLINE:
                    status = "online";
                    break;
                case AWAY:
                    status = "away";
                    break;
                case BUSY:
                    status = "busy";
                    break;
                case OFFLINE:
                case APPEAR_OFFLINE:
                default:
                    status = "offline";
            }

            URL iconPath = Utils.getIcon("status/" + status + "16.png");
            setIcon(new ImageIcon(iconPath));
            setHorizontalAlignment(LEFT);
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // makes grid column line disappear
            setText(status);
            setToolTipText("status");
        }
    }

    static class AgeRenderer extends DefaultTableCellRenderer implements Conventions {

        private final static Font FONT = FONT_UBUNTU_BOLD_15;
        private final static Color COLOR = Color.DARK_GRAY;

        public AgeRenderer() {
            super();
        }

        @Override
        public void setValue(Object value) {
            setFont(FONT);
            setForeground(COLOR);
            //setBackground(BG_COLOR);
            setHorizontalAlignment(CENTER);
            if (value == null) {
                setText("");
            } else {
                int age = Utils.birthdateToAge((Date) value);
                setText(age + "");
            }
            setToolTipText("age");
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // makes grid column line disappear
        }
    }

    static class ChatButtonRenderer extends JButton implements TableCellRenderer, Conventions {

        public ChatButtonRenderer() {
            super();
            super.setBorderPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            boolean isChatTabOpened = (Boolean) value;
            setEnabled(!isChatTabOpened);

            URL iconPath = null;
            if (table.getColumnName(column).equals("ChatButton")) {
                iconPath = Utils.getIcon("text_chat/goldenCloud/comments24.png");
            } else if (table.getColumnName(column).equals("VideoButton")) {
                iconPath = Utils.getIcon("video_chat/camera-red24.png");
            }
            setIcon(new ImageIcon(iconPath));
            return this;
        }
    }

}
