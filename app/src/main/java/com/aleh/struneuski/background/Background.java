package com.aleh.struneuski.background;

import android.app.Application;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aleh.struneuski.background.bean.soundclound.PlayList;
import com.aleh.struneuski.background.network.httpclient.RestClient;
import com.aleh.struneuski.background.network.httpclient.services.SoundCloundService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

    public boolean isErrorPlayList() {
        return isErrorPlayList;
    }

    public void setErrorPlayList(boolean isErrorPlayList) {
        this.isErrorPlayList = isErrorPlayList;
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