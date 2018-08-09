package com.example.miriamsrecipes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.activities.main.MainActivity;
import com.example.miriamsrecipes.activities.widgetconfig.IngredientsWidgetConfigActivity;
import com.example.miriamsrecipes.util.SharedPrefWidgetKey;

public final class IngredientsWidgetProvider extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int appWidgetId : appWidgetIds) {
			RemoteViews view = updateWidget(context, appWidgetId);
			appWidgetManager.updateAppWidget(appWidgetId, view);
		}
	}
	
	private RemoteViews updateWidget(Context context, int appWidgetId) {
		RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
		
		SharedPreferences preferences = context.getSharedPreferences(IngredientsWidgetConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		String recipeName = preferences.getString(SharedPrefWidgetKey.recipeNameKey(context, appWidgetId), null);
		int recipeId = preferences.getInt(SharedPrefWidgetKey.recipeIdKey(context, appWidgetId), -1);
		
		setPendingIntent(context, view);
		setUpView(context, view, recipeId, recipeName);
		return view;
	}
	
	private void setPendingIntent(Context context, RemoteViews view) {
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.root_layout, pendingIntent);
	}
	
	private void setUpView(Context context, RemoteViews view, int recipeId, String recipeName) {
		view.setTextViewText(R.id.tv_recipe_name, recipeName);
		
		Intent adapterIntent = new Intent(context, WidgetRemoteViewService.class);
		adapterIntent.putExtra(context.getString(R.string.widget_key_recipe_id), recipeId);
		view.setRemoteAdapter(R.id.widget_list_view_ingredients, adapterIntent);
	}
}
