package com.example.miriamsrecipes.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.miriamsrecipes.datamodel.Recipe;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
abstract class AppDatabase extends RoomDatabase {

	private static AppDatabase database;
	
	synchronized static AppDatabase getInstance(Context context) {
		if (database == null) {
			database = Room.databaseBuilder(context, AppDatabase.class, DatabaseContract.RECIPE_DATABASE_NAME).build();
		}
		return database;
	}
	
	abstract RecipeDao getRecipeDao();
}
