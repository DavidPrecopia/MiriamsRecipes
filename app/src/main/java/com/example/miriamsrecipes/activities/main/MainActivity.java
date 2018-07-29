package com.example.miriamsrecipes.activities.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.miriamsrecipes.R;

public class MainActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ViewModelFactory factory = new ViewModelFactory(getApplication());
		MainViewModel viewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
	}
}
