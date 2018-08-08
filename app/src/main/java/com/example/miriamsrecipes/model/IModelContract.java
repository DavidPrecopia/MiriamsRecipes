package com.example.miriamsrecipes.model;

import android.arch.lifecycle.LiveData;

import com.example.miriamsrecipes.activities.main.RecipeInfo;
import com.example.miriamsrecipes.datamodel.Recipe;

import java.util.List;

import io.reactivex.Single;

public interface IModelContract {
	LiveData<List<RecipeInfo>> getAllRecipes();
	Single<Recipe> getSingleRecipe(int recipeId);
}
