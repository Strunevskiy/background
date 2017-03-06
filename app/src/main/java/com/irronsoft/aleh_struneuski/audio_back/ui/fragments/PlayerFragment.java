package com.irronsoft.aleh_struneuski.audio_back.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;
import com.irronsoft.aleh_struneuski.audio_back.constants.ProjectConstants;
import com.irronsoft.aleh_struneuski.audio_back.ui.activities.PlayerListActivity;
import com.irronsoft.aleh_struneuski.audio_back.utils.BlurBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Arrays;
import java.util.Set;


import static android.media.MediaPlayer.*;

/**
 * Created by alehstruneuski on 11/25/16.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener {

    private PlayerListActivity playerListActivity;

    private TextView mSelectedTrackTitle;

    private ImageView mSelectedTrackImage;
    private ImageView mBlurePlayer;
    private ImageView mPlayerControl;

    private ImageView mPlayerControlPrev;
    private ImageView mPlayerControlNext;

    private Target loadtarget;
    private Track trackOnLoad;
    private int currentTrack;

    private SimpleExoPlayer exoPlayer;
    private DefaultHttpDataSourceFactory dataSourceFactory;
    private ExtractorsFactory extractor;
    private MediaSource audioSource;
    private CacheDataSourceFactory cacheDataSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        trackOnLoad = getArguments().getParcelable("track");
        return inflater.inflate(R.layout.player_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPlayerControl = (ImageView) getView().findViewById(R.id.player_control);
        mPlayerControl.setOnClickListener(this);

        mPlayerControlPrev = (ImageView) getView().findViewById(R.id.player_control_prev);
        mPlayerControlPrev.setOnClickListener(this);

        mPlayerControlNext = (ImageView) getView().findViewById(R.id.player_control_next);
        mPlayerControlNext.setOnClickListener(this);


        mSelectedTrackTitle = (TextView) getView().findViewById(R.id.selected_track_title);
        mSelectedTrackImage = (ImageView) getView().findViewById(R.id.selected_track_image);
        mBlurePlayer = (ImageView) getView().findViewById(R.id.image_bluer_player);


        Cache cache = new SimpleCache(getContext().getApplicationContext().getCacheDir(), new NoOpCacheEvictor());


        Set<String> keys = cache.getKeys();
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();

        exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext().getApplicationContext(), trackSelector, loadControl);
        extractor = new DefaultExtractorsFactory();
        dataSourceFactory = new DefaultHttpDataSourceFactory("Android");
        cacheDataSource = new CacheDataSourceFactory(cache, dataSourceFactory, 0);


        exoPlayer.addListener(new ExoPlayer.EventListener() {

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                if (playbackState == ExoPlayer.STATE_ENDED) {
                    mPlayerControl.setImageResource(R.drawable.ic_play_button);
                    playerListActivity.getTrack(currentTrack, true);
                }

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }
        });
        handleClickOnTrack(trackOnLoad, currentTrack);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            playerListActivity = (PlayerListActivity) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(playerListActivity.toString() + " must implement");
        }
    }

    public void setTrackImageToPlayer(String url) {
        if (loadtarget == null) loadtarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                handleLoadedBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(getContext()).load(url).into(loadtarget);
    }

    public void handleLoadedBitmap(Bitmap originalBitmap) {
        mSelectedTrackImage.setImageBitmap(originalBitmap);
        Bitmap blurredBitmap = BlurBuilder.blur(getContext(), originalBitmap);
        Bitmap scalabelBluredBitmap = Bitmap.createScaledBitmap(blurredBitmap, mBlurePlayer.getWidth(), mBlurePlayer.getHeight(), false);
        mBlurePlayer.setImageBitmap(scalabelBluredBitmap);
    }

    public void handleClickOnTrack(Track track, int position) {
        currentTrack = position;
        mSelectedTrackTitle.setText(track.getTitle());
        mSelectedTrackTitle.setSelected(true);

        setTrackImageToPlayer(track.getArtworkURL());

        if (exoPlayer.getPlayWhenReady()|| !exoPlayer.getPlayWhenReady()) {
            exoPlayer.stop();
        }

        String urlOfTrackStream = track.getStreamURL() + "?client_id=" + ProjectConstants.CLIENT_ID;
        audioSource = new ExtractorMediaSource(Uri.parse(urlOfTrackStream), cacheDataSource, extractor, null, null);
        exoPlayer.prepare(audioSource);
        exoPlayer.setPlayWhenReady(true);
    }

    private void togglePlayPause() {
        exoPlayer.getPlaybackState();

        if (exoPlayer.getPlayWhenReady()) {
            exoPlayer.setPlayWhenReady(false);
            mPlayerControl.setImageResource(R.drawable.ic_play_button);
        } else {
            exoPlayer.setPlayWhenReady(true);
            mPlayerControl.setImageResource(R.drawable.ic_pause);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            if (exoPlayer.getPlayWhenReady()) {
                exoPlayer.setPlayWhenReady(false);
            }
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_control:
                togglePlayPause();
                break;
            case R.id.player_control_next:
                playerListActivity.getTrack(currentTrack, true);
                break;
            case R.id.player_control_prev:
                playerListActivity.getTrack(currentTrack, false);
                break;
            default:
                break;
        }
    }


}




