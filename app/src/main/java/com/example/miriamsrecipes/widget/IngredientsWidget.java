package com.example.miriamsrecipes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.activities.main.MainActivity;

public final class IngredientsWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int appWidgetId : appWidgetIds) {
			Intent intent = new Intent(context, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
			view.setTextViewText(R.id.tv_recipe_name, "Banana Cream Pie");
			view.setOnClickPendingIntent(R.id.tv_recipe_name, pendingIntent);
			
			appWidgetManager.updateAppWidget(appWidgetId, view);
		}
	}
}
