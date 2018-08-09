package com.example.miriamsrecipes.widget;

import android.content.Context;

import com.example.miriamsrecipes.R;

final class WidgetKeys {
	private WidgetKeys() {
	}
	
	
	static String recipeIdKey(Context context, int widgetId) {
		return context.getString(R.string.widget_key_recipe_id) + widgetId;
	}
	
	static String recipeNameKey(Context context, int widgetId) {
		return context.getString(R.string.widget_key_recipe_name) + widgetId;
	}
}
