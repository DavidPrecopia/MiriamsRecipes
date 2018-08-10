package com.example.miriamsrecipes.network;

import com.example.miriamsrecipes.datamodel.Recipe;

import java.util.List;

import retrofit2.Callback;

public interface INetworkContract {
	void getRecipes(Callback<List<Recipe>> callback);
}
