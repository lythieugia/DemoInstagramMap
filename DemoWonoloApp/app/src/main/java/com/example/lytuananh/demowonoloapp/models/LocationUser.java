package com.example.lytuananh.demowonoloapp.models;

import java.io.Serializable;

/**
 * Created by lytuananh on 11/8/15.
 */
public class LocationUser implements Serializable {

    private String id;
    private String username;
    private String profile_picture;
    private String full_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}
