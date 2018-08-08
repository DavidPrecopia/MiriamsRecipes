package com.example.miriamsrecipes.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public final class MyRemoteViewService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new MyRemoteViewsFactory(getApplication());
	}
}
