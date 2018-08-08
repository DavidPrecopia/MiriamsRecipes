package com.example.miriamsrecipes.model;

import android.app.Application;
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

import io.reactivex.Single;

public final class Model implements IModelContract {
	
	private final RecipeDao dao;
	
	private boolean databasePopulated = false;
	private final InputStream rawJson;
	
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
	}
	
	
	@Override
	public Single<List<RecipeInfo>> getAllRecipes() {
		checkDatabase();
		return dao.getAllRecipes();
	}
	
	@Override
	public Single<Recipe> getSingleRecipe(int recipeId) {
		checkDatabase();
		return dao.getSingleRecipe(recipeId);
	}
	
	
	/**
	 * This check ensure that insertion happens on a background thread
	 */
	private void checkDatabase() {
		if (databasePopulated) {
			return;
		}
		populateDatabase();
	}
	
	private void populateDatabase() {
		List<Long> insertResults = dao.popularDatabase(parseJson());
		if (insertResults.isEmpty() || insertResults.get(0) <= 0) {
			throw new SQLException("Error populating the database");
		}
		databasePopulated = true;
	}
	
	private List<Recipe> parseJson() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(rawJson));
		return new Gson().fromJson(reader, new TypeToken<List<Recipe>>() {
		}.getType());
	}
}