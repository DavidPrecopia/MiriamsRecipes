package com.example.miriamsrecipes.activities;

import android.app.Application;

import com.example.miriamsrecipes.BuildConfig;

import timber.log.Timber;

public final class MyApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		}
	}
}
