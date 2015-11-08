package com.example.lytuananh.demowonoloapp.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lytuananh on 11/8/15.
 */
public class Comment implements Serializable {
    private int count;
    private ArrayList<DataComment> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<DataComment> getData() {
        return data;
    }

    public void setData(ArrayList<DataComment> data) {
        this.data = data;
    }
}
