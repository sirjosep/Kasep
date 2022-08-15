package com.josepvictorr.kasep.item;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.josepvictorr.kasep.model.Author;

public class ResponseDetailItem {

	@SerializedName("servings")
	private String servings;

	@SerializedName("times")
	private String times;

	@SerializedName("ingredient")
	private List<String> ingredient;

	@SerializedName("thumb")
	private Object thumb;

	@SerializedName("author")
	private Author author;

	@SerializedName("step")
	private List<String> step;

	@SerializedName("title")
	private String title;

	@SerializedName("needItem")
	private List<NeedItemItem> needItem;

	@SerializedName("dificulty")
	private String dificulty;

	@SerializedName("desc")
	private String desc;

	public String getServings(){
		return servings;
	}

	public String getTimes(){
		return times;
	}

	public List<String> getIngredient(){
		return ingredient;
	}

	public Object getThumb(){
		return thumb;
	}

	public Author getAuthor(){
		return author;
	}

	public List<String> getStep(){
		return step;
	}

	public String getTitle(){
		return title;
	}

	public List<NeedItemItem> getNeedItem(){
		return needItem;
	}

	public String getDificulty(){
		return dificulty;
	}

	public String getDesc(){
		return desc;
	}
}