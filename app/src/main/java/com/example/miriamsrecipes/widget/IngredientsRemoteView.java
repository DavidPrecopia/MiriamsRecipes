package com.example.miriamsrecipes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.activities.recipe.RecipeActivity;
import com.example.miriamsrecipes.activities.widgetconfig.IngredientsWidgetConfigActivity;

public final class IngredientsRemoteView {
	
	public IngredientsRemoteView() {
	
	}
	
	
	public RemoteViews updateWidget(Context context, int appWidgetId) {
		RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
		
		SharedPreferences preferences = getSharedPreferences(context);
		int recipeId = getRecipeId(context, appWidgetId, preferences);
		
		setPendingIntent(context, view, recipeId);
		setConfigPendingIntent(context, view, appWidgetId);
		setUpView(
				context,
				view,
				recipeId,
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
	
	
	private void setPendingIntent(Context context, RemoteViews view, int recipeId) {
		Intent intent = new Intent(context, RecipeActivity.class);
		intent.putExtra(RecipeActivity.class.getSimpleName(), recipeId);
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.widget_tv_recipe_name, pendingIntent);
	}
	
	private void setConfigPendingIntent(Context context, RemoteViews view, int appWidgetId) {
		Intent configIntent = new Intent(context, IngredientsWidgetConfigActivity.class);
		configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE + Integer.toString(appWidgetId));
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, 0);
		view.setOnClickPendingIntent(R.id.widget_iv_settings, pendingIntent);
	}
	
	
	private void setUpView(Context context, RemoteViews view, int recipeId, String recipeName, int appWidgetId) {
		view.setTextViewText(R.id.widget_tv_recipe_name, recipeName);
		
		Intent adapterIntent = new Intent(context, IngredientsRemoteViewService.class);
		adapterIntent.putExtra(context.getString(R.string.widget_key_recipe_id), recipeId);
		adapterIntent.putExtra(context.getString(R.string.widget_key_widget_id), appWidgetId);
		view.setRemoteAdapter(R.id.widget_list_view_ingredients, adapterIntent);
	}
}
