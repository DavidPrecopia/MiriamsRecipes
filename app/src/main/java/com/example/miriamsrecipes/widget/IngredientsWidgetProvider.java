package com.example.miriamsrecipes.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public final class IngredientsWidgetProvider extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int appWidgetId : appWidgetIds) {
			IngredientsRemoteView remoteView = new IngredientsRemoteView();
			RemoteViews view = remoteView.updateWidget(context, appWidgetId);
			appWidgetManager.updateAppWidget(appWidgetId, view);
		}
	}
}
