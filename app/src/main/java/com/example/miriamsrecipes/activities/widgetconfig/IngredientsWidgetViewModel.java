package com.example.miriamsrecipes.activities.widgetconfig;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.miriamsrecipes.datamodel.Recipe;
import com.example.miriamsrecipes.model.IModelContract;
import com.example.miriamsrecipes.model.Model;

import java.util.List;

final class IngredientsWidgetViewModel extends AndroidViewModel {
	
	private MutableLiveData<List<Recipe>> recipes;
	
	private final IModelContract model;
	
	IngredientsWidgetViewModel(@NonNull Application application) {
		super(application);
		this.recipes = new MutableLiveData<>();
		this.model = Model.getInstance(application);
		observeModel();
	}
	
	private void observeModel() {
//		model.observeAllRecipes(this.recipes);
	}
	
	
	LiveData<List<Recipe>> getRecipes() {
		return recipes;
	}
}
