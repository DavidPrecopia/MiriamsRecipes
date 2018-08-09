package com.example.miriamsrecipes.network;

import com.example.miriamsrecipes.datamodel.Recipe;

import java.util.List;

import io.reactivex.Single;

public interface INetworkContract {
	Single<List<Recipe>> getRecipes();
}
