package com.zihadrizkyef.oou.model;

import com.google.gson.annotations.SerializedName;

public class ChatRoom {

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("message")
	private String message;

	@SerializedName("is_group")
	private String isGroup;

	@SerializedName("image_url")
	private String imageUrl;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setIsGroup(String isGroup){
		this.isGroup = isGroup;
	}

	public String getIsGroup(){
		return isGroup;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	@Override
 	public String toString(){
		return 
			"ChatRoom{" +
			"name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",message = '" + message + '\'' +
			",is_group = '" + isGroup + '\'' +
			",image_url = '" + imageUrl + '\'' +
			"}";
		}
}