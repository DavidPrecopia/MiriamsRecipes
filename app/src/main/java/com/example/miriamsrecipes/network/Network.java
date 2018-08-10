package com.example.miriamsrecipes.network;

import com.example.miriamsrecipes.datamodel.Recipe;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class Network implements INetworkContract {
	
	private final RecipeService recipeService;
	
	private static Network network;
	
	public synchronized static Network getInstance() {
		if (network == null) {
			network = new Network();
		}
		return network;
	}
	
	private Network() {
		this.recipeService = new Retrofit.Builder()
				.baseUrl(UrlManager.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build()
				.create(RecipeService.class);
	}
	
	
	@Override
	public void getRecipes(Callback<List<Recipe>> callback) {
		recipeService.getRecipes().enqueue(callback);
	}
}
