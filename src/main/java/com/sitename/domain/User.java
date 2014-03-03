package com.sitename.domain;

import javax.persistence.Entity;

@Entity
public class User extends AbstractEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
