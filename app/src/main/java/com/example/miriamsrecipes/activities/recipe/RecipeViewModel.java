package com.example.miriamsrecipes.activities.recipe;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.miriamsrecipes.datamodel.Recipe;
import com.example.miriamsrecipes.model.IModelContract;
import com.example.miriamsrecipes.model.Model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

final class RecipeViewModel extends AndroidViewModel {
	
	private final int recipeId;
	private final MutableLiveData<Recipe> recipe;
	
	private final CompositeDisposable disposable;
	private final IModelContract model;
	
	RecipeViewModel(@NonNull Application application, int recipeId) {
		super(application);
		this.recipeId = recipeId;
		this.recipe = new MutableLiveData<>();
		this.disposable = new CompositeDisposable();
		this.model = Model.getInstance(application);
		init();
	}
	
	private void init() {
		getRecipeFromModel();
	}
	
	
	private void getRecipeFromModel() {
		disposable.add(model.getSingleRecipe(recipeId)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(singleObserver())
		);
	}
	
	private DisposableSingleObserver<Recipe> singleObserver() {
		return new DisposableSingleObserver<Recipe>() {
			@Override
			public void onSuccess(Recipe recipe) {
				RecipeViewModel.this.recipe.setValue(recipe);
			}
			
			@Override
			public void onError(Throwable e) {
				Timber.e(e);
			}
		};
	}
	
	
	LiveData<Recipe> getRecipe() {
		return recipe;
	}
	
	
	@Override
	protected void onCleared() {
		super.onCleared();
		disposable.clear();
	}
}
