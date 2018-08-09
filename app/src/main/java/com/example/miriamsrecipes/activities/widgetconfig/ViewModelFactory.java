package com.example.miriamsrecipes.activities.widgetconfig;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

final class ViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
	
	private final Application application;
	
	/**
	 * Creates a {@code AndroidViewModelFactory}
	 *
	 * @param application an application to pass in {@link AndroidViewModel}
	 */
	ViewModelFactory(@NonNull Application application) {
		super(application);
		this.application = application;
	}
	
	@NonNull
	@Override
	public IngredientsWidgetViewModel create(@NonNull Class modelClass) {
		return new IngredientsWidgetViewModel(application);
	}
}
