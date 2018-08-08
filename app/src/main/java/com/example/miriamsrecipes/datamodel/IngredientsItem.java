package com.example.miriamsrecipes.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

public class IngredientsItem implements Parcelable {
	
	private double quantity;
	private String measure;
	
	private String ingredient;
	
	
	public double getQuantity() {
		return quantity;
	}
	
	public String getMeasure() {
		return measure;
	}
	
	public String getIngredient() {
		return ingredient;
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
		
		parcel.writeDouble(quantity);
		parcel.writeString(measure);
		parcel.writeString(ingredient);
	}
	
	private IngredientsItem(Parcel in) {
		quantity = in.readDouble();
		measure = in.readString();
		ingredient = in.readString();
	}
	
	public static final Creator<IngredientsItem> CREATOR = new Creator<IngredientsItem>() {
		@Override
		public IngredientsItem createFromParcel(Parcel in) {
			return new IngredientsItem(in);
		}
		
		@Override
		public IngredientsItem[] newArray(int size) {
			return new IngredientsItem[size];
		}
	};
}