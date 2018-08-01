package com.example.miriamsrecipes.activities.recipe;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import java.util.Objects;

public class IngredientsFragment extends Fragment {
	
	private SharedFragmentsViewModel viewModel;
	private FragmentIngredientsBinding binding;
	
	
	public IngredientsFragment() {
	}
	
	public static IngredientsFragment newInstance() {
		return new IngredientsFragment();
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedFragmentsViewModel.class);
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredients, container, false);
		setUpRecyclerView();
		return binding.getRoot();
	}
	
	private void setUpRecyclerView() {
		RecyclerView recyclerView = binding.recyclerView;
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(new IngredientAdapter(viewModel.getRecipe().getIngredients()));
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
			
			private ListItemIngredientBinding binding;
			
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
