package com.example.miriamsrecipes.activities.recipe;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.databinding.ActivityRecipeBinding;

public class RecipeActivity extends AppCompatActivity
		implements StepsFragment.FragmentClickListener, StepsFragment.IngredientClickListener,
		SingleStepFragment.ChangeStepListener {
	
	private FragmentManager fragmentManager;
	
	private boolean dualPane;
	
	private static final String BACKSTACK_TAG = "backstack_tag";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityRecipeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe);
		
		dualPane = (binding.masterDetailLayout != null);
		
		fragmentManager = getSupportFragmentManager();
		if (!dualPane && savedInstanceState == null) {
			initializeFragment(binding.fragmentHolder.getId());
		}
		
		// testing
		if (dualPane) {
			initializeFragment(binding.masterHolder.getId());
			
			SingleStepFragment fragment = SingleStepFragment.newInstance(1);
			fragmentManager.beginTransaction().add(binding.detailHolder.getId(), fragment).commit();
		}
	}
	
	private void initializeFragment(int layoutId) {
		StepsFragment fragment = StepsFragment.newInstance(
				getIntent().getParcelableExtra(RecipeActivity.class.getSimpleName())
		);
		fragmentManager.beginTransaction()
				.add(layoutId, fragment)
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
				.addToBackStack(BACKSTACK_TAG)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();
	}
	
	
	@Override
	public void onPrevious(int currentStepId) {
		changeCurrentStep(
				SingleStepFragment.newInstance((currentStepId - 1))
		);
	}
	
	@Override
	public void onNext(int currentStepId) {
		changeCurrentStep(
				SingleStepFragment.newInstance((currentStepId + 1))
		);
	}
	
	private void changeCurrentStep(Fragment fragment) {
		fragmentManager.popBackStack(BACKSTACK_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		fragmentManager.beginTransaction()
				.replace(R.id.fragment_holder, fragment)
				.addToBackStack(BACKSTACK_TAG)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}
	
	
	/**
	 * @return true if Up navigation completed successfully <b>and</b> this Activity was finished, false otherwise.
	 */
	@Override
	public boolean onSupportNavigateUp() {
		if (fragmentManager.getBackStackEntryCount() > 0) {
			fragmentManager.popBackStack();
			return false;
		} else {
			return super.onSupportNavigateUp();
		}
	}
}