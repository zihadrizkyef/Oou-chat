package com.zihadrizkyef.oou.model;

import com.google.gson.annotations.SerializedName;

public class CreateChatRoom {

    @SerializedName("room_id")
    private int roomId;

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return
                "CreateChatRoom{" +
                        "roomId = '" + roomId + '\'' +
                        "}";
    }
}