package com.example.lytuananh.demowonoloapp.models;

import java.io.Serializable;

/**
 * Created by lytuananh on 11/8/15.
 */
public class ImageResolution implements Serializable {


    private String url;
    private String width;
    private String height;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

}
