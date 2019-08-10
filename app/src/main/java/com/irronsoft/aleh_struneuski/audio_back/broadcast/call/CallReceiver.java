package com.irronsoft.aleh_struneuski.audio_back.broadcast.call;

import android.content.Context;

import com.irronsoft.aleh_struneuski.audio_back.ui.listeners.OnPlayerControlListener;

public class CallReceiver extends AbstractCallReceiver {

    private OnPlayerControlListener onPlayerControlListener;

    private boolean isPlayingBeforeStopping;

    public CallReceiver() {
    }

    public CallReceiver(OnPlayerControlListener onPlayerControlListener) {
        this.onPlayerControlListener = onPlayerControlListener;
    }

    @Override
    protected void onIncomingCallReceived(Context ctx) {
        isPlayingBeforeStopping = onPlayerControlListener.isPlayingPlayer();
        if (isPlayingBeforeStopping) {
            onPlayerControlListener.togglePlayPausePlayer();
        }
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx) {
    }

    @Override
    protected void onIncomingCallEnded(Context ctx) {
        if (isPlayingBeforeStopping) {
            onPlayerControlListener.togglePlayPausePlayer();
        }
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx) {
        isPlayingBeforeStopping = onPlayerControlListener.isPlayingPlayer();
        if (isPlayingBeforeStopping) {
            onPlayerControlListener.togglePlayPausePlayer();
        }
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx) {
        if (isPlayingBeforeStopping) {
            onPlayerControlListener.togglePlayPausePlayer();
        }
    }

    @Override
    protected void onMissedCall(Context ctx) {
        if (isPlayingBeforeStopping) {
            onPlayerControlListener.togglePlayPausePlayer();
        }
    }

}
