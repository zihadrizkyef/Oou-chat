package com.zihadrizkyef.oou.model;

import com.google.gson.annotations.SerializedName;

public class SendChat {

    @SerializedName("readed")
    private int readed;

    @SerializedName("chat_room_id")
    private int chatRoomId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id")
    private int id;

    @SerializedName("message")
    private String message;

    @SerializedName("sender_id")
    private int senderId;

    public int getReaded() {
        return readed;
    }

    public void setReaded(int readed) {
        this.readed = readed;
    }

    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    @Override
    public String toString() {
        return
                "SendChat{" +
                        "readed = '" + readed + '\'' +
                        ",chat_room_id = '" + chatRoomId + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",id = '" + id + '\'' +
                        ",message = '" + message + '\'' +
                        ",sender_id = '" + senderId + '\'' +
                        "}";
    }
}