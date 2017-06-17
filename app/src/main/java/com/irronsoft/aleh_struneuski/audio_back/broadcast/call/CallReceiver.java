package com.irronsoft.aleh_struneuski.audio_back.broadcast.call;

import android.content.Context;

import com.irronsoft.aleh_struneuski.audio_back.ui.listeners.OnPlayerControlListener;

/**
 * Created by alehstruneuski on 6/17/17.
 */

public class CallReceiver extends AbstractCallReceiver {

    private OnPlayerControlListener onPlayerControlListener;

    public CallReceiver() {}

    public CallReceiver(OnPlayerControlListener onPlayerControlListener) {
        this.onPlayerControlListener = onPlayerControlListener;
    }

    @Override
    protected void onIncomingCallReceived(Context ctx) {
        if (onPlayerControlListener.isPlayingPlayer())  {
            onPlayerControlListener.togglePlayPausePlayer();
        }
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx) {
        if (onPlayerControlListener.isPlayingPlayer())  {
            onPlayerControlListener.togglePlayPausePlayer();
        }
    }

    @Override
    protected void onIncomingCallEnded(Context ctx) {
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx) {
        if (onPlayerControlListener.isPlayingPlayer())  {
            onPlayerControlListener.togglePlayPausePlayer();
        }
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx) {

    }

    @Override
    protected void onMissedCall(Context ctx) {

    }

}
