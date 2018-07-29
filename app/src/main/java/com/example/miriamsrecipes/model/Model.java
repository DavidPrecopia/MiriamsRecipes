package com.example.miriamsrecipes.model;

import android.app.Application;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.datamodel.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public final class Model implements IModelContract {
	
	private final List<Recipe> allRecipes;
	private final InputStream rawJson;
	
	private static Model model;
	
	public static Model getInstance(Application application) {
		if (model == null) {
			model = new Model(application);
		}
		return model;
	}
	
	private Model(Application application) {
		allRecipes = new ArrayList<>();
		rawJson = application.getResources().openRawResource(R.raw.udacity_miriam_recipes);
	}
	
	
	@Override
	public Single<List<Recipe>> getRecipes() {
		if (allRecipes.isEmpty()) {
			// This check ensure that parsing happens off the main thread
			parseJson();
		}
		return Single.just(allRecipes);
	}
	
	private void parseJson() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(rawJson));
		allRecipes.addAll(
				new Gson().fromJson(reader, new TypeToken<List<Recipe>>() {
				}.getType())
		);
	}
}