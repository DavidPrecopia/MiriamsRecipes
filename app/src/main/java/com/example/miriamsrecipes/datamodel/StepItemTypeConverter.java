package com.example.miriamsrecipes.datamodel;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

public final class StepItemTypeConverter {
	
	private static final Gson GSON;
	
	static {
		GSON = new Gson();
	}
	
	
	@TypeConverter
	public static List<StepItem> stringToIngredientsList(String steps) {
		if (TextUtils.isEmpty(steps)) {
			return Collections.emptyList();
		}
		return GSON.fromJson(steps, new TypeToken<List<StepItem>>() {}.getType());
	}
	
	@TypeConverter
	public static String ingredientsItemToString(List<StepItem> stepItemList) {
		return GSON.toJson(stepItemList);
	}
}
