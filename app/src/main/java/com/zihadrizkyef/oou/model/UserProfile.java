package com.zihadrizkyef.oou.model;

import com.google.gson.annotations.SerializedName;

public class UserProfile{

	@SerializedName("imageUrl")
	private String imageUrl;

	@SerializedName("name")
	private String name;

	@SerializedName("bio")
	private String bio;

	@SerializedName("id")
	private int id;

	@SerializedName("username")
	private String username;

	public UserProfile(int id, String username, String name, String bio, String imageUrl) {
		this.id = id;
		this.username = username;
		this.name = name;
		this.bio = bio;
		this.imageUrl = imageUrl;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setBio(String bio){
		this.bio = bio;
	}

	public String getBio(){
		return bio;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	@Override
 	public String toString(){
		return 
			"UserProfile{" + 
			"imageUrl = '" + imageUrl + '\'' +
			",name = '" + name + '\'' + 
			",bio = '" + bio + '\'' + 
			",id = '" + id + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}