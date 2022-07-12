package com.josepvictorr.kasep.item;

import com.google.gson.annotations.SerializedName;

public class ResponseResepItem {

	@SerializedName("times")
	private String times;

	@SerializedName("thumb")
	private String thumb;

	@SerializedName("portion")
	private String portion;

	@SerializedName("title")
	private String title;

	@SerializedName("key")
	private String key;

	@SerializedName("dificulty")
	private String dificulty;

	public String getTimes(){
		return times;
	}

	public String getThumb(){
		return thumb;
	}

	public String getPortion(){
		return portion;
	}

	public String getTitle(){
		return title;
	}

	public String getKey(){
		return key;
	}

	public String getDificulty(){
		return dificulty;
	}
}