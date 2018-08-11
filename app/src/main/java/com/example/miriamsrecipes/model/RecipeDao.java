package com.example.miriamsrecipes.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.miriamsrecipes.datamodel.Recipe;

import java.util.List;

import io.reactivex.Maybe;

import static com.example.miriamsrecipes.model.DatabaseContract.COLUMN_RECIPE_ID;
import static com.example.miriamsrecipes.model.DatabaseContract.TABLE_NAME_RECIPE;

@Dao
interface RecipeDao {
	@Query("SELECT * FROM " + TABLE_NAME_RECIPE)
	Maybe<List<Recipe>> getAllRecipes();
	
	@Query("SELECT * FROM " + TABLE_NAME_RECIPE + " WHERE " + COLUMN_RECIPE_ID + " = :recipeId")
	LiveData<Recipe> getSingleRecipe(int recipeId);
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	Long[] insertRecipe(List<Recipe> recipeList);
}
