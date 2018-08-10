package com.example.miriamsrecipes.network;

import com.example.miriamsrecipes.datamodel.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {
	@GET(UrlManager.BAKING_JSON)
	Call<List<Recipe>> getRecipes();
}
