package common.db.entity;

import java.util.Date;

/**
 * Exists only in Client, it is a virtual wrapper entity
 *
 * For online ACRs, For offline ACRs, CMs, Files
 *
 * @author johny
 */
public class Notification {

    private Integer id;
    private Type type;
    private Status status;
    private Integer eventId;
    private String relatedUsername;
    private Date timeStamp;

    public enum Type {

        ACRR, ACRA, ACRDR, MISSED_CHAT, MISSED_CALL, FILE_SENT
    }

    // READ IF CLOSE BUTTON CLICKED
    public enum Status {

        UNREAD, READ, HANDLED, DELETED
    }

    public Notification() {
    }

    public Notification(Type type) {
        this();
        this.setType(type);
    }

    public Notification(Type type, AddContactRequest acrIn) {

        this.setType(type);
        this.setEventId(acrIn.getServerGenId());
        this.setStatus(Notification.Status.UNREAD);
        this.setTimeStamp(acrIn.getTimeByServer());

        if (type == Type.ACRR) {
            this.setRelatedUsername(acrIn.getQuesterName());
        } else if (type == Type.ACRA || type == Type.ACRDR) {
            this.setRelatedUsername(acrIn.getRecipientName());
        }
    }

    public Notification(Type type, FileTransfer fileIn) {

        this.setType(type);
        this.setEventId(fileIn.getId());
        this.setStatus(Notification.Status.UNREAD);
        this.setTimeStamp(fileIn.getTimeByServer());
        this.setRelatedUsername(fileIn.getSourceName());
    }

    public Notification(Type type, ChatMessage cmIn) {

        this.setType(type);
        this.setEventId(cmIn.getConversationId());
        this.setStatus(Notification.Status.UNREAD);
        this.setTimeStamp(cmIn.getTimeByServer());
        this.setRelatedUsername(cmIn.getSourceName());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getRelatedUsername() {
        return relatedUsername;
    }

    public void setRelatedUsername(String relatedUsername) {
        this.relatedUsername = relatedUsername;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
