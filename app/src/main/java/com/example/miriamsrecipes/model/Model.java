package com.example.miriamsrecipes.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.database.SQLException;

import com.example.miriamsrecipes.activities.main.RecipeInfo;
import com.example.miriamsrecipes.datamodel.Recipe;
import com.example.miriamsrecipes.network.INetworkContract;
import com.example.miriamsrecipes.network.Network;

import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class Model implements IModelContract {
	
	private final RecipeDao dao;
	
	private final INetworkContract network;
	
	private static Model model;
	
	public synchronized static Model getInstance(Application application) {
		if (model == null) {
			model = new Model(application);
		}
		return model;
	}
	
	private Model(Application application) {
		dao = AppDatabase.getInstance(application).getRecipeDao();
		network = Network.getInstance(application);
		populateDatabase();
	}
	
	
	/**
	 * Regarding both get methods below,
	 *
	 * @return LiveData (instead of Single) because the database might be empty.
	 */
	
	@Override
	public LiveData<List<RecipeInfo>> getAllRecipes() {
		return dao.getAllRecipes();
	}
	
	@Override
	public LiveData<Recipe> getSingleRecipe(int recipeId) {
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
		network.getRecipes()
				.subscribeOn(Schedulers.io())
				.subscribe(populateObserver());
	}
	
	private SingleObserver<List<Recipe>> populateObserver() {
		return new SingleObserver<List<Recipe>>() {
			@Override
			public void onSubscribe(Disposable d) {
				Timber.d("onSub");
			}
			
			@Override
			public void onSuccess(List<Recipe> recipeList) {
				dao.popularDatabase(recipeList);
			}
			
			@Override
			public void onError(Throwable e) {
				Timber.e(e);
			}
		};
	}
}