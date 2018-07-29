package com.example.miriamsrecipes.activities.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
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
	
	private final CompositeDisposable disposable;
	private final IModelContract model;
	
	MainViewModel(@NonNull Application application) {
		super(application);
		this.disposable = new CompositeDisposable();
		this.model = Model.getInstance(application);
		init();
	}
	
	private void init() {
		getRecipes();
	}
	
	
	private void getRecipes() {
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
				for (Recipe recipe : recipes) {
					Timber.i(recipe.getId() + "\n" + recipe.getName());
				}
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
}
