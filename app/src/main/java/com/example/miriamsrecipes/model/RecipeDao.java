package com.example.miriamsrecipes.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.miriamsrecipes.activities.main.RecipeInfo;
import com.example.miriamsrecipes.datamodel.Recipe;

import java.util.List;

import io.reactivex.Maybe;

import static com.example.miriamsrecipes.model.DatabaseContract.COLUMN_IMAGE;
import static com.example.miriamsrecipes.model.DatabaseContract.COLUMN_NAME;
import static com.example.miriamsrecipes.model.DatabaseContract.COLUMN_RECIPE_ID;
import static com.example.miriamsrecipes.model.DatabaseContract.COLUMN_SERVINGS;
import static com.example.miriamsrecipes.model.DatabaseContract.TABLE_NAME_RECIPE;

@Dao
interface RecipeDao {
	@Query("SELECT " +
			COLUMN_RECIPE_ID + ", " +
			COLUMN_NAME + ", " +
			COLUMN_SERVINGS + ", " +
			COLUMN_IMAGE +
			" FROM " + TABLE_NAME_RECIPE)
	LiveData<List<RecipeInfo>> getAllRecipes();
	
	@Query("SELECT * FROM " + TABLE_NAME_RECIPE + " WHERE " + COLUMN_RECIPE_ID + " = :recipeId")
	LiveData<Recipe> getSingleRecipe(int recipeId);


	/**
	 * A simple query to determine whether or not
	 * the database is empty.
	 *
	 * @return the number of recipes in the database.
	 * If 0, the database is empty
	 */
	@Query("SELECT " + COLUMN_RECIPE_ID + " FROM " + TABLE_NAME_RECIPE + " LIMIT 1")
	Maybe<Integer> checkDatabase();
	
	@Insert
	List<Long> popularDatabase(List<Recipe> recipeList);
}
