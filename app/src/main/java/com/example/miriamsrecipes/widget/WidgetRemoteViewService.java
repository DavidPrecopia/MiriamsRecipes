package com.example.miriamsrecipes.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.miriamsrecipes.R;

public final class WidgetRemoteViewService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new MyRemoteViewsFactory(
				getApplication(),
				intent.getIntExtra(getApplicationContext().getString(R.string.widget_key_recipe_id), -1)
		);
	}
}