package com.irronsoft.aleh_struneuski.audio_back.httpclient.services;

import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.PlayList;
import com.irronsoft.aleh_struneuski.audio_back.constants.ProjectConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by alehstruneuski on 6/13/16.
 */
public interface SoundCloundService {

    @GET("users/" + ProjectConstants.USER_ID + "/playlists?client_id=" + ProjectConstants.CLIENT_ID)
    Call<List<PlayList>> getPlayLists();


}
