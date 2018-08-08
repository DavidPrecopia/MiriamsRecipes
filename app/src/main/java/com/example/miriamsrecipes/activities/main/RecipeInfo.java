package com.example.miriamsrecipes.activities.main;

import android.arch.persistence.room.ColumnInfo;

import com.example.miriamsrecipes.model.DatabaseContract;

public final class RecipeInfo {
	@ColumnInfo(name = DatabaseContract.COLUMN_RECIPE_ID)
	private int id;
	
	@ColumnInfo(name = DatabaseContract.COLUMN_NAME)
	private String name;
	
	@ColumnInfo(name = DatabaseContract.COLUMN_SERVINGS)
	private int servings;
	
	@ColumnInfo(name = DatabaseContract.COLUMN_IMAGE)
	private String image;
	
	
	public RecipeInfo(int id, String name, int servings, String image) {
		this.id = id;
		this.name = name;
		this.servings = servings;
		this.image = image;
	}
	
	
	int getId() {
		return id;
	}
	
	String getName() {
		return name;
	}
	
	int getServings() {
		return servings;
	}
	
	String getImage() {
		return image;
	}
}
