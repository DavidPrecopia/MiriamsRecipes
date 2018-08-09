package com.example.miriamsrecipes.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.miriamsrecipes.R;

public final class IngredientsRemoteViewService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new IngredientsRemoteViewsFactory(
				getApplication(),
				intent.getIntExtra(getApplicationContext().getString(R.string.widget_key_recipe_id), -1)
		);
	}
}