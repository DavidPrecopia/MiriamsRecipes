package com.example.miriamsrecipes.widget;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.datamodel.IngredientsItem;
import com.example.miriamsrecipes.datamodel.Recipe;
import com.example.miriamsrecipes.model.IModelContract;
import com.example.miriamsrecipes.model.Model;

import java.util.ArrayList;
import java.util.List;

final class IngredientsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
	
	private final Application application;
	
	private final int recipeId;
	private List<IngredientsItem> ingredients;
	
	private final AppWidgetManager appWidgetManager;
	private final IModelContract model;
	private final Observer<Recipe> observer;
	
	
	IngredientsRemoteViewsFactory(Application application, int recipeId, int widgetId) {
		this.application = application;
		this.recipeId = recipeId;
		this.ingredients = new ArrayList<>();
		
		this.model = Model.getInstance(application);
		appWidgetManager = AppWidgetManager.getInstance(application);
		
		this.observer = (recipe) -> {
			ingredients.clear();
			ingredients.addAll(recipe.getIngredients());
			appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_list_view_ingredients);
		};
	}
	
	@Override
	public void onCreate() {
		model.getSingleRecipe(recipeId).observeForever(observer);
	}
	
	
	@Override
	public RemoteViews getViewAt(int position) {
		IngredientsItem ingredient = ingredients.get(position);
		RemoteViews view = new RemoteViews(application.getPackageName(), R.layout.list_view_item_ingredient);
		setUpListItem(view, ingredient);
		return view;
	}
	
	private void setUpListItem(RemoteViews view, IngredientsItem ingredient) {
		view.setTextViewText(R.id.widget_tv_ingredient_name, ingredient.getIngredient());
		view.setTextViewText(R.id.widget_tv_ingredient_quantity, String.valueOf(ingredient.getQuantity()));
		view.setTextViewText(R.id.widget_tv_ingredient_measurement, ingredient.getMeasure());
	}
	
	
	@Override
	public int getCount() {
		return ingredients.size();
	}
	
	
	@Override
	public void onDestroy() {
	}
	
	
	@Override
	public void onDataSetChanged() {
	}
	
	@Override
	public RemoteViews getLoadingView() {
		return null;
	}
	
	@Override
	public int getViewTypeCount() {
		return 1;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}
}
