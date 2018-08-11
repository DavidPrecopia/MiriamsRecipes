package com.example.miriamsrecipes.activities.recipe;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.databinding.FragmentIngredientsBinding;
import com.example.miriamsrecipes.databinding.ListItemIngredientBinding;
import com.example.miriamsrecipes.datamodel.IngredientsItem;

import java.util.List;

public class IngredientsFragment extends Fragment {
	
	private static final String RECIPE_ID_KEY = "recipe_id_key";
	private static final String MASTER_DETAIL_LAYOUT_KEY = "master_detail_layout_key";
	
	private RecipeViewModel viewModel;
	private FragmentIngredientsBinding binding;
	
	private boolean masterDetailLayout;
	
	
	public IngredientsFragment() {
	}
	
	public static IngredientsFragment newInstance(boolean dualPane, int recipeId) {
		IngredientsFragment fragment = new IngredientsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(RECIPE_ID_KEY, recipeId);
		bundle.putBoolean(MASTER_DETAIL_LAYOUT_KEY, dualPane);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		masterDetailLayout = getArguments().getBoolean(MASTER_DETAIL_LAYOUT_KEY);
		int recipeId = getArguments().getInt(RECIPE_ID_KEY);
		ViewModelFactory factory = new ViewModelFactory(getActivity().getApplication(), recipeId);
		viewModel = ViewModelProviders.of(getActivity(), factory).get(RecipeViewModel.class);
	}
	
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredients, container, false);
		progressBarVisibility(View.VISIBLE);
		observeViewModel();
		return binding.getRoot();
	}
	
	private void progressBarVisibility(int visibility) {
		binding.progressBar.setVisibility(visibility);
	}
	
	
	private void observeViewModel() {
		viewModel.getRecipe().observe(this, recipe -> {
			init();
			progressBarVisibility(View.GONE);
		});
	}
	
	private void init() {
		setUpToolbar();
		setUpRecyclerView();
	}
	
	private void setUpToolbar() {
		((AppCompatActivity) getActivity()).setSupportActionBar(binding.appBar.toolbar);
		ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
		if (! masterDetailLayout) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		binding.appBar.toolbar.setTitle(R.string.title_ingredients_fragment);
	}
	
	private void setUpRecyclerView() {
		RecyclerView recyclerView = binding.recyclerView;
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
		
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.addItemDecoration(getDividerItemDecoration(recyclerView, linearLayoutManager));
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(new IngredientAdapter(viewModel.getRecipe().getValue().getIngredients()));
	}
	
	private RecyclerView.ItemDecoration getDividerItemDecoration(RecyclerView recyclerView, LinearLayoutManager layoutManager) {
		return new DividerItemDecoration(
				recyclerView.getContext(),
				layoutManager.getOrientation()
		);
	}
	
	
	final class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
		
		private final List<IngredientsItem> ingredientsList;
		
		IngredientAdapter(List<IngredientsItem> ingredientsList) {
			this.ingredientsList = ingredientsList;
		}
		
		
		@NonNull
		@Override
		public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			return new IngredientViewHolder(
					ListItemIngredientBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
			);
		}
		
		@Override
		public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
			holder.bindView();
		}
		
		@Override
		public int getItemCount() {
			return ingredientsList.size();
		}
		
		
		final class IngredientViewHolder extends RecyclerView.ViewHolder {
			
			private final ListItemIngredientBinding binding;
			
			IngredientViewHolder(ListItemIngredientBinding binding) {
				super(binding.getRoot());
				this.binding = binding;
			}
			
			private void bindView() {
				binding.setIngredient(ingredientsList.get(getAdapterPosition()));
				binding.executePendingBindings();
			}
		}
	}
}
