package com.example.miriamsrecipes.activities.recipe;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.miriamsrecipes.R;

public class RecipeActivity extends AppCompatActivity implements StepsFragment.FragmentClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataBindingUtil.setContentView(this, R.layout.activity_recipe);
		
		if (savedInstanceState == null) {
			initializeFragment();
		}
	}
	
	private void initializeFragment() {
		StepsFragment fragment = StepsFragment.newInstance(
				getIntent().getParcelableExtra(RecipeActivity.class.getSimpleName())
		);
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_holder, fragment)
				.commit();
	}
	
	@Override
	public void onStepClick(int stepId) {
		IndividualStepFragment fragment = IndividualStepFragment.newInstance(stepId);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_holder, fragment)
				.addToBackStack(null)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commit();
	}
}