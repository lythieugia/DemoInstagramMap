package com.example.lytuananh.demowonoloapp.models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by lytuananh on 11/8/15.
 */
public class Caption implements Serializable {

    private String created_time;
    private String text;
    private HashMap<String,String> from;
    private String id;



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public HashMap<String, String> getFrom() {
        return from;
    }

    public void setFrom(HashMap<String, String> from) {
        this.from = from;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
