package com.irronsoft.aleh_struneuski.audio_back.broadcast.headset;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.irronsoft.aleh_struneuski.audio_back.ui.listeners.OnPlayerControlListener;

/**
 * Created by alehstruneuski on 6/17/17.
 */

public class HeadsetReceiver extends BroadcastReceiver {

    private OnPlayerControlListener onPlayerControlListener;

    private boolean headsetConnected = false;

    public HeadsetReceiver() {}

    public HeadsetReceiver(OnPlayerControlListener onPlayerControlListener) {
        this.onPlayerControlListener = onPlayerControlListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra("state")) {
            if (headsetConnected && intent.getIntExtra("state", 0) == 0) {
                headsetConnected = false;
                if (onPlayerControlListener.isPlayingPlayer()) {
                    onPlayerControlListener.togglePlayPausePlayer();
                }
            } else if (!headsetConnected && intent.getIntExtra("state", 0) == 1){
                headsetConnected = true;
            }
        }
    }
}
