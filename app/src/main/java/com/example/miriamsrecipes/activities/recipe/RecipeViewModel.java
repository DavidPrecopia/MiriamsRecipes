package com.example.miriamsrecipes.activities.recipe;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

import com.example.miriamsrecipes.datamodel.Recipe;
import com.example.miriamsrecipes.model.IModelContract;
import com.example.miriamsrecipes.model.Model;

final class RecipeViewModel extends AndroidViewModel {
	
	private final int recipeId;
	private final MutableLiveData<Recipe> recipe;
	private final Observer<Recipe> observer;
	
	private final IModelContract model;
	
	RecipeViewModel(@NonNull Application application, int recipeId) {
		super(application);
		this.recipeId = recipeId;
		this.recipe = new MutableLiveData<>();
		this.observer = (this.recipe::setValue);
		this.model = Model.getInstance(application);
		observeModel();
	}
	
	private void observeModel() {
		model.getSingleRecipe(recipeId).observeForever(observer);
	}
	
	
	LiveData<Recipe> getRecipe() {
		return recipe;
	}
	
	
	@Override
	protected void onCleared() {
		super.onCleared();
		model.getSingleRecipe(recipeId).removeObserver(observer);
	}
}
