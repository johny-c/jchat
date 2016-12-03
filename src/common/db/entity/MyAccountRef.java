package common.db.entity;

import java.io.Serializable;

public class MyAccountRef implements Serializable {

    private Integer id;
    private Integer accountId;

    public MyAccountRef() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

}
