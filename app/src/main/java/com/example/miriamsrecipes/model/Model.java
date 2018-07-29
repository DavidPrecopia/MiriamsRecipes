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

final class Model {
	
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
	
	
	public List<Recipe> getRecipes() {
		// Ensure that this happens off the main thread
		if (allRecipes.isEmpty()) {
			parseJson();
		}
		return allRecipes;
	}
	
	private void parseJson() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(rawJson));
		allRecipes.addAll(
				new Gson().fromJson(reader, new TypeToken<List<Recipe>>() {
				}.getType())
		);
	}
}