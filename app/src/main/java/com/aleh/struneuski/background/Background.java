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

import com.aleh.struneuski.background.bean.soundclound.AccessToken;
import com.aleh.struneuski.background.bean.soundclound.PlayList;
import com.aleh.struneuski.background.constants.ProjectConstants;
import com.aleh.struneuski.background.network.httpclient.RestClient;
import com.aleh.struneuski.background.network.httpclient.services.SoundCloundService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Background extends Application {

    private String authorizationHeaderValue;
    private boolean isErrorPlayList;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void extractPlayList(final ArrayAdapter arrayAdapter) {

        final RestClient restClient = RestClient.getInstance();
        final Retrofit retrofitClient = restClient.getRetrofitClient();
        final SoundCloundService soundCloundService = retrofitClient.create(SoundCloundService.class);
        final Call<AccessToken> accessTokenCall = soundCloundService.getAccessToken(ProjectConstants.CLIENT_ID, ProjectConstants.CLIENT_SECRET, ProjectConstants.GRANT_TYPE);
        accessTokenCall.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                authorizationHeaderValue = String.format("OAuth %s", response.body().getAccessToken());
                final Call<List<PlayList>> playList = soundCloundService.getPlayLists(authorizationHeaderValue);
                playList.enqueue(new Callback<List<PlayList>>() {
                    @Override
                    public void onResponse(Call<List<PlayList>> call, Response<List<PlayList>> response) {
                        System.out.println(response.toString());
                        List<PlayList> playLists = response.body();
                        System.out.println(playLists);

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

            @Override
            public void onFailure(Call<AccessToken> call, Throwable throwable) {

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

    public String getAuthorizationHeaderValue() {
        return authorizationHeaderValue;
    }

}