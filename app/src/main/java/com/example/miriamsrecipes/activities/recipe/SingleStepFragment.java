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
import android.widget.Toast;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.databinding.FragmentSingleStepBinding;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class SingleStepFragment extends Fragment {
	
	private static final String STEP_ID_KEY = "step_key";
	
	private int stepId;
	private SharedFragmentsViewModel viewModel;
	
	private FragmentSingleStepBinding binding;
	
	private ChangeStepListener changeStepListener;
	
	
	public SingleStepFragment() {
	}
	
	public static SingleStepFragment newInstance(int stepId) {
		SingleStepFragment fragment = new SingleStepFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(STEP_ID_KEY, stepId);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stepId = Objects.requireNonNull(getArguments()).getInt(STEP_ID_KEY);
		viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedFragmentsViewModel.class);
	}
	
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_step, container, false);
		
		// use step ID to get single recipe
		
		// Placeholder
		binding.ivPicture.setImageResource(R.drawable.generic_cooking_picture);
		
		setStepChangeListeners();
		bindStepIndicator();
		
		return binding.getRoot();
	}
	
	private void setStepChangeListeners() {
		try {
			changeStepListener = (ChangeStepListener) getActivity();
		} catch (ClassCastException e) {
			Timber.e(e);
		}
		setPreviousStepClickListener();
		setNextStepClickListener();
	}
	
	private void setPreviousStepClickListener() {
		binding.ivPreviousArrow.setOnClickListener(view -> {
			if (stepId == 0) {
				Toasty.info(Objects.requireNonNull(getContext()), "This is the first step", Toast.LENGTH_SHORT).show();
				return;
			}
			changeStepListener.onPrevious(this.stepId);
		});
	}
	
	private void setNextStepClickListener() {
		binding.ivNextArrow.setOnClickListener(view -> {
			if (stepId == (viewModel.getRecipe().getSteps().size() - 1)) {
				Toasty.info(Objects.requireNonNull(getContext()), "You've reached the last step", Toast.LENGTH_SHORT).show();
				return;
			}
			changeStepListener.onNext(this.stepId);
		});
	}
	
	private void bindStepIndicator() {
		binding.tvCurrentStep.setText(String.valueOf(stepId + 1));
		binding.tvTotalSteps.setText(String.valueOf(viewModel.getRecipe().getSteps().size()));
	}
	
	
	interface ChangeStepListener {
		void onPrevious(int currentStepId);
		
		void onNext(int currentStepId);
	}
}
