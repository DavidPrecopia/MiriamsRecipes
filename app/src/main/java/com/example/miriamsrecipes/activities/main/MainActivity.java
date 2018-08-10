package com.example.miriamsrecipes.activities.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.activities.recipe.RecipeActivity;
import com.example.miriamsrecipes.databinding.ActivityMainBinding;
import com.example.miriamsrecipes.datamodel.Recipe;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeInfoAdapter.RecipeInfoItemClickListener {
	
	private MainViewModel viewModel;
	private ActivityMainBinding binding;
	
	private RecipeInfoAdapter recyclerViewAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		init();
	}
	
	private void init() {
		progressBarVisibility(View.VISIBLE);
		setUpToolbar();
		setUpViewModel();
		setUpRecyclerView();
		observeViewModel();
	}
	
	private void setUpToolbar() {
		setSupportActionBar(binding.appBar.toolbar);
	}
	
	private void setUpViewModel() {
		ViewModelFactory factory = new ViewModelFactory(getApplication());
		viewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
	}
	
	private void observeViewModel() {
		viewModel.getRecipes().observe(this, recipes -> {
			recyclerViewAdapter.replaceData(recipes);
			progressBarVisibility(View.GONE);
		});
	}
	
	private void setUpRecyclerView() {
		RecyclerView recyclerView = binding.recyclerView;
		
		recyclerView.setLayoutManager(getLayoutManager());
		recyclerView.setHasFixedSize(true);
		
		recyclerViewAdapter = new RecipeInfoAdapter(this);
		restoreAdapterData();
		recyclerView.setAdapter(recyclerViewAdapter);
	}
	
	private RecyclerView.LayoutManager getLayoutManager() {
		if (getResources().getBoolean(R.bool.master_detail_layout)) {
			return new GridLayoutManager(this, 3);
		} else {
			return new LinearLayoutManager(this);
		}
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
	
	
	private void progressBarVisibility(int visibility) {
		binding.progressBar.setVisibility(visibility);
	}
	
	
	@Override
	public void recipeInfoClick(int listPosition) {
		Intent intent = new Intent(this, RecipeActivity.class);
		intent.putExtra(
				RecipeActivity.class.getSimpleName(),
				viewModel.getRecipes().getValue().get(listPosition).getId()
		);
		startActivity(intent);
	}
}
