package com.example.miriamsrecipes.util;

import android.content.Context;

import com.example.miriamsrecipes.R;

public final class SharedPrefWidgetKey {
	private SharedPrefWidgetKey() {
	}
	
	
	public static String recipeIdKey(Context context, int widgetId) {
		return context.getString(R.string.widget_key_recipe_id) + widgetId;
	}
	
	public static String recipeNameKey(Context context, int widgetId) {
		return context.getString(R.string.widget_key_recipe_name) + widgetId;
	}
}
