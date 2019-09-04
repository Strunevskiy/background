package com.aleh.struneuski.background.ui.activities;

import android.os.Bundle;
import android.os.Parcelable;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.aleh.struneuski.background.R;

import com.aleh.struneuski.background.bean.soundclound.Track;

import com.aleh.struneuski.background.database.dao.impl.TrackDaoImpl;
import com.aleh.struneuski.background.ui.adapters.TrackAdapter;
import com.aleh.struneuski.background.ui.fragments.components.PlayerFragment;
import com.aleh.struneuski.background.ui.listeners.OnTrackListener;
import com.aleh.struneuski.background.utils.PlayerFragmentUtils;

import java.util.Iterator;
import java.util.List;

public class PlayerListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, OnTrackListener {

    private static final String TAG = PlayerListActivity.class.getSimpleName();


    private static final String TAG_TRACK_LIST = "Track List";

    private Toolbar toolbar;
    private PlayerFragment playerFragment;

    private TrackAdapter mAdapter;

    private TrackDaoImpl trackDao;

    private List<Track> mListItems;
    private boolean isPlayerAttached = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(TAG_TRACK_LIST);

        mListItems = this.getIntent().getParcelableArrayListExtra("album_track_list");
        doFilter(mListItems);

        trackDao = new TrackDaoImpl(getApplicationContext());
        trackDao.tagDowloadedTracks(mListItems);

        ListView listView = (ListView) findViewById(R.id.track_list_view);
        mAdapter = new TrackAdapter(this, mListItems);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }

    private void doFilter(List<Track> tracks) {
        Iterator<Track> tracksIterator = tracks.iterator();
        while (tracksIterator.hasNext()) {
            Track track = tracksIterator.next();
            int lengthAfterSplit = track.getTitle().split("(-|–)").length;
            if (lengthAfterSplit != 2) {
                tracksIterator.remove();
            }
        }
    }

    private void loadTracks(List<Track> tracks) {
        mListItems.clear();
        mListItems.addAll(tracks);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Track track = mListItems.get(position);
        if (!isPlayerAttached) {
            isPlayerAttached = true;
            playerFragment = new PlayerFragment();
            playerFragment.setArguments(put("track", track));
            getSupportFragmentManager().beginTransaction().add(R.id.player_control_container, playerFragment).commit();
        } else {
            PlayerFragment playerFragment = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.player_control_container);
            playerFragment.handleClickOnTrack(track, position);
        }
    }

    private Bundle put(String key, Parcelable value){
        Bundle bundle = new Bundle();
        bundle.putParcelable(key, value);
        return bundle;
    }

    @Override
    public void getTrack(int index, boolean isNext) {
        PlayerFragment playerFragment = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.player_control_container);
        PlayerFragmentUtils.getTrack(playerFragment, mListItems, index, isNext);
    }

}

