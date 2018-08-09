package com.example.miriamsrecipes.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.activities.main.MainActivity;
import com.example.miriamsrecipes.activities.widgetconfig.IngredientsWidgetConfigActivity;

public final class IngredientsRemoteView {
	
	public IngredientsRemoteView() {
	
	}
	
	
	public RemoteViews updateWidget(Context context, int appWidgetId) {
		RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
		
		SharedPreferences preferences = getSharedPreferences(context);
		
		setPendingIntent(context, view);
		setUpView(
				context,
				view,
				getRecipeId(context, appWidgetId, preferences),
				getRecipeName(context, appWidgetId, preferences),
				appWidgetId
		);
		return view;
	}
	
	
	private SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(IngredientsWidgetConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
	}
	
	private int getRecipeId(Context context, int appWidgetId, SharedPreferences preferences) {
		return preferences.getInt(SharedPrefWidgetKey.recipeIdKey(context, appWidgetId), - 1);
	}
	
	private String getRecipeName(Context context, int appWidgetId, SharedPreferences preferences) {
		return preferences.getString(SharedPrefWidgetKey.recipeNameKey(context, appWidgetId), null);
	}
	
	
	private void setPendingIntent(Context context, RemoteViews view) {
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.root_layout, pendingIntent);
	}
	
	private void setUpView(Context context, RemoteViews view, int recipeId, String recipeName, int appWidgetId) {
		view.setTextViewText(R.id.tv_recipe_name, recipeName);
		
		Intent adapterIntent = new Intent(context, IngredientsRemoteViewService.class);
		adapterIntent.putExtra(context.getString(R.string.widget_key_recipe_id), recipeId);
		adapterIntent.putExtra(context.getString(R.string.widget_key_widget_id), appWidgetId);
		view.setRemoteAdapter(R.id.widget_list_view_ingredients, adapterIntent);
	}
}
