package com.example.miriamsrecipes.activities.recipe;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.databinding.ActivityRecipeBinding;

import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity
		implements StepsFragment.FragmentClickListener, StepsFragment.IngredientClickListener,
		SingleStepFragment.ChangeStepListener {
	
	private ActivityRecipeBinding binding;
	
	private FragmentManager fragmentManager;
	
	private boolean dualPane;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe);
		init(savedInstanceState == null);
	}
	
	private void init(boolean newActivity) {
		initializeFields();
		setUpLayout(newActivity);
	}
	
	private void initializeFields() {
		dualPane = (binding.masterDetailLayout != null);
		fragmentManager = getSupportFragmentManager();
	}
	
	
	private void setUpLayout(boolean newActivity) {
		if (! dualPane && newActivity) {
			initializeFragment(getStepsFragment(), binding.fragmentHolder.getId());
		} else if (dualPane) {
			initializeDualPaneFragments();
		} else {
			Timber.e(getString(R.string.error_unknown_layout_state));
		}
	}
	
	private void initializeDualPaneFragments() {
		initializeFragment(
				getStepsFragment(),
				binding.masterHolder.getId()
		);
		initializeFragment(
				IngredientsFragment.newInstance(dualPane),
				binding.detailHolder.getId()
		);
	}
	
	private StepsFragment getStepsFragment() {
		return StepsFragment.newInstance(
				getIntent().getParcelableExtra(RecipeActivity.class.getSimpleName())
		);
	}
	
	
	private void initializeFragment(Fragment fragment, int layoutId) {
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
				IngredientsFragment.newInstance(dualPane)
		);
	}
	
	private void replaceFragment(Fragment fragment) {
		fragmentManager.beginTransaction()
				.replace(R.id.fragment_holder, fragment)
				.addToBackStack(getString(R.string.fragments_backstack_tag))
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
}