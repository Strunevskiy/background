package com.irronsoft.aleh_struneuski.audio_back;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.PlayList;
import com.irronsoft.aleh_struneuski.audio_back.network.httpclient.RestClient;
import com.irronsoft.aleh_struneuski.audio_back.network.httpclient.services.SoundCloundService;
import com.irronsoft.aleh_struneuski.audio_back.ui.activities.IntroActivity;
import com.irronsoft.aleh_struneuski.audio_back.ui.activities.SplashActivity;
import com.irronsoft.aleh_struneuski.audio_back.ui.fragments.HomeFragment;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by alehstruneuski on 6/24/17.
 */

public class Background extends Application {

    private boolean isErrorPlayList;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void extractPlayList(final ArrayAdapter arrayAdapter) {

        final RestClient restClient = RestClient.getInstance();
        Retrofit retrofitClient = restClient.getRetrofitClient();
        SoundCloundService soundCloundService = retrofitClient.create(SoundCloundService.class);

        final Call<List<PlayList>> playList = soundCloundService.getPlayLists();
        playList.enqueue(new Callback<List<PlayList>>() {
            @Override
            public void onResponse(Call<List<PlayList>> call, Response<List<PlayList>> response) {
                List<PlayList> playLists = response.body();

                if (null != arrayAdapter) {
                    arrayAdapter.clear();
                    arrayAdapter.addAll(playLists);

                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<PlayList>> call, Throwable t) {
                isErrorPlayList = true;
            }
        });
    }

    public void setErrorPlayList(boolean isErrorPlayList) {
        this.isErrorPlayList = isErrorPlayList;
    }

    public boolean isErrorPlayList() {
        return isErrorPlayList;
    }

    public void showToast(String message) {
        Toast toast = new Toast(this);

        View snackbar = LayoutInflater.from(this).inflate(R.layout.snackbar_layout, null);
        ((TextView) snackbar.findViewById(R.id.message)).setText(message);
        ViewCompat.setElevation(snackbar, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));

        toast.setView(snackbar);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setMargin(0, 0);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.show();
    }

}