package com.example.soil.biogi.model;

import java.io.Serializable;

/**
 * Created by soil on 2016/5/17.
 */
public class User implements Serializable {
    String id,name, created_at,address, sex, birthday, phone, cellphone, email ;

    public User() {
    }

    public User(String id, String name ,String email) {
        this.id = id;
        this.name = name;
        this.email = email;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}