package com.zihadrizkyef.oou.model;

import com.google.gson.annotations.SerializedName;

public class AddFriend{

	@SerializedName("success")
	private Boolean success;

	public void setSuccess(Boolean success){
		this.success = success;
	}

	public Boolean getSuccess(){
		return success;
	}

	@Override
 	public String toString(){
		return 
			"AddFriend{" + 
			"success = '" + success + '\'' +
			"}";
		}
}