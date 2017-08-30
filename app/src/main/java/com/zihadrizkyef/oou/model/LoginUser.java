package com.zihadrizkyef.oou.model;

import com.google.gson.annotations.SerializedName;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 02/08/17.
 */

public class LoginUser {
    @SerializedName("id")
    private Integer id;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("name")
    private String name;

    @SerializedName("imageurl")
    private String imageurl;

    @SerializedName("bio")
    private String bio;

    @SerializedName("frbs_notif_id")
    private String frbsNotifId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFrbsNotifId() {
        return frbsNotifId;
    }

    public void setFrbsNotifId(String frbsNotifId) {
        this.frbsNotifId = frbsNotifId;
    }

}