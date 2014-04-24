package common.db.entity;

import java.util.HashSet;
import java.util.Set;

public class Participant extends User implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Integer contactId;
    private Set<Conversation> conversations = new HashSet<>();

    public Participant() {
    }

    public Integer getContactId() {
        return this.contactId;
    }

    public void setContactId(Integer PContactId) {
        this.contactId = PContactId;
    }

    public Set<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(Set<Conversation> conversations) {
        this.conversations = conversations;
    }
}
