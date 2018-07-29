package com.example.miriamsrecipes.datamodel;

import java.util.List;

public class Recipe{
	
	private int id;
	
	private String name;
	private int servings;
	private String image;
	
	private List<IngredientsItem> ingredients;
	private List<StepsItem> steps;
	
	
	public int getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public int getServings(){
		return servings;
	}
	
	public String getImage(){
		return image;
	}
	
	public List<IngredientsItem> getIngredients(){
		return ingredients;
	}

	public List<StepsItem> getSteps(){
		return steps;
	}
}