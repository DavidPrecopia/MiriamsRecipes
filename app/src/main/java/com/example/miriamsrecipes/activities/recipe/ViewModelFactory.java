package com.example.miriamsrecipes.activities.recipe;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.miriamsrecipes.datamodel.Recipe;

final class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
	
	private final Recipe recipe;
	
	ViewModelFactory(Recipe recipe) {
		super();
		this.recipe = recipe;
	}
	
	@NonNull
	@Override
	public SharedFragmentsViewModel create(@NonNull Class modelClass) {
		return new SharedFragmentsViewModel(recipe);
	}
}
