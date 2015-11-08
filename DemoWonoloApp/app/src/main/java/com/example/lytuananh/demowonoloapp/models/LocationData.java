package com.example.lytuananh.demowonoloapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by lytuananh on 11/8/15.
 */
public class LocationData implements Serializable {
    private Objects attribution;
    private String type;
    private ArrayList<UsersInPhoto> users_in_photo;
    private String filter;
    private ArrayList<String> tags;
    private Comment comments;
    private Caption caption;
    private LocationLike likes;
    private String link;
    private LocationUser user;
    private String created_time;
    private HashMap<String,ImageResolution> images;
    private InsLocation location;
    private Boolean user_has_liked;
    private String id;
    private HashMap<String,VideoBandwidth> videos;

    public Objects getAttribution() {
        return attribution;
    }

    public void setAttribution(Objects attribution) {
        this.attribution = attribution;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<UsersInPhoto> getUsers_in_photo() {
        return users_in_photo;
    }

    public void setUsers_in_photo(ArrayList<UsersInPhoto> users_in_photo) {
        this.users_in_photo = users_in_photo;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public Comment getComments() {
        return comments;
    }

    public void setComments(Comment comments) {
        this.comments = comments;
    }

    public Caption getCaption() {
        return caption;
    }

    public void setCaption(Caption caption) {
        this.caption = caption;
    }

    public LocationLike getLikes() {
        return likes;
    }

    public void setLikes(LocationLike likes) {
        this.likes = likes;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocationUser getUser() {
        return user;
    }

    public void setUser(LocationUser user) {
        this.user = user;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public HashMap<String, ImageResolution> getImages() {
        return images;
    }

    public void setImages(HashMap<String, ImageResolution> images) {
        this.images = images;
    }

    public InsLocation getLocation() {
        return location;
    }

    public void setLocation(InsLocation location) {
        this.location = location;
    }

    public Boolean getUser_has_liked() {
        return user_has_liked;
    }

    public void setUser_has_liked(Boolean user_has_liked) {
        this.user_has_liked = user_has_liked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, VideoBandwidth> getVideos() {
        return videos;
    }

    public void setVideos(HashMap<String, VideoBandwidth> videos) {
        this.videos = videos;
    }
}
