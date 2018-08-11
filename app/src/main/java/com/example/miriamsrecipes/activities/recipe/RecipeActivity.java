package com.example.miriamsrecipes.activities.recipe;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.databinding.ActivityRecipeBinding;

public class RecipeActivity extends AppCompatActivity
		implements StepsFragment.FragmentClickListener, StepsFragment.IngredientClickListener,
		SingleStepFragment.ChangeStepListener {
	
	private final CountingIdlingResource countingIdlingResource = new CountingIdlingResource(RecipeActivity.class.getSimpleName());
	
	private ActivityRecipeBinding binding;
	
	private FragmentManager fragmentManager;
	
	private int recipeId;
	
	private boolean masterDetailLayout;
	private boolean newActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe);
		init(savedInstanceState == null);
	}
	
	private void init(boolean newActivity) {
		setUpViewModel();
		initializeFields(newActivity);
		setUpLayout();
	}
	
	private void setUpViewModel() {
		countingIdlingResource.increment();
		recipeId = getIntent().getIntExtra(RecipeActivity.class.getSimpleName(), 0);
		ViewModelFactory factory = new ViewModelFactory(
				getApplication(),
				recipeId
		);
		RecipeViewModel viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);
		viewModel.getRecipe().observe(this, recipe -> countingIdlingResource.decrement());
	}
	
	
	private void initializeFields(boolean newActivity) {
		masterDetailLayout = getResources().getBoolean(R.bool.master_detail_layout);
		fragmentManager = getSupportFragmentManager();
		this.newActivity = newActivity;
	}
	
	
	private void setUpLayout() {
		if (! masterDetailLayout && newActivity) {
			initializeFragment(StepsFragment.newInstance(recipeId), binding.fragmentHolder.getId());
		} else if (masterDetailLayout) {
			initializeDualPaneFragments();
		}
	}
	
	private void initializeDualPaneFragments() {
		initializeFragment(
				StepsFragment.newInstance(recipeId),
				binding.masterHolder.getId()
		);
		initializeFragment(
				IngredientsFragment.newInstance(masterDetailLayout, recipeId),
				binding.detailHolder.getId()
		);
	}
	
	
	private void initializeFragment(Fragment fragment, int layoutId) {
		fragmentManager.beginTransaction()
				.add(layoutId, fragment)
				.commit();
	}
	
	
	@Override
	public void onIngredientClick() {
		IngredientsFragment fragment = IngredientsFragment.newInstance(masterDetailLayout, recipeId);
		checkIfDualPane(fragment);
	}
	
	@Override
	public void onStepClick(int stepId) {
		SingleStepFragment fragment = SingleStepFragment.newInstance(stepId, masterDetailLayout, recipeId);
		checkIfDualPane(fragment);
	}
	
	
	@Override
	public void onPrevious(int currentStepId) {
		SingleStepFragment fragment = SingleStepFragment.newInstance((currentStepId - 1), masterDetailLayout, recipeId);
		checkIfDualPaneChangeStep(fragment);
	}
	
	@Override
	public void onNext(int currentStepId) {
		SingleStepFragment fragment = SingleStepFragment.newInstance((currentStepId + 1), masterDetailLayout, recipeId);
		checkIfDualPaneChangeStep(fragment);
	}
	
	
	private void checkIfDualPane(Fragment fragment) {
		if (masterDetailLayout) {
			replaceDetailFragment(fragment);
		} else {
			replaceFragment(fragment);
		}
	}
	
	private void checkIfDualPaneChangeStep(SingleStepFragment fragment) {
		if (masterDetailLayout) {
			replaceDetailFragment(fragment);
		} else {
			changeCurrentStep(fragment);
		}
	}
	
	
	private void replaceFragment(Fragment fragment) {
		fragmentManager.beginTransaction()
				.replace(R.id.fragment_holder, fragment)
				.addToBackStack(getString(R.string.fragments_backstack_tag))
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();
	}
	
	private void replaceDetailFragment(Fragment fragment) {
		fragmentManager.beginTransaction()
				.replace(binding.detailHolder.getId(), fragment)
				.commit();
	}
	
	private void changeCurrentStep(SingleStepFragment fragment) {
		fragmentManager.popBackStack(getString(R.string.fragments_backstack_tag), FragmentManager.POP_BACK_STACK_INCLUSIVE);
		fragmentManager.beginTransaction()
				.replace(R.id.fragment_holder, fragment)
				.addToBackStack(getString(R.string.fragments_backstack_tag))
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
	
	
	public CountingIdlingResource getCountingIdlingResource() {
		return countingIdlingResource;
	}
}