package com.zihadrizkyef.oou.model;

import com.google.gson.annotations.SerializedName;

public class ChangeProfilePicture {

    @SerializedName("image_url")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return
                "ChangeProfilePicture{" +
                        "image_url = '" + imageUrl + '\'' +
                        "}";
    }
}