package com.example.miriamsrecipes.model;

public final class DatabaseContract {
	private DatabaseContract() {
	}
	
	static final String RECIPE_DATABASE_NAME = "recipe_database";
	
	
	public static final String TABLE_NAME_RECIPE = "recipe";
	
	public static final String COLUMN_RECIPE_ID = "recipe_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_SERVINGS = "servings";
	public static final String COLUMN_IMAGE = "image";
}
