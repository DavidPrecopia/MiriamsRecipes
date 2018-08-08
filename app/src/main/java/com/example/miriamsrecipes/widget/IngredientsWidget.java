package com.example.miriamsrecipes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.activities.main.MainActivity;

public final class IngredientsWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int appWidgetId : appWidgetIds) {
			RemoteViews view = updateWidget(context);
			appWidgetManager.updateAppWidget(appWidgetId, view);
		}
	}
	
	@NonNull
	private RemoteViews updateWidget(Context context) {
		RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
		setPendingIntent(context, view);
		setUpView(context, view);
		return view;
	}
	
	private void setPendingIntent(Context context, RemoteViews view) {
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.root_layout, pendingIntent);
	}
	
	private void setUpView(Context context, RemoteViews view) {
		view.setTextViewText(R.id.tv_recipe_name, "Banana Cream Pie");
		Intent adapterIntent = new Intent(context, MyRemoteViewService.class);
		view.setRemoteAdapter(R.id.widget_list_view_ingredients, adapterIntent);
	}
}
