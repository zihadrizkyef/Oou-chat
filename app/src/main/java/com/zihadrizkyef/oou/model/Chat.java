package com.zihadrizkyef.oou.model;

import com.google.gson.annotations.SerializedName;

public class Chat {

	@SerializedName("readed")
	private int readed;

	@SerializedName("image_url")
	private String imageUrl;

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

	public Chat(int id, String message, int senderId, int chatRoomId, String imageUrl, int readed, String createdAt) {
		this.id = id;
		this.message = message;
		this.senderId = senderId;
		this.chatRoomId = chatRoomId;
		this.imageUrl = imageUrl;
		this.readed = readed;
		this.createdAt = createdAt;
	}

	public int getReaded() {
		return readed;
	}

	public void setReaded(int readed) {
		this.readed = readed;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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
				"Chat{" +
						"readed = '" + readed + '\'' +
						",image_url = '" + imageUrl + '\'' +
						",chat_room_id = '" + chatRoomId + '\'' +
						",created_at = '" + createdAt + '\'' +
						",id = '" + id + '\'' +
						",message = '" + message + '\'' +
						",sender_id = '" + senderId + '\'' +
						"}";
	}
}