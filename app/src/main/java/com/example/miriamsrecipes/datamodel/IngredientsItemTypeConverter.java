package com.example.miriamsrecipes.datamodel;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

public final class IngredientsItemTypeConverter {
	
	private static final Gson GSON;
	
	static {
		GSON = new Gson();
	}
	
	
	@TypeConverter
	public static List<IngredientsItem> stringToIngredientsList(String ingredients) {
		if (TextUtils.isEmpty(ingredients)) {
			return Collections.emptyList();
		}
		return GSON.fromJson(ingredients, new TypeToken<List<IngredientsItem>>() {}.getType());
	}
	
	@TypeConverter
	public static String ingredientsItemToString(List<IngredientsItem> ingredientsItemList) {
		return GSON.toJson(ingredientsItemList);
	}
}
