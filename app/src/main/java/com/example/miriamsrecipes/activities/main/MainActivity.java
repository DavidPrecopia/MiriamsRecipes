package com.example.miriamsrecipes.activities.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.activities.recipe.RecipeActivity;

public class MainActivity extends AppCompatActivity {
	
	private MainViewModel viewModel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		ViewModelFactory factory = new ViewModelFactory(getApplication());
		viewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
	}
	
	
	private void openRecipeActivity() {
		Intent intent = new Intent(this, RecipeActivity.class);
		// Grab specific recipe
//		intent.putExtra(RecipeActivity.class.getSimpleName(), viewModel.getRecipes().getValue().get(0));
//		startActivity(intent);
	}
}
