package com.example.miriamsrecipes.datamodel;

public class IngredientsItem {
	
	private final double quantity;
	private final String measure;
	
	private final String ingredient;
	
	
	public IngredientsItem(double quantity, String measure, String ingredient) {
		this.quantity = quantity;
		this.measure = measure;
		this.ingredient = ingredient;
	}
	
	
	public double getQuantity() {
		return quantity;
	}
	
	public String getMeasure() {
		return measure;
	}
	
	public String getIngredient() {
		return ingredient;
	}
}