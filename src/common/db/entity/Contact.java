package common.db.entity;

import java.io.Serializable;

public class Contact extends User implements Serializable {

    private boolean chatPaneOpened = false;
    private boolean videoPaneOpened = false;
    private static final long serialVersionUID = 1L;

    public Contact() {
    }

    public boolean isChatPaneOpened() {
        return chatPaneOpened;
    }

    public void setChatPaneOpened(boolean chatPaneOpened) {
        this.chatPaneOpened = chatPaneOpened;
    }

    public boolean isVideoPaneOpened() {
        return videoPaneOpened;
    }

    public void setVideoPaneOpened(boolean videoPaneOpened) {
        this.videoPaneOpened = videoPaneOpened;
    }
}
