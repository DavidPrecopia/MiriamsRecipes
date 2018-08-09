package com.example.miriamsrecipes.activities.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.databinding.ListItemRecipeBinding;
import com.example.miriamsrecipes.util.GlideApp;

import java.util.ArrayList;
import java.util.List;

public final class RecipeInfoAdapter extends RecyclerView.Adapter<RecipeInfoAdapter.RecipeViewHolder> {
	
	private final List<RecipeInfo> recipes;
	
	private final RecipeInfoItemClickListener listener;
	
	public RecipeInfoAdapter(RecipeInfoItemClickListener listener) {
		this.recipes = new ArrayList<>();
		this.listener = listener;
	}
	
	@NonNull
	@Override
	public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new RecipeViewHolder(
				ListItemRecipeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
		);
	}
	
	@Override
	public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
		holder.bindView();
	}
	
	public void replaceData(List<RecipeInfo> newRecipes) {
		recipes.clear();
		recipes.addAll(newRecipes);
		notifyDataSetChanged();
	}
	
	@Override
	public int getItemCount() {
		return recipes.size();
	}
	
	
	final class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		
		private final ListItemRecipeBinding binding;
		
		RecipeViewHolder(ListItemRecipeBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
			binding.getRoot().setOnClickListener(this);
		}
		
		
		private void bindView() {
			binding.setRecipe(recipes.get(getAdapterPosition()));
			bindImage();
			binding.executePendingBindings();
		}
		
		private void bindImage() {
			ImageView imageView = binding.ivRecipePicture;
			GlideApp.with(imageView)
					.load(recipes.get(getAdapterPosition()).getImage())
					.error(R.drawable.generic_cooking_picture)
					.placeholder(R.drawable.generic_cooking_picture)
					.into(imageView);
		}
		
		
		@Override
		public void onClick(View view) {
			listener.recipeInfoClick(
					getAdapterPosition()
			);
		}
	}
	
	
	public interface RecipeInfoItemClickListener {
		void recipeInfoClick(int listPosition);
	}
}
