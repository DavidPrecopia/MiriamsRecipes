package com.example.miriamsrecipes.activities.recipe;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.miriamsrecipes.R;
import com.example.miriamsrecipes.databinding.FragmentSingleStepBinding;
import com.example.miriamsrecipes.datamodel.StepItem;
import com.example.miriamsrecipes.util.GlideApp;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class SingleStepFragment extends Fragment {
	
	private static final String STEP_ID_KEY = "step_key";
	private static final String MASTER_DETAIL_LAYOUT_KEY = "master_detail_layout_key";
	
	private StepItem step;
	private SharedFragmentsViewModel viewModel;
	private FragmentSingleStepBinding binding;
	
	private PlayerView playerView;
	private ImageView mediaPicture;
	
	private SimpleExoPlayer exoPlayer;
	private long playbackPosition;
	private boolean playWhenReady;
	
	private MediaSessionCompat mediaSession;
	private MediaSessionConnector mediaSessionConnector;
	
	private boolean masterDetailLayout;
	private boolean haveVideo = false;
	
	private ChangeStepListener changeStepListener;
	
	
	public SingleStepFragment() {
	}
	
	public static SingleStepFragment newInstance(int stepId,boolean dualPane) {
		SingleStepFragment fragment = new SingleStepFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(STEP_ID_KEY, stepId);
		bundle.putBoolean(MASTER_DETAIL_LAYOUT_KEY, dualPane);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		masterDetailLayout = getArguments().getBoolean(MASTER_DETAIL_LAYOUT_KEY);
		getStep();
	}
	
	private void getStep() {
		int stepId = getArguments().getInt(STEP_ID_KEY);
		viewModel = ViewModelProviders.of(getActivity()).get(SharedFragmentsViewModel.class);
		step = viewModel.getRecipe().getSteps().get(stepId);
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_step, container, false);
		init();
		restorePlayerState(savedInstanceState);
		return binding.getRoot();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		// Initializing ExoPlayer here per their tutorial.
		if (haveVideo && exoPlayer == null) {
			setUpVideoPlayer();
			exoPlayer.setPlayWhenReady(playWhenReady);
			exoPlayer.seekTo(0, playbackPosition);
		}
	}
	
	
	private void restorePlayerState(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			return;
		}
		playbackPosition = savedInstanceState.getLong(getString(R.string.key_player_position));
		playWhenReady = savedInstanceState.getBoolean(getString(R.string.key_play_when_ready));
	}
	
	
	private void init() {
		getMediaReferences();
		pickMedia();
		if (orientationPortrait() || masterDetailLayout) {
			bindDescription();
			if (! masterDetailLayout) {
				setUpStepNavigation();
			}
		}
	}
	
	
	private void getMediaReferences() {
		playerView = binding.videoPlayer;
		mediaPicture = binding.ivPicture;
	}
	
	
	private void pickMedia() {
		if (isValid(step.getVideoURL())) {
			haveVideo = true;
		} else if (isValid(step.getThumbnailURL())) {
			bindPicture();
		} else {
			bindPicturePlaceholder();
		}
	}
	
	private boolean isValid(String url) {
		return ! TextUtils.isEmpty(url);
	}
	
	
	private void setUpVideoPlayer() {
		playerView.setVisibility(View.VISIBLE);
		
		exoPlayer = getExoPlayerInstance();
		exoPlayer.prepare(getMediaSource());
		exoPlayer.addListener(new ExoPlayerListener());
		playerView.setPlayer(exoPlayer);
		
		setUpMediaSession();
	}
	
	@NonNull
	private SimpleExoPlayer getExoPlayerInstance() {
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
				getContext(),
				Util.getUserAgent(
						getContext(),
						getContext().getApplicationInfo().loadLabel(getContext().getPackageManager()).toString()
				)
		);
	}
	
	private void setUpMediaSession() {
		mediaSession = new MediaSessionCompat(
				getContext(),
				getString(R.string.tag_media_session)
		);
		mediaSessionConnector = new MediaSessionConnector(mediaSession);
		mediaSessionConnector.setPlayer(exoPlayer, null);
	}
	
	
	private void bindPicture() {
		mediaPicture.setVisibility(View.VISIBLE);
		GlideApp.with(getContext())
				.load(step.getThumbnailURL())
				.placeholder(R.drawable.black_placeholder)
				.error(R.drawable.generic_cooking_picture)
				.into(mediaPicture);
	}
	
	private void bindPicturePlaceholder() {
		mediaPicture.setVisibility(View.VISIBLE);
		GlideApp.with(getContext())
				.load(R.drawable.generic_cooking_picture)
				.into(mediaPicture);
	}
	
	
	private void bindDescription() {
		binding.tvDescription.setText(step.getDescription());
	}
	
	
	private void setUpStepNavigation() {
		setStepChangeListeners();
		bindStepIndicator();
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
		Toasty.info(getContext(), message, Toast.LENGTH_SHORT).show();
	}
	
	
	@Override
	public void onStop() {
		super.onStop();
		if (exoPlayer != null) {
			releaseExoPlayerResources();
		}
	}
	
	private void releaseExoPlayerResources() {
		savePlayerState();
		releasePlayer();
		releaseMediaSession();
	}
	
	private void releasePlayer() {
		playerView.setPlayer(null);
		exoPlayer.release();
		exoPlayer = null;
	}
	
	private void releaseMediaSession() {
		mediaSession.release();
		mediaSessionConnector.setPlayer(null, null);
	}
	
	private void savePlayerState() {
		Timber.d(exoPlayer.getCurrentPosition() + "\n" + exoPlayer.getPlayWhenReady());
		playbackPosition = exoPlayer.getCurrentPosition();
		playWhenReady = exoPlayer.getPlayWhenReady();
	}
	
	
	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		Timber.d("onSaveInstanceState");
		super.onSaveInstanceState(outState);
		if (exoPlayer != null) {
			outState.putLong(getString(R.string.key_player_position), exoPlayer.getCurrentPosition());
			outState.putBoolean(getString(R.string.key_play_when_ready), exoPlayer.getPlayWhenReady());
		}
	}
	
	
	private boolean orientationPortrait() {
		return getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}
	
	
	private final class ExoPlayerListener implements Player.EventListener {
		
		@Override
		public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
		
		}
		
		@Override
		public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
		
		}
		
		@Override
		public void onLoadingChanged(boolean isLoading) {
		
		}
		
		@Override
		public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
			if ((playbackState == Player.STATE_READY) && playWhenReady) {
				// Video playing
				SingleStepFragment.this.exoPlayer.setPlayWhenReady(true);
			} else if ((playbackState == Player.STATE_READY)) {
				// Video paused
				SingleStepFragment.this.exoPlayer.setPlayWhenReady(false);
			}
		}
		
		@Override
		public void onRepeatModeChanged(int repeatMode) {
		
		}
		
		@Override
		public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
		
		}
		
		@Override
		public void onPlayerError(ExoPlaybackException error) {
		
		}
		
		@Override
		public void onPositionDiscontinuity(int reason) {
		
		}
		
		@Override
		public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
		
		}
		
		@Override
		public void onSeekProcessed() {
		
		}
	}
	
	interface ChangeStepListener {
		void onPrevious(int currentStepId);
		
		void onNext(int currentStepId);
	}
}
