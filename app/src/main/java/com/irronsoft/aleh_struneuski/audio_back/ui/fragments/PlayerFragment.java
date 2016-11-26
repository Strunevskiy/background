package com.irronsoft.aleh_struneuski.audio_back.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;
import com.irronsoft.aleh_struneuski.audio_back.constants.ProjectConstants;
import com.irronsoft.aleh_struneuski.audio_back.ui.activities.PlayerListActivity;
import com.irronsoft.aleh_struneuski.audio_back.utils.BlurBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

/**
 * Created by alehstruneuski on 11/25/16.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener {

    private PlayerListActivity playerListActivity;
    private MediaPlayer mMediaPlayer;
    private TextView mSelectedTrackTitle;
    private ImageView mSelectedTrackImage;
    private ImageView mBlurePlayer;
    private ImageView mPlayerControl;
    private ImageView mPlayerControlPrev;
    private ImageView mPlayerControlNext;
    private Target loadtarget;
    private Track trackOnLoad;
    private int currentTrack;

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

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                togglePlayPause();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayerControl.setImageResource(R.drawable.ic_play_button);
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

        if (mMediaPlayer.isPlaying() || !mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }

        try {
            mMediaPlayer.setDataSource(track.getStreamURL() + "?client_id=" + ProjectConstants.CLIENT_ID);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void togglePlayPause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPlayerControl.setImageResource(R.drawable.ic_play_button);
        } else {
            mMediaPlayer.start();
            mPlayerControl.setImageResource(R.drawable.ic_pause);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
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




