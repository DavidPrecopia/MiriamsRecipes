package com.example.miriamsrecipes.activities.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.activities.recipe.RecipeActivity;
import com.example.miriamsrecipes.databinding.ActivityMainBinding;
import com.example.miriamsrecipes.databinding.ListItemRecipeBinding;
import com.example.miriamsrecipes.datamodel.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	
	private MainViewModel viewModel;
	private ActivityMainBinding binding;
	
	private RecipeAdapter recyclerViewAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		init();
	}
	
	
	private void init() {
		displayLoading();
		setUpToolbar();
		setUpRecyclerView();
		setUpViewModel();
		observeRecipes();
	}
	
	
	private void setUpToolbar() {
		setSupportActionBar(binding.appBar.toolbar);
	}
	
	private void setUpViewModel() {
		ViewModelFactory factory = new ViewModelFactory(getApplication());
		viewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
	}
	
	private void observeRecipes() {
		viewModel.getRecipes().observe(this, recipes -> {
			recyclerViewAdapter.replaceData(recipes);
			hideLoading();
		});
	}
	
	private void setUpRecyclerView() {
		RecyclerView recyclerView = binding.recyclerView;
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.addItemDecoration(getDividerItemDecoration(recyclerView, layoutManager));
		recyclerView.setHasFixedSize(true);
		recyclerViewRestoreData();
		recyclerView.setAdapter(recyclerViewAdapter);
	}
	
	private RecyclerView.ItemDecoration getDividerItemDecoration(RecyclerView recyclerView, LinearLayoutManager layoutManager) {
		return new DividerItemDecoration(
				recyclerView.getContext(),
				layoutManager.getOrientation()
		);
	}
	
	/**
	 * Setting the data before the adapter is set,
	 * restores scroll state on rotation
	 */
	private void recyclerViewRestoreData() {
		List<Recipe> recipeList = viewModel.getRecipes().getValue();
		if (recipeList == null) {
			recyclerViewAdapter = new RecipeAdapter(new ArrayList<>());
		} else {
			recyclerViewAdapter = new RecipeAdapter(recipeList);
		}
	}
	
	
	private void displayLoading() {
		recyclerViewVisibility(View.INVISIBLE);
		progressBarVisibility(View.VISIBLE);
	}
	
	private void hideLoading() {
		recyclerViewVisibility(View.VISIBLE);
		progressBarVisibility(View.GONE);
	}
	
	private void recyclerViewVisibility(int visibility) {
		binding.recyclerView.setVisibility(visibility);
	}
	
	private void progressBarVisibility(int visibility) {
		binding.progressBar.setVisibility(visibility);
	}
	
	
	private void openSpecificRecipe(final Recipe recipe) {
		Intent intent = new Intent(this, RecipeActivity.class);
		intent.putExtra(RecipeActivity.class.getSimpleName(), recipe);
		startActivity(intent);
	}
	
	
	final class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
		
		private final List<Recipe> recipes;
		
		RecipeAdapter(List<Recipe> recipes) {
			this.recipes = recipes;
		}
		
		@NonNull
		@Override
		public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			return new RecipeViewHolder(
					ListItemRecipeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
			);
		}
		
		@Override
		public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
			ListItemRecipeBinding binding = holder.binding;
			binding.setRecipe(recipes.get(holder.getAdapterPosition()));
			binding.executePendingBindings();
		}
		
		void replaceData(List<Recipe> newRecipes) {
			recipes.clear();
			recipes.addAll(newRecipes);
			notifyDataSetChanged();
		}
		
		@Override
		public int getItemCount() {
			return recipes.size();
		}
		
		
		final class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
			
			private final ListItemRecipeBinding binding;
			
			RecipeViewHolder(ListItemRecipeBinding binding) {
				super(binding.getRoot());
				this.binding = binding;
				binding.getRoot().setOnClickListener(this);
			}
			
			@Override
			public void onClick(View view) {
				openSpecificRecipe(
						recipes.get(getAdapterPosition())
				);
			}
		}
	}
}
