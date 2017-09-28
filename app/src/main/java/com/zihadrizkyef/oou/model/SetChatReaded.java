package com.zihadrizkyef.oou.model;

import com.google.gson.annotations.SerializedName;

public class SetChatReaded {

    @SerializedName("success")
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return
                "SetChatReaded{" +
                        "success = '" + success + '\'' +
                        "}";
    }
}