package com.example.miriamsrecipes.model;

import android.app.Application;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.activities.main.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

final class Model {
	
	private final Gson gson;
	private final BufferedReader reader;
	
	private static Model model;
	
	public static Model getInstance(Application application) {
		if (model == null) {
			model = new Model(application);
		}
		return model;
	}
	
	private Model(Application application) {
		this.gson = new Gson();
		InputStream raw = application.getResources().openRawResource(R.raw.udacity_miriam_recipes);
		this.reader = new BufferedReader(new InputStreamReader(raw));
	}
	
	
	// Works
	public List<Recipe> getAllRecipes(Application context) {
		return new Gson().fromJson(reader, new TypeToken<List<Recipe>>() {
		}.getType());
	}
}