package com.zihadrizkyef.oou.model;

import com.google.gson.annotations.SerializedName;

public class CountAllUnreaded {

    @SerializedName("unreaded")
    private int unreaded;

    public int getUnreaded() {
        return unreaded;
    }

    public void setUnreaded(int unreaded) {
        this.unreaded = unreaded;
    }

    @Override
    public String toString() {
        return
                "CountAllUnreaded{" +
                        "unreaded = '" + unreaded + '\'' +
                        "}";
    }
}