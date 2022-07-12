package com.josepvictorr.kasep.model;

import com.google.gson.annotations.SerializedName;

public class Author{

	@SerializedName("datePublished")
	private String datePublished;

	@SerializedName("user")
	private String user;

	public String getDatePublished(){
		return datePublished;
	}

	public String getUser(){
		return user;
	}
}