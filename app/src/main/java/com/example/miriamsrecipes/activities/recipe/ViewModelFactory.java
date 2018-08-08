package com.example.miriamsrecipes.activities.recipe;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

final class ViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
	
	private final Application application;
	private final int recipeId;
	
	ViewModelFactory(@NonNull Application application, int recipeId) {
		super(application);
		this.application = application;
		this.recipeId = recipeId;
	}
	
	@NonNull
	@Override
	public RecipeViewModel create(@NonNull Class modelClass) {
		return new RecipeViewModel(application, recipeId);
	}
}
