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
import com.example.miriamsrecipes.databinding.FragmentIndividualStepBinding;

import java.util.Objects;

public class IndividualStepFragment extends Fragment {
	
	private static final String STEP_ID_KEY = "step_key";

	private int stepId;
	
	private SharedFragmentsViewModel viewModel;
	private FragmentIndividualStepBinding binding;
	
	
	public IndividualStepFragment() {
	}
	
	public static IndividualStepFragment newInstance(int stepId) {
		IndividualStepFragment fragment = new IndividualStepFragment();
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
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_individual_step, container, false);
		
		// use step ID to get single recipe
		
		binding.ivStepImage.setImageResource(R.drawable.generic_cooking_picture);
		
		return binding.getRoot();
	}
	
	
	
	@Override
	public void onDetach() {
		super.onDetach();
	}
}
