package com.example.miriamsrecipes.activities.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.miriamsrecipes.model.IModelContract;
import com.example.miriamsrecipes.model.Model;

import java.util.List;

final class MainViewModel extends AndroidViewModel {
	
	private final IModelContract model;
	
	MainViewModel(@NonNull Application application) {
		super(application);
		this.model = Model.getInstance(application);
	}
	
	LiveData<List<RecipeInfo>> getRecipes() {
		return model.getAllRecipes();
	}
}
