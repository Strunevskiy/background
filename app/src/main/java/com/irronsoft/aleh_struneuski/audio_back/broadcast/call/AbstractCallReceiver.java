package com.irronsoft.aleh_struneuski.audio_back.broadcast.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.irronsoft.aleh_struneuski.audio_back.ui.listeners.OnPlayerControlListener;

import java.util.Date;

public abstract class AbstractCallReceiver extends BroadcastReceiver {

    private static final String TAG = AbstractCallReceiver.class.getSimpleName();

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static boolean isIncoming;

    private OnPlayerControlListener onPlayerControlListener;

    public AbstractCallReceiver() {}

    public AbstractCallReceiver(OnPlayerControlListener onPlayerControlListener) {
        this.onPlayerControlListener = onPlayerControlListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
        } else {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            int state = 0;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }
            onCallStateChanged(context, state);
        }
    }
    //Derived classes should override these to respond to specific events of interest
    protected abstract void onIncomingCallReceived(Context ctx);
    protected abstract void onIncomingCallAnswered(Context ctx);
    protected abstract void onIncomingCallEnded(Context ctx);

    protected abstract void onOutgoingCallStarted(Context ctx);
    protected abstract void onOutgoingCallEnded(Context ctx);

    protected abstract void onMissedCall(Context ctx);

    //Deals with actual events

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    public void onCallStateChanged(Context context, int state) {
        if(lastState == state){
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                onIncomingCallReceived(context);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING){
                    isIncoming = false;
                    onOutgoingCallStarted(context);
                } else {
                    isIncoming = true;
                    onIncomingCallAnswered(context);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    onMissedCall(context);
                } else if(isIncoming) {
                    onIncomingCallEnded(context);
                } else {
                    onOutgoingCallEnded(context);
                }
                break;
        }
        lastState = state;
    }

}
