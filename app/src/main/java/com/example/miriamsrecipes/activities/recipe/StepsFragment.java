package com.example.miriamsrecipes.activities.recipe;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.databinding.FragmentStepsBinding;
import com.example.miriamsrecipes.databinding.ListItemStepBinding;
import com.example.miriamsrecipes.datamodel.Recipe;
import com.example.miriamsrecipes.datamodel.StepItem;

import java.util.List;
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
		init();
		return binding.getRoot();
	}
	
	private void init() {
		setUpToolbar();
		setFragmentClickListener();
		setUpRecyclerView();
	}
	
	private void setUpToolbar() {
		((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(binding.appBar.toolbar);
		ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
		assert actionBar != null;
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(viewModel.getRecipe().getName());
	}
	
	private void setFragmentClickListener() {
		try {
			fragmentClickListener = (FragmentClickListener) getActivity();
		} catch (Exception e) {
			Timber.e(e);
			throw new ClassCastException("The parent Activity must implement FragmentClickListener");
		}
	}
	
	private void setUpRecyclerView() {
		RecyclerView recyclerView = binding.recyclerView;
		recyclerView.setNestedScrollingEnabled(false);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(new StepsAdapter(viewModel.getRecipe().getSteps()));
	}
	
	
	@Override
	public void onDetach() {
		super.onDetach();
		fragmentClickListener = null;
	}
	
	
	final class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {
		
		private final List<StepItem> stepsList;
		
		StepsAdapter(List<StepItem> stepsList) {
			this.stepsList = stepsList;
		}
		
		
		@NonNull
		@Override
		public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			return new StepViewHolder(
					ListItemStepBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
			);
		}
		
		@Override
		public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
			holder.bindViews();
		}
		
		@Override
		public int getItemCount() {
			return stepsList.size();
		}
		
		
		final class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
			
			private final ListItemStepBinding binding;
			
			StepViewHolder(ListItemStepBinding binding) {
				super(binding.getRoot());
				this.binding = binding;
				binding.getRoot().setOnClickListener(this);
			}
			
			void bindViews() {
				binding.setStep(stepsList.get(getAdapterPosition()));
				binding.executePendingBindings();
			}
			
			@Override
			public void onClick(View view) {
				fragmentClickListener.onStepClick(
						stepsList.get(getAdapterPosition()).getId()
				);
			}
		}
	}
	
	
	public interface FragmentClickListener {
		void onStepClick(int stepId);
	}
}
