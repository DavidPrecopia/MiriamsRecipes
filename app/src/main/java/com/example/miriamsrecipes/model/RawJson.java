package com.example.miriamsrecipes.model;

import android.app.Application;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.activities.main.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

final class RawJson {
	
	// Works
	static List<Recipe> getAllRecipes(Application context) {
		InputStream raw = context.getResources().openRawResource(R.raw.udacity_miriam_recipes);
		Reader reader = new BufferedReader(new InputStreamReader(raw));
		
		return new Gson().fromJson(reader, new TypeToken<List<Recipe>>() {}.getType());
	}
	
}
