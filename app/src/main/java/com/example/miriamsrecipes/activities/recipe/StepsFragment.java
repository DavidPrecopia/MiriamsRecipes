package com.example.miriamsrecipes.activities.recipe;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.databinding.FragmentStepsBinding;
import com.example.miriamsrecipes.datamodel.Recipe;

import java.util.Objects;

import timber.log.Timber;

public class StepsFragment extends Fragment {
	
	private static final String RECIPE_KEY = "recipe_key";
	
	private FragmentStepsBinding binding;
	private SharedFragmentsViewModel viewModel;
	
	private FragmentClickListener fragmentClickListener;
	
	
	public StepsFragment() {
	}
	
	public static StepsFragment newInstance(Recipe recipe) {
		StepsFragment fragment = new StepsFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(RECIPE_KEY, recipe);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpViewModel();
	}
	
	private void setUpViewModel() {
		ViewModelFactory factory = new ViewModelFactory(Objects.requireNonNull(getArguments()).getParcelable(RECIPE_KEY));
		viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(SharedFragmentsViewModel.class);
	}
	
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container, false);
		
		//
		// observeViewModel || get data from ViewModel
		
		setFragmentClickListener();
		
		return binding.getRoot();
	}
	
	private void setFragmentClickListener() {
		try {
			fragmentClickListener = (FragmentClickListener) getActivity();
		} catch (Exception e) {
			Timber.e(e);
			throw new ClassCastException("The parent Activity must implement FragmentClickListener");
		}
	}
	
	
	@Override
	public void onDetach() {
		super.onDetach();
		fragmentClickListener = null;
	}
	
	
	public interface FragmentClickListener {
		void onStepClick(int stepId);
	}
}
