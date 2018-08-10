package com.example.miriamsrecipes.activities.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

import com.example.miriamsrecipes.datamodel.Recipe;
import com.example.miriamsrecipes.model.IModelContract;
import com.example.miriamsrecipes.model.Model;

import java.util.List;

import timber.log.Timber;

final class MainViewModel extends AndroidViewModel {
	
	private MutableLiveData<List<Recipe>> recipes;
	private final Observer<List<Recipe>> observer;
	
	private final IModelContract model;
	
	MainViewModel(@NonNull Application application) {
		super(application);
		this.recipes = new MutableLiveData<>();
		this.observer = getObserver();
		this.model = Model.getInstance(application);
		observeModel();
	}
	
	private Observer<List<Recipe>> getObserver() {
		return recipeInfoList -> {
			if (recipeInfoList == null || recipeInfoList.isEmpty()) {
				Timber.i("getObserver - recipeInfoList is null or empty.");
				return;
			}
			recipes.setValue(recipeInfoList);
		};
	}
	
	private void observeModel() {
		model.observeAllRecipes().observeForever(observer);
	}
	
	
	LiveData<List<Recipe>> getRecipes() {
		return recipes;
	}
	
	
	@Override
	protected void onCleared() {
		super.onCleared();
		model.observeAllRecipes().removeObserver(observer);
	}
}
