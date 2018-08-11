package com.example.miriamsrecipes.datamodel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.miriamsrecipes.model.DatabaseContract;

import java.util.List;

@Entity(tableName = DatabaseContract.TABLE_NAME_RECIPE)
public class Recipe {
	
	@PrimaryKey
	@ColumnInfo(name = DatabaseContract.COLUMN_RECIPE_ID)
	private final int id;
	
	@ColumnInfo(name = DatabaseContract.COLUMN_NAME)
	private final String name;
	
	@ColumnInfo(name = DatabaseContract.COLUMN_SERVINGS)
	private final int servings;
	
	@ColumnInfo(name = DatabaseContract.COLUMN_IMAGE)
	private final String image;
	
	
	@TypeConverters(IngredientsItemTypeConverter.class)
	private final List<IngredientsItem> ingredients;
	
	@TypeConverters(StepItemTypeConverter.class)
	private final List<StepItem> steps;
	
	
	public Recipe(int id, String name, int servings, String image, List<IngredientsItem> ingredients, List<StepItem> steps) {
		this.id = id;
		this.name = name;
		this.servings = servings;
		this.image = image;
		this.ingredients = ingredients;
		this.steps = steps;
	}
	
	
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

	public List<StepItem> getSteps(){
		return steps;
	}
}