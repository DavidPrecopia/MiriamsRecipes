package com.example.miriamsrecipes.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Recipe implements Parcelable {
	
	private int id;
	
	private String name;
	private int servings;
	private String image;
	
	private List<IngredientsItem> ingredients;
	private List<StepsItem> steps;
	
	
	public int getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public int getServings(){
		return servings;
	}
	
	public String getImage(){
		return image;
	}
	
	public List<IngredientsItem> getIngredients(){
		return ingredients;
	}

	public List<StepsItem> getSteps(){
		return steps;
	}
	
	
	/**
	 * Implementation of Parcelable
	 */
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(id);
		parcel.writeString(name);
		parcel.writeInt(servings);
		parcel.writeString(image);
		parcel.writeTypedList(ingredients);
		parcel.writeTypedList(steps);
	}
	
	private Recipe(Parcel in) {
		id = in.readInt();
		name = in.readString();
		servings = in.readInt();
		image = in.readString();
		ingredients = in.createTypedArrayList(IngredientsItem.CREATOR);
		steps = in.createTypedArrayList(StepsItem.CREATOR);
	}
	
	public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
		@Override
		public Recipe createFromParcel(Parcel in) {
			return new Recipe(in);
		}
		
		@Override
		public Recipe[] newArray(int size) {
			return new Recipe[size];
		}
	};
}