package com.example.miriamsrecipes.activities.recipe;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.miriamsrecipes.R;

public class RecipeActivity extends AppCompatActivity
		implements StepsFragment.FragmentClickListener, StepsFragment.IngredientClickListener,
		SingleStepFragment.ChangeStepListener {
	
	private FragmentManager fragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataBindingUtil.setContentView(this, R.layout.activity_recipe);
		
		fragmentManager = getSupportFragmentManager();
		if (savedInstanceState == null) {
			initializeFragment();
		}
	}
	
	private void initializeFragment() {
		StepsFragment fragment = StepsFragment.newInstance(
				getIntent().getParcelableExtra(RecipeActivity.class.getSimpleName())
		);
		fragmentManager.beginTransaction()
				.add(R.id.fragment_holder, fragment)
				.commit();
	}
	
	
	@Override
	public void onStepClick(int stepId) {
		replaceFragment(
				SingleStepFragment.newInstance(stepId)
		);
	}
	
	@Override
	public void onIngredientClick() {
		replaceFragment(
				IngredientsFragment.newInstance()
		);
	}
	
	private void replaceFragment(Fragment fragment) {
		fragmentManager.beginTransaction()
				.replace(R.id.fragment_holder, fragment)
				.addToBackStack(null)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();
	}
	
	
	@Override
	public void onPrevious(int currentStepId) {
		changeCurrentStep(
				SingleStepFragment.newInstance(currentStepId - 1)
		);
	}
	
	@Override
	public void onNext(int currentStepId) {
		changeCurrentStep(
				SingleStepFragment.newInstance(currentStepId + 1)
		);
	}
	
	private void changeCurrentStep(Fragment fragment) {
		fragmentManager.beginTransaction()
				.replace(R.id.fragment_holder, fragment)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}
}