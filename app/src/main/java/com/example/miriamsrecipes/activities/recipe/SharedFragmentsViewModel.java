package com.example.miriamsrecipes.activities.recipe;

import android.arch.lifecycle.ViewModel;

import com.example.miriamsrecipes.datamodel.Recipe;

final class SharedFragmentsViewModel extends ViewModel {
	
	private final Recipe recipe;
	
	SharedFragmentsViewModel(Recipe recipe) {
		this.recipe = recipe;
	}
	
	Recipe getRecipe() {
		return recipe;
	}
}
