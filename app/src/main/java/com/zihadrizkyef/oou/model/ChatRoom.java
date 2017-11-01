package com.zihadrizkyef.oou.model;

import com.google.gson.annotations.SerializedName;

public class ChatRoom {

    @SerializedName("last_activity")
    private String lastActivity;

    @SerializedName("image_url")
    private String imageUrl;

	@SerializedName("name")
	private String name;

    @SerializedName("unreaded_message")
    private int unreadedMessage;

	@SerializedName("id")
	private int id;

	@SerializedName("message")
	private String message;

	@SerializedName("is_group")
	private String isGroup;

    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

	public String getName(){
		return name;
	}

    public void setName(String name) {
        this.name = name;
    }

    public int getUnreadedMessage() {
        return unreadedMessage;
    }

    public void setUnreadedMessage(int unreadedMessage) {
        this.unreadedMessage = unreadedMessage;
    }

	public int getId(){
		return id;
	}

    public void setId(int id) {
        this.id = id;
    }

	public String getMessage(){
		return message;
	}

    public void setMessage(String message) {
        this.message = message;
    }

	public String getIsGroup(){
		return isGroup;
	}

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

	@Override
 	public String toString(){
		return
                "DBChatRoom{" +
                        "last_activity = '" + lastActivity + '\'' +
                        ",image_url = '" + imageUrl + '\'' +
                        ",name = '" + name + '\'' +
                        ",unreadedMessage = '" + unreadedMessage + '\'' +
                        ",id = '" + id + '\'' +
                        ",message = '" + message + '\'' +
                        ",is_group = '" + isGroup + '\'' +
                        "}";
		}
}