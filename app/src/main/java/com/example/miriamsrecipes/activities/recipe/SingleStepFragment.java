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
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
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
	
	private ChangeStepListener changeStepListener;
	
	private SimpleExoPlayer simpleExoPlayer;
	
	// ExoPlayer state management
	private boolean playWhenReady = true;
	
	private int currentWindow = 0;
	private long playbackPosition = 0;
	
	
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
		binding.videoPlayer.setVisibility(View.VISIBLE);
		
		videoPlayer();
		bindDescription();
		setStepChangeListeners();
		bindStepIndicator();
	}
	
	private void videoPlayer() {
		/**
		 * Roughly a RenderersFactory creates renderers for timestamp synchronized rendering of video, audio and text (subtitles).
		 * The TrackSelector is responsible for selecting from the available audio, video and text tracks.
		 * The LoadControl manages buffering of the player.
		 */
		simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
				new DefaultRenderersFactory(getContext()),
				new DefaultTrackSelector(),
				new DefaultLoadControl()
		);
		
		binding.videoPlayer.setPlayer(simpleExoPlayer);
		
		simpleExoPlayer.setPlayWhenReady(playWhenReady);
		simpleExoPlayer.seekTo(currentWindow, playbackPosition);
		
		DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(Objects.requireNonNull(getContext()), Util.getUserAgent(getContext(), getContext().getApplicationInfo().loadLabel(getContext().getPackageManager()).toString()));
		
		ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(defaultDataSourceFactory).createMediaSource(Uri.parse(step.getVideoURL()));
		
		simpleExoPlayer.prepare(mediaSource);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		binding.videoPlayer.setPlayer(null);
		simpleExoPlayer.release();
		simpleExoPlayer = null;
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
