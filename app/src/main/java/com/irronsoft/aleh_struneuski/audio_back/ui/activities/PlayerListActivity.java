package com.irronsoft.aleh_struneuski.audio_back.ui.activities;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;


import com.irronsoft.aleh_struneuski.audio_back.R;

import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;

import com.irronsoft.aleh_struneuski.audio_back.ui.adapters.TrackAdapter;
import com.irronsoft.aleh_struneuski.audio_back.ui.fragments.PlayerFragment;

import java.util.Iterator;
import java.util.List;



/**
 * Created by alehstruneuski on 6/13/16.
 */
public class PlayerListActivity extends FragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = PlayerListActivity.class.getSimpleName();
    private List<Track> mListItems;
    private TrackAdapter mAdapter;
    private ImageView mBackToHome;

    private PlayerFragment playerFragment;
    private boolean isPlayerAttached = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        mListItems = this.getIntent().getParcelableArrayListExtra("album_track_list");
        doFillter(mListItems);
        ListView listView = (ListView) findViewById(R.id.track_list_view);
        mAdapter = new TrackAdapter(this, mListItems);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);


        mBackToHome = (ImageView) findViewById(R.id.back_button);
        mBackToHome.setOnClickListener(this);
    }




    private void doFillter(List<Track> listOfTrack) {
        Iterator<Track> tracks = listOfTrack.iterator();
        while (tracks.hasNext()) {
            Track track = tracks.next();
            int lengthAfterSplit = track.getTitle().split("(-|–)").length;
            if (lengthAfterSplit != 2) {
                tracks.remove();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            case R.id.back_button:
                onBackPressed();
                break;
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

    public void getTrack(int index, boolean isNext) {
        PlayerFragment playerFragment = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.player_control_container);
        if (isNext) {
            index++;
            if (index >= mListItems.size()) {
                playerFragment.handleClickOnTrack(mListItems.get(0), 0);
            } else {
                playerFragment.handleClickOnTrack(mListItems.get(index), index);
            }
        } else {
            index--;
            if (index == -1) {
                index = mListItems.size() - 1;
                playerFragment.handleClickOnTrack(mListItems.get(index), index);
            } else {
                playerFragment.handleClickOnTrack(mListItems.get(index), index);
            }
        }
    }
}

