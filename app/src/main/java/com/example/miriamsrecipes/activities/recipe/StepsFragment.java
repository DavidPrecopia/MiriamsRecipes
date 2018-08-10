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
import com.example.miriamsrecipes.datamodel.StepItem;

import java.util.List;

import timber.log.Timber;

public class StepsFragment extends Fragment {
	
	private static final String RECIPE_ID_KEY = "recipe_id_key";
	
	private FragmentStepsBinding binding;
	private RecipeViewModel viewModel;
	
	private FragmentClickListener fragmentClickListener;
	private IngredientClickListener ingredientClickListener;
	
	
	public StepsFragment() {
	}
	
	public static StepsFragment newInstance(int recipeId) {
		StepsFragment fragment = new StepsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(RECIPE_ID_KEY, recipeId);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int recipeId = getArguments().getInt(RECIPE_ID_KEY);
		ViewModelFactory factory = new ViewModelFactory(getActivity().getApplication(), recipeId);
		viewModel = ViewModelProviders.of(getActivity(), factory).get(RecipeViewModel.class);
	}
	
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container, false);
		progressBarVisibility(View.VISIBLE);
		observeViewModel();
		return binding.getRoot();
	}
	
	private void observeViewModel() {
		viewModel.getRecipe().observe(this, recipe -> {
			init();
			progressBarVisibility(View.GONE);
		});
	}
	
	private void init() {
		setUpToolbar();
		setFragmentClickListener();
		setIngredientsClickListener();
		setUpRecyclerView();
	}
	
	private void setUpToolbar() {
		((AppCompatActivity) getActivity()).setSupportActionBar(binding.appBar.toolbar);
		ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(viewModel.getRecipe().getValue().getName());
	}
	
	private void setFragmentClickListener() {
		try {
			fragmentClickListener = (FragmentClickListener) getActivity();
		} catch (ClassCastException e) {
			Timber.e(e);
		}
	}
	
	private void setIngredientsClickListener() {
		try {
			ingredientClickListener = (IngredientClickListener) getActivity();
		} catch (ClassCastException e) {
			Timber.e(e);
		}
		binding.ingredientListItem.setOnClickListener(view -> ingredientClickListener.onIngredientClick());
	}
	
	private void setUpRecyclerView() {
		RecyclerView recyclerView = binding.recyclerView;
		recyclerView.setNestedScrollingEnabled(false);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(new StepsAdapter(viewModel.getRecipe().getValue().getSteps()));
	}
	
	
	private void progressBarVisibility(int visibility) {
		binding.progressBar.setVisibility(visibility);
	}
	
	
	@Override
	public void onDetach() {
		super.onDetach();
		fragmentClickListener = null;
		ingredientClickListener = null;
	}
	
	
	final class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {
		
		private int lastSelectedPosition = RecyclerView.NO_POSITION;
		
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
				highlightStep();
				binding.setStep(stepsList.get(getAdapterPosition()));
				binding.executePendingBindings();
			}
			
			private void highlightStep() {
				if (lastSelectedPosition == getAdapterPosition()) {
					binding.rootLayout.setSelected(true);
				} else {
					binding.rootLayout.setSelected(false);
				}
			}
			
			
			@Override
			public void onClick(View view) {
				manageHighlightedStep();
				fragmentClickListener.onStepClick(
						stepsList.get(getAdapterPosition()).getId()
				);
			}
			
			private void manageHighlightedStep() {
				int oldPosition = lastSelectedPosition;
				lastSelectedPosition = getAdapterPosition();
				notifyItemChanged(oldPosition);
				notifyItemChanged(lastSelectedPosition);
			}
		}
	}
	
	
	interface FragmentClickListener {
		void onStepClick(int stepId);
	}
	
	interface IngredientClickListener {
		void onIngredientClick();
	}
}
