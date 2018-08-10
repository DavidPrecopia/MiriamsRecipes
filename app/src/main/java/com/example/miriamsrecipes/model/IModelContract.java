package com.example.miriamsrecipes.model;

import android.arch.lifecycle.LiveData;

import com.example.miriamsrecipes.datamodel.Recipe;

import java.util.List;

public interface IModelContract {
	LiveData<List<Recipe>> observeAllRecipes();
	LiveData<Recipe> getSingleRecipe(int recipeId);
}
