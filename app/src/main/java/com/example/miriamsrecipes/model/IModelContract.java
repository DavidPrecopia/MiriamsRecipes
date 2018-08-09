package com.example.miriamsrecipes.model;

import android.arch.lifecycle.LiveData;

import com.example.miriamsrecipes.activities.main.RecipeInfo;
import com.example.miriamsrecipes.datamodel.Recipe;

import java.util.List;

public interface IModelContract {
	LiveData<List<RecipeInfo>> getAllRecipes();
	LiveData<Recipe> getSingleRecipe(int recipeId);
}
