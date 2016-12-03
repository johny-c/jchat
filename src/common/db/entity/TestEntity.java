/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.db.entity;

import java.io.Serializable;

/**
 *
 * @author johny
 */
public class TestEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private Status status;

    public enum Status {

        FIRST, SECOND, THIRD
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Status getStatus() {
        return status;
    }


    public void setStatus(Status status) {
        this.status = status;
    }

}

