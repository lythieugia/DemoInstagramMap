package com.example.lytuananh.demowonoloapp.models;

import java.io.Serializable;

/**
 * Created by lytuananh on 11/8/15.
 */
public class DataComment implements Serializable {
    private  String created_time;
    private String text;
    private LocationUser from;
    private  String id;

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocationUser getFrom() {
        return from;
    }

    public void setFrom(LocationUser from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
