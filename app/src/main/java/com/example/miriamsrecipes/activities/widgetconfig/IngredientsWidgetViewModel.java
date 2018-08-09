package com.example.miriamsrecipes.activities.widgetconfig;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

import com.example.miriamsrecipes.activities.main.RecipeInfo;
import com.example.miriamsrecipes.model.IModelContract;
import com.example.miriamsrecipes.model.Model;

import java.util.List;

final class IngredientsWidgetViewModel extends AndroidViewModel {
	
	private MutableLiveData<List<RecipeInfo>> recipes;
	private Observer<List<RecipeInfo>> observer;
	
	private final IModelContract model;
	
	IngredientsWidgetViewModel(@NonNull Application application) {
		super(application);
		this.recipes = new MutableLiveData<>();
		this.observer = (this.recipes::setValue);
		this.model = Model.getInstance(application);
		observeModel();
	}
	
	private void observeModel() {
		model.getAllRecipes().observeForever(observer);
	}
	
	
	LiveData<List<RecipeInfo>> getRecipes() {
		return recipes;
	}
	
	
	@Override
	protected void onCleared() {
		super.onCleared();
		model.getAllRecipes().removeObserver(observer);
	}
}
