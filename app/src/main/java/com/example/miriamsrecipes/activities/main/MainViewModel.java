package com.example.miriamsrecipes.activities.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.miriamsrecipes.datamodel.Recipe;
import com.example.miriamsrecipes.model.IModelContract;
import com.example.miriamsrecipes.model.Model;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

final class MainViewModel extends AndroidViewModel {
	
	private final MutableLiveData<List<Recipe>> recipes;
	
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
		disposable.add(model.getRecipes()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(observer())
		);
	}
	
	private DisposableSingleObserver<List<Recipe>> observer() {
		return new DisposableSingleObserver<List<Recipe>>() {
			@Override
			public void onSuccess(List<Recipe> recipes) {
				MainViewModel.this.recipes.setValue(recipes);
			}
			
			@Override
			public void onError(Throwable e) {
				Timber.e(e);
			}
		};
	}
	
	
	@Override
	protected void onCleared() {
		super.onCleared();
		disposable.clear();
	}
	
	LiveData<List<Recipe>> getRecipes() {
		return recipes;
	}
}
