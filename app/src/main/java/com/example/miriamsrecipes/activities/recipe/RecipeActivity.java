package com.example.miriamsrecipes.activities.recipe;

import android.arch.lifecycle.ViewModelProviders;
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
	
	private RecipeViewModel viewModel;
	private ActivityRecipeBinding binding;
	
	private FragmentManager fragmentManager;
	
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
		observeViewModel();
	}
	
	/**
	 * Creating the ViewModel in the Activity fixes a crash caused
	 * by rotating the device when the app is in the background.
	 * This issue is similar to a known issue with ViewModel,
	 * https://issuetracker.google.com/issues/72690424#comment10
	 */
	private void setUpViewModel() {
		ViewModelFactory factory = new ViewModelFactory(
				getApplication(),
				getIntent().getIntExtra(RecipeActivity.class.getSimpleName(), 0)
		);
		viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);
	}
	
	private void initializeFields(boolean newActivity) {
		masterDetailLayout = getResources().getBoolean(R.bool.master_detail_layout);
		fragmentManager = getSupportFragmentManager();
		this.newActivity = newActivity;
	}
	
	private void observeViewModel() {
		viewModel.getRecipe().observe(this, recipe -> setUpLayout());
	}
	
	
	
	private void setUpLayout() {
		if (! masterDetailLayout && newActivity) {
			initializeFragment(StepsFragment.newInstance(), binding.fragmentHolder.getId());
		} else if (masterDetailLayout) {
			initializeDualPaneFragments();
		} else {
			throw new IllegalStateException(getString(R.string.error_unknown_layout_state));
		}
	}
	
	private void initializeDualPaneFragments() {
		initializeFragment(
				StepsFragment.newInstance(),
				binding.masterHolder.getId()
		);
		initializeFragment(
				IngredientsFragment.newInstance(masterDetailLayout),
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
		IngredientsFragment fragment = IngredientsFragment.newInstance(masterDetailLayout);
		checkIfDualPane(fragment);
	}
	
	@Override
	public void onStepClick(int stepId) {
		SingleStepFragment fragment = SingleStepFragment.newInstance(stepId, masterDetailLayout);
		checkIfDualPane(fragment);
	}
	
	
	@Override
	public void onPrevious(int currentStepId) {
		SingleStepFragment fragment = SingleStepFragment.newInstance((currentStepId - 1), masterDetailLayout);
		checkIfDualPaneChangeStep(fragment);
	}
	
	@Override
	public void onNext(int currentStepId) {
		SingleStepFragment fragment = SingleStepFragment.newInstance((currentStepId + 1), masterDetailLayout);
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
}