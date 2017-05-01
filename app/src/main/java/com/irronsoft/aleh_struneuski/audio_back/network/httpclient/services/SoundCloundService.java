package com.irronsoft.aleh_struneuski.audio_back.network.httpclient.services;

import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.PlayList;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;
import com.irronsoft.aleh_struneuski.audio_back.constants.ProjectConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by alehstruneuski on 6/13/16.
 */
public interface SoundCloundService {

    @GET("users/" + ProjectConstants.USER_ID + "/playlists?client_id=" + ProjectConstants.CLIENT_ID)
    Call<List<PlayList>> getPlayLists();

//https://api.soundcloud.com/tracks?client_id=1bffd6b8ab1e4480ba73bbe2422c4ed2&q=future-I%20Thank%20You&limit=20


    @GET("tracks/?limit=20&client_id=" + ProjectConstants.CLIENT_ID)
    Call<List<Track>> getTracksByQuery(@Query("q") String query);
}
