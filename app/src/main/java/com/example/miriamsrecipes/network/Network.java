package com.example.miriamsrecipes.network;

import android.app.Application;

import com.example.miriamsrecipes.datamodel.Recipe;

import java.util.List;

import io.reactivex.Single;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class Network implements INetworkContract {
	
	private final RecipeService recipeService;
	
	private static Network network;
	
	public synchronized static Network getInstance(Application context) {
		if (network == null) {
			network = new Network(context);
		}
		return network;
	}
	
	private Network(Application context) {
		this.recipeService = new Retrofit.Builder()
				.baseUrl(UrlManager.BASE_URL)
				.client(okHttpClient(context))
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.build()
				.create(RecipeService.class);
	}
	
	private OkHttpClient okHttpClient(Application context) {
		Cache cache = new Cache(context.getCacheDir(), 10 * 1024 * 1024);
		return new OkHttpClient.Builder()
				.cache(cache)
				.build();
	}
	
	
	@Override
	public Single<List<Recipe>> getRecipes() {
		return recipeService.getRecipes();
	}
}
