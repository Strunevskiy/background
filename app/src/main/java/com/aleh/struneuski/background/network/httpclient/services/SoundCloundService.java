package com.aleh.struneuski.background.network.httpclient.services;

import com.aleh.struneuski.background.bean.soundclound.AccessToken;
import com.aleh.struneuski.background.bean.soundclound.PlayList;
import com.aleh.struneuski.background.bean.soundclound.Track;
import com.aleh.struneuski.background.constants.ProjectConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SoundCloundService {

    @FormUrlEncoded
    @POST("oauth2/token")
    Call<AccessToken> getAccessToken(@Field("client_id") String clientId, @Field("client_secret") String clientSecret, @Field("grant_type") String grantType);

    @GET("users/" + ProjectConstants.USER_ID + "/playlists")
    Call<List<PlayList>> getPlayLists(@Header("Authorization") String accessToken);

    @GET("tracks")
    Call<List<Track>> getTracksByQuery(@Query("limit") String numberOfTracks, @Query("q") String query);
}
