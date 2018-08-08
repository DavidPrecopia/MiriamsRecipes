package com.example.miriamsrecipes.widget;

import android.app.Application;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.miriamsrecipes.R;

import java.util.ArrayList;
import java.util.List;

final class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
	
	private List<String> tempList;
	
	private final Application application;
	
	MyRemoteViewsFactory(Application application) {
		this.tempList = new ArrayList<>();
		this.application = application;
		
		tempList.add("1");
		tempList.add("2");
		tempList.add("3");
		tempList.add("4");
		tempList.add("5");
		tempList.add("6");
		tempList.add("7");
		tempList.add("8");
		tempList.add("9");
		tempList.add("10");
	}
	
	
	@Override
	public int getCount() {
		return tempList.size();
	}
	
	@Override
	public RemoteViews getViewAt(int position) {
		RemoteViews view = new RemoteViews(application.getPackageName(), R.layout.list_view_item_ingredient);
		view.setTextViewText(R.id.widget_tv_ingredient_name, tempList.get(position));
		view.setTextViewText(R.id.widget_tv_ingredient_quantity, tempList.get(position));
		view.setTextViewText(R.id.widget_tv_ingredient_measurement, tempList.get(position));
		return view;
	}
	
	
	@Override
	public void onCreate() {
	
	}
	
	@Override
	public void onDataSetChanged() {
	
	}
	
	@Override
	public void onDestroy() {
	
	}
	
	@Override
	public RemoteViews getLoadingView() {
		return null;
	}
	
	@Override
	public int getViewTypeCount() {
		return 1;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}
}
