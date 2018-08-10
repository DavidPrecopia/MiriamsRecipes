package com.example.miriamsrecipes.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.database.SQLException;
import android.support.annotation.NonNull;

import com.example.miriamsrecipes.datamodel.Recipe;
import com.example.miriamsrecipes.network.Network;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public final class Model implements IModelContract, Callback<List<Recipe>> {
	
	private MutableLiveData<List<Recipe>> allRecipesList;
	
	private final RecipeDao dao;
	
	private static Model model;
	
	public static Model getInstance(Application application) {
		if (model == null) {
			model = new Model(application);
		}
		return model;
	}
	
	private Model(Application application) {
		allRecipesList = new MutableLiveData<>();
		dao = AppDatabase.getInstance(application).getRecipeDao();
		populateDatabase();
	}
	
	
	@Override
	public LiveData<List<Recipe>> observeAllRecipes() {
		return allRecipesList;
	}
	
	@Override
	public LiveData<Recipe> getSingleRecipe(int recipeId) {
		return dao.getSingleRecipe(recipeId);
	}
	
	
	private void populateDatabase() {
		dao.checkDatabase()
				.subscribeOn(Schedulers.io())
				.doOnError((onError) -> {throw new SQLException("Error checking the database.\n%s", onError);})
				.doOnSuccess((recipeList -> {
					if (recipeList.isEmpty()) {
						insertRecipes();
					} else {
						allRecipesList.postValue(recipeList);
					}
				}))
				.doOnComplete(this::insertRecipes)
				.subscribe();
	}
	
	
	private void insertRecipes() {
		Network.getInstance().getRecipes(this);
	}
	
	
	@Override
	public void onResponse(@NonNull Call<List<Recipe>> call, Response<List<Recipe>> response) {
		if (! response.isSuccessful()) {
			Timber.e("Call was unsuccessful.");
			return;
		}
		Completable.fromCallable(() -> dao.insertRecipe(response.body()))
				.subscribeOn(Schedulers.io())
				.doOnError((onError) -> {throw new SQLException("Error inserting recipes into the database.\n%s", onError);})
				.doOnComplete(() -> this.allRecipesList.postValue(response.body()))
				.subscribe();
	}
	
	@Override
	public void onFailure(@NonNull Call<List<Recipe>> call, Throwable t) {
		Timber.e("Call failed. Throwable message: %s", t.toString());
	}
}