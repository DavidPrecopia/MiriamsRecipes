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
import com.example.miriamsrecipes.datamodel.StepItem;

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
		init(viewModel.getRecipe().getSteps().get(stepId));
		return binding.getRoot();
	}
	
	private void init(StepItem step) {
		bindDescription(step.getDescription());
		setStepChangeListeners();
		bindStepIndicator(step.getId(), viewModel.getRecipe().getSteps().size());
	}
	
	private void bindDescription(String description) {
		binding.tvDescription.setText(description);
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
				infoToast(getString(R.string.error_reached_first_step));
				return;
			}
			changeStepListener.onPrevious(this.stepId);
		});
	}
	
	private void setNextStepClickListener() {
		binding.ivNextArrow.setOnClickListener(view -> {
			if (stepId == (viewModel.getRecipe().getSteps().size() - 1)) {
				infoToast(getString(R.string.error_reached_last_step));
				return;
			}
			changeStepListener.onNext(this.stepId);
		});
	}
	
	private void bindStepIndicator(int stepId, int totalNumberOfSteps) {
		binding.tvCurrentStep.setText(String.valueOf(stepId + 1));
		binding.tvTotalSteps.setText(String.valueOf(totalNumberOfSteps));
	}
	
	
	private void infoToast(String message) {
		Toasty.info(Objects.requireNonNull(getContext()), message, Toast.LENGTH_SHORT).show();
	}
	
	
	interface ChangeStepListener {
		void onPrevious(int currentStepId);
		
		void onNext(int currentStepId);
	}
}
