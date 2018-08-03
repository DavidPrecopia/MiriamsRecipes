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
	
	private SimpleExoPlayer exoPlayer;
	private boolean playWhenReady;
	private long playbackPosition;
	
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
		restorePlayerState(savedInstanceState);
		return binding.getRoot();
	}
	
	private void restorePlayerState(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			return;
		}
		playbackPosition = savedInstanceState.getLong(getString(R.string.key_player_position));
		playWhenReady = savedInstanceState.getBoolean(getString(R.string.key_play_when_ready));
	}
	
	
	private void init() {
		pickMedia();
		bindDescription();
		setStepChangeListeners();
		bindStepIndicator();
	}
	
	
	private void pickMedia() {
		playWhenReady = true;
		setUpVideoPlayer();
	}
	
	
	private void setUpVideoPlayer() {
		exoPlayer = getExoPlayer();
		exoPlayer.prepare(getMediaSource());
		
		binding.videoPlayer.setVisibility(View.VISIBLE);
		binding.videoPlayer.setPlayer(exoPlayer);
	}
	
	@NonNull
	private SimpleExoPlayer getExoPlayer() {
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
	
	
	@Override
	public void onStart() {
		super.onStart();
		if (exoPlayer == null) {
			setUpVideoPlayer();
		}
		exoPlayer.setPlayWhenReady(playWhenReady);
		exoPlayer.seekTo(0, playbackPosition);
	}
	
	
	@Override
	public void onStop() {
		super.onStop();
		releasePlayer();
	}
	
	private void releasePlayer() {
		savePlayerState();
		exoPlayer.release();
		exoPlayer = null;
	}
	
	private void savePlayerState() {
		playbackPosition = exoPlayer.getCurrentPosition();
		playWhenReady = exoPlayer.getPlayWhenReady();
	}
	
	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(getString(R.string.key_player_position), exoPlayer.getCurrentPosition());
		outState.putBoolean(getString(R.string.key_play_when_ready), exoPlayer.getPlayWhenReady());
	}
	
	interface ChangeStepListener {
		void onPrevious(int currentStepId);
		
		void onNext(int currentStepId);
	}
}
