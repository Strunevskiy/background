package com.irronsoft.aleh_struneuski.audio_back;

import android.app.Application;
import android.content.ContentResolver;

import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.PlayList;
import com.irronsoft.aleh_struneuski.audio_back.network.httpclient.RestClient;
import com.irronsoft.aleh_struneuski.audio_back.network.httpclient.services.SoundCloundService;

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

    private List<PlayList> mPlayLists;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mPlayLists = new ArrayList<>();
    }

    public void setPlayList() {

        RestClient restClient = RestClient.getInstance();
        Retrofit retrofitClient = restClient.getRetrofitClient();
        SoundCloundService soundCloundService = retrofitClient.create(SoundCloundService.class);

        final Call<List<PlayList>> playList = soundCloundService.getPlayLists();
        playList.enqueue(new Callback<List<PlayList>>() {
            @Override
            public void onResponse(Call<List<PlayList>> call, Response<List<PlayList>> response) {
                List<PlayList> playLists = response.body();

                mPlayLists.retainAll(playLists);
                mPlayLists.addAll(playLists);
            }
            @Override
            public void onFailure(Call<List<PlayList>> call, Throwable t) {
            }
        });
    }

    public List<PlayList> getPlayList() {
        return mPlayLists;
    }

}