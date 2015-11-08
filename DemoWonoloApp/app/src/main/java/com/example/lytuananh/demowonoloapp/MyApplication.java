package com.example.lytuananh.demowonoloapp;

import android.app.Application;

import com.example.lytuananh.demowonoloapp.instagram.InstagramApp;

/**
 * Created by lytuananh on 11/8/15.
 */
public class MyApplication extends Application {
    private InstagramApp mInstagramMap;


    public InstagramApp getInstgramMap() {
        return mInstagramMap;
    }

    public void setInstgramMap(InstagramApp pInstagramMap) {
        this.mInstagramMap = pInstagramMap;
    }
}

