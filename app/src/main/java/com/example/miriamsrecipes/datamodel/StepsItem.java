package com.example.miriamsrecipes.datamodel;

public class StepsItem{
	
	private int id;
	
	private String shortDescription;
	private String description;
	
	private String videoURL;
	private String thumbnailURL;
	
	
	public int getId(){
		return id;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getShortDescription(){
		return shortDescription;
	}
	
	public String getVideoURL(){
		return videoURL;
	}

	public String getThumbnailURL(){
		return thumbnailURL;
	}
}