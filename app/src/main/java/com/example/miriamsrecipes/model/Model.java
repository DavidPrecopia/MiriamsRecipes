package com.example.miriamsrecipes.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.database.SQLException;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.activities.main.RecipeInfo;
import com.example.miriamsrecipes.datamodel.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class Model implements IModelContract {
	
	private final RecipeDao dao;
	
	private InputStream rawJson;
	
	private static Model model;
	
	public synchronized static Model getInstance(Application application) {
		if (model == null) {
			model = new Model(application);
		}
		return model;
	}
	
	private Model(Application application) {
		dao = AppDatabase.getInstance(application).getRecipeDao();
		rawJson = application.getResources().openRawResource(R.raw.udacity_miriam_recipes);
		populateDatabase();
	}
	
	
	@Override
	public LiveData<List<RecipeInfo>> getAllRecipes() {
		return dao.getAllRecipes();
	}
	
	@Override
	public Single<Recipe> getSingleRecipe(int recipeId) {
		return dao.getSingleRecipe(recipeId);
	}
	
	
	private void populateDatabase() {
		dao.checkDatabase()
				.subscribeOn(Schedulers.io())
				.subscribe(checkObserver());
	}
	
	private MaybeObserver<? super Integer> checkObserver() {
		return new MaybeObserver<Integer>() {
			@Override
			public void onSubscribe(Disposable d) {
			
			}
			
			@Override
			public void onSuccess(Integer integer) {
				Timber.i("The database is populated");
			}
			
			@Override
			public void onError(Throwable e) {
				throw new SQLException("Error checking the database");
			}
			
			@Override
			public void onComplete() {
				insertRecipes();
			}
		};
	}
	
	
	private void insertRecipes() {
		Single.fromCallable(() -> dao.popularDatabase(parseJson()))
				.subscribeOn(Schedulers.io())
				.subscribe(populateObserver());
	}
	
	private SingleObserver<List<Long>> populateObserver() {
		return new SingleObserver<List<Long>>() {
			@Override
			public void onSubscribe(Disposable d) {
			
			}
			
			@Override
			public void onSuccess(List<Long> longs) {
				Timber.i("Successfully populated database.");
			}
			
			@Override
			public void onError(Throwable e) {
				throw new SQLException("Error populating the database");
			}
		};
	}
	
	private List<Recipe> parseJson() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(rawJson));
		return new Gson().fromJson(reader, new TypeToken<List<Recipe>>() {
		}.getType());
	}
}