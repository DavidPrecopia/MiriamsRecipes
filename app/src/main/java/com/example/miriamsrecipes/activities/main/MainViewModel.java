package com.example.miriamsrecipes.activities.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.miriamsrecipes.model.IModelContract;
import com.example.miriamsrecipes.model.Model;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

final class MainViewModel extends AndroidViewModel {
	
	private final MutableLiveData<List<RecipeInfo>> recipes;
	
	private final CompositeDisposable disposable;
	private final IModelContract model;
	
	MainViewModel(@NonNull Application application) {
		super(application);
		this.recipes = new MutableLiveData<>();
		this.disposable = new CompositeDisposable();
		this.model = Model.getInstance(application);
		init();
	}
	
	private void init() {
		getRecipesFromModel();
	}
	
	
	private void getRecipesFromModel() {
		disposable.add(model.getAllRecipes()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(observer())
		);
	}
	
	private DisposableSingleObserver<List<RecipeInfo>> observer() {
		return new DisposableSingleObserver<List<RecipeInfo>>() {
			@Override
			public void onSuccess(List<RecipeInfo> recipeInfoList) {
				MainViewModel.this.recipes.setValue(recipeInfoList);
			}
			
			@Override
			public void onError(Throwable e) {
				Timber.e(e);
			}
		};
	}
	
	
	LiveData<List<RecipeInfo>> getRecipes() {
		return recipes;
	}
	
	
	@Override
	protected void onCleared() {
		super.onCleared();
		disposable.clear();
	}
}
