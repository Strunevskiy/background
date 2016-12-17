package com.irronsoft.aleh_struneuski.audio_back.ui.activities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.PlayList;
import com.irronsoft.aleh_struneuski.audio_back.httpclient.RestClient;
import com.irronsoft.aleh_struneuski.audio_back.httpclient.services.SoundCloundService;
import com.irronsoft.aleh_struneuski.audio_back.ui.adapters.GridViewAdapter;
import com.irronsoft.aleh_struneuski.audio_back.utils.ResolutionUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.ViewGroup.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList playListGridData;

    private GridView mGridView;
    private GridViewAdapter mGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
      //  initCollapsingToolbar();


        mGridView = (GridView) findViewById(R.id.gridView);
        mGridView.setHorizontalSpacing(ResolutionUtils.convertPercentToPixelWidth(this.getApplicationContext(),1.25f));
        mGridView.setVerticalSpacing(ResolutionUtils.convertPercentToPixelHight(this.getApplicationContext(), 0.7525f));


        //Initialize with empty data
        playListGridData = new ArrayList<PlayList>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, playListGridData);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(mGridAdapter);

        setPlayList();
    }

    private void setPlayList() {
        RestClient restClient = RestClient.getInstance();
        Retrofit retrofitClient = restClient.getRetrofitClient();
        SoundCloundService soundCloundService = retrofitClient.create(SoundCloundService.class);

        Call<List<PlayList>> playList = soundCloundService.getPlayLists();
        playList.enqueue(new Callback<List<PlayList>>() {
            @Override
            public void onResponse(Call<List<PlayList>> call, Response<List<PlayList>> response) {
                List<PlayList> playLists = response.body();
                mGridAdapter.setGridData(playLists);
            }

            @Override
            public void onFailure(Call<List<PlayList>> call, Throwable t) {
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
