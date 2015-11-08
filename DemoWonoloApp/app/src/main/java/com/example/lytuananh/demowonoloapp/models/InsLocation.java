package com.example.lytuananh.demowonoloapp.models;

import java.io.Serializable;

/**
 * Created by lytuananh on 11/8/15.
 */
public class InsLocation implements Serializable {

    private Double latitude;
    private String name;
    private Double longitude;
    private int id;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
