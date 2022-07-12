package com.josepvictorr.kasep.model;

import com.google.gson.annotations.SerializedName;
import com.josepvictorr.kasep.item.ResponseDetailItem;

public class ResponseDetailResep{

	@SerializedName("method")
	private String method;

	@SerializedName("results")
	private ResponseDetailItem responseDetailItem;

	@SerializedName("status")
	private boolean status;

	public String getMethod(){
		return method;
	}

	public ResponseDetailItem getResults(){
		return responseDetailItem;
	}

	public boolean isStatus(){
		return status;
	}
}