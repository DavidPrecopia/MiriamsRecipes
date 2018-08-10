package com.example.miriamsrecipes.activities.widgetconfig;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RemoteViews;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.activities.main.RecipeInfoAdapter;
import com.example.miriamsrecipes.databinding.ActivityIngredientsWidgetConfigBinding;
import com.example.miriamsrecipes.datamodel.Recipe;
import com.example.miriamsrecipes.widget.IngredientsRemoteView;
import com.example.miriamsrecipes.widget.SharedPrefWidgetKey;

import java.util.List;

import timber.log.Timber;

public class IngredientsWidgetConfigActivity extends AppCompatActivity
		implements RecipeInfoAdapter.RecipeInfoItemClickListener {
	
	public static final String SHARED_PREF_NAME = IngredientsWidgetConfigActivity.class.getSimpleName();
	private int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	
	private IngredientsWidgetViewModel viewModel;
	private ActivityIngredientsWidgetConfigBinding binding;
	
	private RecipeInfoAdapter recyclerViewAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inCaseUserQuits();
		binding = DataBindingUtil.setContentView(this, R.layout.activity_ingredients_widget_config);
		init();
	}
	
	private void init() {
		progressBarVisibility(View.VISIBLE);
		getWidgetId();
		setUpViewModel();
		setUpRecyclerView();
		observeViewModel();
	}
	
	private void observeViewModel() {
		viewModel.getRecipes().observe(this, recipeInfoList -> {
			recyclerViewAdapter.replaceData(recipeInfoList);
			progressBarVisibility(View.GONE);
		});
	}
	
	private void getWidgetId() {
		widgetId = getIntent().getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		Timber.d(String.valueOf(widgetId));
		if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}
	}
	
	private void setUpViewModel() {
		ViewModelFactory factory = new ViewModelFactory(getApplication());
		viewModel = ViewModelProviders.of(this, factory).get(IngredientsWidgetViewModel.class);
	}
	
	private void setUpRecyclerView() {
		RecyclerView recyclerView = binding.recyclerView;
		
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setHasFixedSize(true);
		
		recyclerViewAdapter = new RecipeInfoAdapter(this);
		restoreAdapterData();
		recyclerView.setAdapter(recyclerViewAdapter);
	}
	
	/**
	 * Restoring data before setting this adapter as RecyclerView's adapter,
	 * restores scroll state by RecyclerView itself.
	 */
	private void restoreAdapterData() {
		List<Recipe> recipeList = viewModel.getRecipes().getValue();
		if (recipeList == null || recipeList.isEmpty()) {
			return;
		}
		recyclerViewAdapter.replaceData(recipeList);
	}
	
	
	@Override
	public void recipeInfoClick(int listPosition) {
		saveSelectedRecipe(
				viewModel.getRecipes().getValue().get(listPosition)
		);
	}
	
	private void saveSelectedRecipe(Recipe recipe) {
		saveValuesToSharedPrefs(recipe.getId(), recipe.getName());
		updateWidget();
		resultsIntent();
		finish();
	}
	
	private void saveValuesToSharedPrefs(int id, String name) {
		SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
		editor.putInt(SharedPrefWidgetKey.recipeIdKey(getApplicationContext(), widgetId), id);
		editor.putString(SharedPrefWidgetKey.recipeNameKey(getApplicationContext(), widgetId), name);
		editor.apply();
	}
	
	private void updateWidget() {
		RemoteViews remoteView = new IngredientsRemoteView().updateWidget(getApplicationContext(), widgetId);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		appWidgetManager.updateAppWidget(widgetId, remoteView);
	}
	
	private void resultsIntent() {
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		setResult(RESULT_OK, resultValue);
	}
	
	
	private void progressBarVisibility(int visibility) {
		binding.progressBar.setVisibility(visibility);
	}
	
	
	private void inCaseUserQuits() {
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		setResult(RESULT_CANCELED, resultValue);
	}
}
