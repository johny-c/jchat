package common.db.entity;

public class UserContact implements java.io.Serializable {

    private Integer id;
    private Integer userId;
    private Integer contactId;
    private Boolean status; // Status: PENDING, CARRIED_OUT

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId1) {
        this.userId = userId1;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer userId2) {
        this.contactId = userId2;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean received) {
        this.status = received;
    }
}
