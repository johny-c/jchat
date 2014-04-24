package feature_testing.server;

import common.pojos.Message;

public class InterMessage {

    private long sourceUserId;
    private long targetUserId;
    private Message message;

    long getSourceUserId() {
        return sourceUserId;
    }

    void setSourceUserId(long senderId) {
        this.sourceUserId = senderId;
    }

    long getTargetUserId() {
        return targetUserId;
    }

    void setTargetUserId(long recipientId) {
        this.targetUserId = recipientId;
    }

    Message getMessage() {
        return message;
    }

    void setMessage(Message message) {
        this.message = message;
    }
}
