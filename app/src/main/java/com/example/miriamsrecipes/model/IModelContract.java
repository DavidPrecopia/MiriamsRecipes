package com.example.miriamsrecipes.model;

import com.example.miriamsrecipes.activities.main.RecipeInfo;
import com.example.miriamsrecipes.datamodel.Recipe;

import java.util.List;

import io.reactivex.Single;

public interface IModelContract {
	Single<List<RecipeInfo>> getAllRecipes();
	
	Single<Recipe> getSingleRecipe(int recipeId);
}
