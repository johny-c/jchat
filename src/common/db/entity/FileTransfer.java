package common.db.entity;

import java.io.Serializable;
import java.util.Date;

public class FileTransfer implements Serializable {

    private Integer id;
    private Integer sourceUserId;
    private Integer targetUserId;
    private Integer fileSize;
    private String fileName;
    private String sourceName;
    private String targetName;
    private String senderPath;
    private String serverPath;
    private String receiverPath;
    private Date timeSent;
    private Date timeByServer;
    private Date timeNotified;
    private Date timeDownloaded;
    private Status status;
    private Integer conversationId;
    private Integer notificationId;

    public enum Status {

        BY_SOURCE, BY_SERVER, RECIPIENT_NOTIFIED, FILE_DOWNLOADED, DOWNLOAD_REPORTED
    }

    public FileTransfer() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(Integer sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public Integer getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Integer targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getSenderPath() {
        return senderPath;
    }

    public void setSenderPath(String senderPath) {
        this.senderPath = senderPath;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getReceiverPath() {
        return receiverPath;
    }

    public void setReceiverPath(String receiverPath) {
        this.receiverPath = receiverPath;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }

    public Date getTimeByServer() {
        return timeByServer;
    }

    public void setTimeByServer(Date timeByServer) {
        this.timeByServer = timeByServer;
    }

    public Date getTimeNotified() {
        return timeNotified;
    }

    public void setTimeNotified(Date timeNotified) {
        this.timeNotified = timeNotified;
    }

    public Date getTimeDownloaded() {
        return timeDownloaded;
    }

    public void setTimeDownloaded(Date timeDownloaded) {
        this.timeDownloaded = timeDownloaded;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getConversationId() {
        return conversationId;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }
}
