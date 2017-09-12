package com.zihadrizkyef.oou.model;

import com.google.gson.annotations.SerializedName;

public class EditProfile {

    @SerializedName("success")
    private Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return
                "EditProfile{" +
                        "success = '" + success + '\'' +
                        "}";
    }
}