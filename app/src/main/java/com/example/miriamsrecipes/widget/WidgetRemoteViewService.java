package com.example.miriamsrecipes.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public final class WidgetRemoteViewService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new MyRemoteViewsFactory(getApplication());
	}
}
