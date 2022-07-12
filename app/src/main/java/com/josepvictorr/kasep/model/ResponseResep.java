package com.josepvictorr.kasep.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.josepvictorr.kasep.item.ResponseResepItem;

public class ResponseResep{

	@SerializedName("method")
	private String method;

	@SerializedName("results")
	private List<ResponseResepItem> results;

	@SerializedName("status")
	private Boolean status;

	public String getMethod(){
		return method;
	}

	public List<ResponseResepItem> getResults(){
		return results;
	}

	public Boolean isStatus(){
		return status;
	}
}