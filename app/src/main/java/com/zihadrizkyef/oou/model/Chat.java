package com.zihadrizkyef.oou.model;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 16/07/17.
 */

public class Chat {
    private int profile_id;
    private String name;
    private String photoUrl;
    private int photoVersion;
    private String text;

    public Chat(int profile_id, String name, String photoUrl, int photoVersion, String text) {
        this.profile_id = profile_id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.photoVersion = photoVersion;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getPhotoVersion() {
        return photoVersion;
    }

    public int getProfile_id() {
        return profile_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}