package com.aleh.struneuski.background.broadcast.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ArrayAdapter;

import com.aleh.struneuski.background.Background;
import com.aleh.struneuski.background.R;

public class ConnectionBroadcastReceiver extends BroadcastReceiver {

    private Background mBackground;
    private ArrayAdapter arrayAdapter;

    private Boolean connected;
    private Boolean isConnectedPrvState;


    public ConnectionBroadcastReceiver() {
    }

    public ConnectionBroadcastReceiver(Background background, ArrayAdapter arrayAdapter, Boolean isConnectedPrvState) {
        this.mBackground = background;
        this.arrayAdapter = arrayAdapter;
        this.isConnectedPrvState = isConnectedPrvState;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getExtras() == null)
            return;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = manager.getActiveNetworkInfo();

        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            connected = false;
            isConnectedPrvState = false;
        }

        if (isConnectedPrvState == false && null != connected && connected == true) {
            mBackground.extractPlayList(arrayAdapter);
            mBackground.showToast(context.getResources().getString(R.string.info_internet));
            isConnectedPrvState = true;
        }
    }


}
