package com.example.miriamsrecipes.activities.widgetconfig;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.miriamsrecipes.R;

public class IngredientsWidgetConfigActivity extends AppCompatActivity {
	
	public static final String SHARED_PERF_NAME = IngredientsWidgetConfigActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingredients_widget_config);
	}
}
