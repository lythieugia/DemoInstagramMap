package com.example.lytuananh.demowonoloapp.models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by lytuananh on 11/8/15.
 */
public class UsersInPhoto implements Serializable {
    private HashMap<String,String> position;
    private LocationUser user;

    public HashMap<String, String> getPosition() {
        return position;
    }

    public void setPosition(HashMap<String, String> position) {
        this.position = position;
    }

    public LocationUser getUser() {
        return user;
    }

    public void setUser(LocationUser user) {
        this.user = user;
    }
}
