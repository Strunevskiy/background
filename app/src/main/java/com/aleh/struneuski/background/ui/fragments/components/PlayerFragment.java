package com.aleh.struneuski.background.ui.fragments.components;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.aleh.struneuski.background.R;
import com.aleh.struneuski.background.bean.soundclound.Track;
import com.aleh.struneuski.background.broadcast.call.AbstractCallReceiver;
import com.aleh.struneuski.background.broadcast.call.CallReceiver;
import com.aleh.struneuski.background.broadcast.headset.HeadsetReceiver;
import com.aleh.struneuski.background.constants.ProjectConstants;
import com.aleh.struneuski.background.ui.listeners.OnPlayerControlListener;
import com.aleh.struneuski.background.ui.listeners.OnTrackListener;
import com.aleh.struneuski.background.utils.BlurBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.Set;

/**
 * Created by alehstruneuski on 11/25/16.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener, OnPlayerControlListener {

    private Context mContext;

    private AbstractCallReceiver mCallReceiver;
    private HeadsetReceiver mHeadsetReceiver;

    private OnTrackListener mOnTrackListener;

    private TextView mSelectedTrackTitle;

    private ImageView mSelectedTrackImage;
    private ImageView mBlurePlayer;
    private ImageView mPlayerControl;

    private ImageView mPlayerControlPrev;
    private ImageView mPlayerControlNext;

    private Target loadtarget;
    private Track trackOnLoad;
    private int currentTrack;
    private boolean isAttachedInitially;

    private SimpleExoPlayer exoPlayer;
    private DefaultHttpDataSourceFactory dataSourceFactory;
    private ExtractorsFactory extractor;
    private MediaSource audioSource;
    private CacheDataSourceFactory cacheDataSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        exoPlayer.setPlayWhenReady(true);
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
                    mPlayerControl.setImageResource(R.drawable.ic_pause);
                    mOnTrackListener.getTrack(currentTrack, true);
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
        mContext = context;

        mCallReceiver = new CallReceiver(this);
        IntentFilter callReceiverFilters = new IntentFilter();
        callReceiverFilters.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        callReceiverFilters.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        getActivity().registerReceiver(mCallReceiver, callReceiverFilters);

        mHeadsetReceiver = new HeadsetReceiver(this);
        getActivity().registerReceiver(mHeadsetReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));

        if (context instanceof OnTrackListener) {
            mOnTrackListener = (OnTrackListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement");
        }
    }

    public void handleLoadedBitmap(Bitmap originalBitmap) {
        mSelectedTrackImage.setImageBitmap(originalBitmap);
        final Bitmap blurredBitmap = BlurBuilder.blur(getContext(), originalBitmap);
        // It is the fix of the error width and height must be > 0
        ViewTreeObserver vto = mBlurePlayer.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBlurePlayer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Bitmap scalabelBluredBitmap = Bitmap.createScaledBitmap(blurredBitmap, mBlurePlayer.getWidth(), mBlurePlayer.getHeight(), false);
                mBlurePlayer.setImageBitmap(scalabelBluredBitmap);
            }
        });
    }

    public void handleClickOnTrack(Track track, int position) {
        currentTrack = position;
        setTrackTitleToPlayer(track);
        setTrackImageToPlayer(track);

        boolean isPlayPreviously = false;
        if (exoPlayer.getPlayWhenReady()) {
            isPlayPreviously = true;
        }

        exoPlayer.stop();

        String urlOfTrackStream = track.getStreamURL();
        if (!track.isDowload()) {
            urlOfTrackStream += "?client_id=" + ProjectConstants.CLIENT_ID;
            audioSource = new ExtractorMediaSource(Uri.parse(urlOfTrackStream), cacheDataSource, extractor, null, null);
        } else {
            audioSource = new ExtractorMediaSource(Uri.parse(urlOfTrackStream), new FileDataSourceFactory(), extractor, null, null);
        }
        exoPlayer.prepare(audioSource);

        if (isPlayPreviously) {
            exoPlayer.setPlayWhenReady(true);
        }
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
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getActivity().unregisterReceiver(mHeadsetReceiver);
        getActivity().unregisterReceiver(mCallReceiver);
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
        mOnTrackListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_control:
                togglePlayPause();
                break;
            case R.id.player_control_next:
                mOnTrackListener.getTrack(currentTrack, true);
                break;
            case R.id.player_control_prev:
                mOnTrackListener.getTrack(currentTrack, false);
                break;
            default:
                break;
        }
    }

    private void setTrackTitleToPlayer(Track track) {
        mSelectedTrackTitle.setText(track.getTitle());
        mSelectedTrackTitle.setSelected(true);
    }

    private void setTrackImageToPlayer(Track track) {
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

        String iconUrl = track.getArtworkURL();
        if (null == iconUrl || iconUrl.isEmpty() || iconUrl.equals("null")) {
            Picasso.with(getContext()).load(R.mipmap.ic_launcher).into(loadtarget);
        } else if (!track.isDowload()) {
            Picasso.with(getContext()).load(iconUrl).into(loadtarget);
        } else if (track.isDowload()) {
            Picasso.with(getContext()).load(new File(iconUrl)).into(loadtarget);
        }
    }

    @Override
    public boolean isPlayingPlayer() {
        return exoPlayer.getPlayWhenReady();
    }

    @Override
    public void togglePlayPausePlayer() {
        togglePlayPause();
    }


}




