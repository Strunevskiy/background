package com.aleh.struneuski.background.network.httpclient.services;

import com.aleh.struneuski.background.bean.soundclound.PlayList;
import com.aleh.struneuski.background.bean.soundclound.Track;
import com.aleh.struneuski.background.constants.ProjectConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SoundCloundService {

    @GET("users/" + ProjectConstants.USER_ID + "/playlists?client_id=" + ProjectConstants.CLIENT_ID)
    Call<List<PlayList>> getPlayLists();

    @GET("tracks?client_id=" + ProjectConstants.CLIENT_ID)
    Call<List<Track>> getTracksByQuery(@Query("limit") String numberOfTracks, @Query("q") String query);
}
