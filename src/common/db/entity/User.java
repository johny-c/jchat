package common.db.entity;

import common.pojos.Utils;
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String username;
    private String email;
    private Date regDate;
    private Date birthDate;
    private transient String password;
    private Status status;

    public enum Status {

        OFFLINE, ONLINE, AWAY, BUSY, APPEAR_OFFLINE
    };

    public User() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsernameOrEmail() {
        if (username != null) {
            if (this.getUsername().isEmpty()) {
                return this.getEmail();
            } else {
                return this.getUsername();
            }
        } else {
            return "a";
        }
    }

    public void setUsernameOrEmail(String string) {
        if (Utils.isValidEmailAddress(string)) {
            setEmail(string);
        } else if (string != null) {
            setUsername(string);
        }
    }
}
