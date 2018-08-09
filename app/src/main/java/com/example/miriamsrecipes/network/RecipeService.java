package com.example.miriamsrecipes.network;

import com.example.miriamsrecipes.datamodel.Recipe;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface RecipeService {
	@GET(UrlManager.BAKING_JSON)
	Single<List<Recipe>> getRecipes();
}
