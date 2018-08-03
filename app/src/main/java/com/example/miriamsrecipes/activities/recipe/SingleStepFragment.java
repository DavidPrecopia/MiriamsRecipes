package com.example.miriamsrecipes.activities.recipe;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.databinding.FragmentSingleStepBinding;
import com.example.miriamsrecipes.datamodel.StepItem;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class SingleStepFragment extends Fragment {
	
	private static final String STEP_ID_KEY = "step_key";
	
	private StepItem step;
	
	private SharedFragmentsViewModel viewModel;
	private FragmentSingleStepBinding binding;
	
	private SimpleExoPlayer player;
	// ExoPlayer state management
	private boolean playWhenReady = true;
	private long playbackPosition = 0;
	
	private ChangeStepListener changeStepListener;
	
	
	public SingleStepFragment() {
	}
	
	public static SingleStepFragment newInstance(int stepId) {
		SingleStepFragment fragment = new SingleStepFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(STEP_ID_KEY, stepId);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int stepId = Objects.requireNonNull(getArguments()).getInt(STEP_ID_KEY);
		viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedFragmentsViewModel.class);
		step = viewModel.getRecipe().getSteps().get(stepId);
	}
	
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_step, container, false);
		init();
		return binding.getRoot();
	}
	
	private void init() {
		pickMedia();
		bindDescription();
		setStepChangeListeners();
		bindStepIndicator();
	}
	
	
	private void pickMedia() {
		setUpVideoPlayer();
	}
	
	
	private void setUpVideoPlayer() {
		player = getPlayer();
		player.setPlayWhenReady(playWhenReady);
		player.seekTo(0, playbackPosition);
		player.prepare(getMediaSource());
		
		binding.videoPlayer.setVisibility(View.VISIBLE);
		binding.videoPlayer.setPlayer(player);
	}
	
	@NonNull
	private SimpleExoPlayer getPlayer() {
		return ExoPlayerFactory.newSimpleInstance(
				new DefaultRenderersFactory(getContext()),
				new DefaultTrackSelector(),
				new DefaultLoadControl()
		);
	}
	
	private MediaSource getMediaSource() {
		return new ExtractorMediaSource.Factory(getDataSourceFactory())
				.createMediaSource(Uri.parse(step.getVideoURL()));
	}
	
	private DataSource.Factory getDataSourceFactory() {
		return new DefaultDataSourceFactory(
				Objects.requireNonNull(getContext()),
				Util.getUserAgent(
						getContext(),
						getContext().getApplicationInfo().loadLabel(getContext().getPackageManager()).toString()
				)
		);
	}
	
	
	@Override
	public void onDetach() {
		super.onDetach();
		releasePlayer();
	}
	
	private void releasePlayer() {
		player.release();
		player = null;
	}
	
	
	private void bindDescription() {
		binding.tvDescription.setText(step.getDescription());
	}
	
	
	private void setStepChangeListeners() {
		try {
			changeStepListener = (ChangeStepListener) getActivity();
		} catch (ClassCastException e) {
			Timber.e(e);
		}
		setPreviousStepClickListener();
		setNextStepClickListener();
	}
	
	private void setPreviousStepClickListener() {
		binding.ivPreviousArrow.setOnClickListener(view -> {
			if (step.getId() == 0) {
				infoToast(getString(R.string.error_reached_first_step));
				return;
			}
			changeStepListener.onPrevious(step.getId());
		});
	}
	
	private void setNextStepClickListener() {
		binding.ivNextArrow.setOnClickListener(view -> {
			if (step.getId() == (viewModel.getRecipe().getSteps().size() - 1)) {
				infoToast(getString(R.string.error_reached_last_step));
				return;
			}
			changeStepListener.onNext(step.getId());
		});
	}
	
	private void bindStepIndicator() {
		binding.tvCurrentStep.setText(String.valueOf(step.getId() + 1));
		binding.tvTotalSteps.setText(String.valueOf(viewModel.getRecipe().getSteps().size()));
	}
	
	
	private void infoToast(String message) {
		Toasty.info(Objects.requireNonNull(getContext()), message, Toast.LENGTH_SHORT).show();
	}
	
	
	interface ChangeStepListener {
		void onPrevious(int currentStepId);
		
		void onNext(int currentStepId);
	}
}
