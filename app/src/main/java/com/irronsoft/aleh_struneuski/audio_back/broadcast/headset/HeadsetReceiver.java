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

    public HeadsetReceiver() {}

    public HeadsetReceiver(OnPlayerControlListener onPlayerControlListener) {
        this.onPlayerControlListener = onPlayerControlListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra("state", -1);
        switch (state) {
            case 0:
                if (onPlayerControlListener.isPlayingPlayer()) {
                    onPlayerControlListener.togglePlayPausePlayer();
                }
                break;
        }
    }
}