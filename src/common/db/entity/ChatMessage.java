package common.db.entity;

import common.pojos.Conventions;
import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable, Conventions {

    private static final long serialVersionUID = 1L;
    private Integer clientGenId;
    private Integer serverGenId;
    private String body;
    private Integer sourceUserId;
    private Integer targetUserId;
    private String sourceName;
    private String targetName;
    private Date timeSent;
    private Date timeByServer;
    private Date timeDelivered;
    private Status status;
    private Integer conversationId;

    public enum Status {

        BY_SOURCE, BY_SERVER, DELIVERED, DELIVERY_REPORTED
    }

    public ChatMessage() {
    }

    public Integer getClientGenId() {
        return clientGenId;
    }

    public void setClientGenId(Integer cgId) {
        this.clientGenId = cgId;
    }

    public Integer getServerGenId() {
        return serverGenId;
    }

    public void setServerGenId(Integer sgId) {
        this.serverGenId = sgId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public Date getTimeDelivered() {
        return timeDelivered;
    }

    public void setTimeDelivered(Date timeDelivered) {
        this.timeDelivered = timeDelivered;
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
}
