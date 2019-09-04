package com.aleh.struneuski.background.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.aleh.struneuski.background.R;
import com.aleh.struneuski.background.bean.soundclound.DownloadingStatus;
import com.aleh.struneuski.background.bean.soundclound.Track;
import com.aleh.struneuski.background.database.dao.impl.TrackDaoImpl;
import com.aleh.struneuski.background.network.httpclient.RestClient;
import com.aleh.struneuski.background.network.httpclient.services.SoundCloundService;
import com.aleh.struneuski.background.ui.adapters.TrackAdapter;
import com.aleh.struneuski.background.ui.fragments.components.PlayerFragment;
import com.aleh.struneuski.background.ui.listeners.OnTrackListener;
import com.aleh.struneuski.background.utils.PlayerFragmentUtils;
import com.aleh.struneuski.background.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchForTrackFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, OnTrackListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Track> listOfTracksFromDataBase;
    private List<Track> mListItems;
    private TrackAdapter mAdapter;
    private MenuItem searchItem;
    private SearchView searchView;
    private PlayerFragment playerFragment;
    private boolean isPlayerAttached = false;
    private TrackDaoImpl trackDao;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchForTrackFragment() {
    }

    public static SearchForTrackFragment newInstance(String param1, String param2) {
        SearchForTrackFragment fragment = new SearchForTrackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_for_track, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trackDao = new TrackDaoImpl(getContext());

        mListItems = new ArrayList<>();
        mAdapter = new TrackAdapter(getContext(), mListItems);

        ListView listView = (ListView) getView().findViewById(R.id.track_list_view);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (null != playerFragment) {
            playerFragment.onDetach();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != playerFragment) {
            playerFragment.onDestroy();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchItem.expandActionView();
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                View view = getView();
                if (null != view) {
                    FrameLayout playerContainer = (FrameLayout) view.findViewById(R.id.player_control_container);
                    if (hasFocus) {
                        playerContainer.setVisibility(View.GONE);
                    } else {
                        playerContainer.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    searchView.clearFocus();
                    searchTracksByQuery(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchTracksByQuery(String query) {
        RestClient restClient = RestClient.getInstance();
        Retrofit retrofitClient = restClient.getRetrofitClient();
        SoundCloundService soundCloundService = retrofitClient.create(SoundCloundService.class);

        String[] limits = getResources().getStringArray(R.array.limits);
        int index = PreferenceUtils.getLimit(this.getContext());
        String numberOfTracks = limits[index];

        Call<List<Track>> playList = soundCloundService.getTracksByQuery(numberOfTracks, query);
        playList.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                List<Track> tracks = response.body();
                loadTracks(tracks);
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
            }
        });
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
            getFragmentManager().beginTransaction().add(R.id.player_control_container, playerFragment).commit();
        } else {
            PlayerFragment playerFragment = (PlayerFragment) getFragmentManager().findFragmentById(R.id.player_control_container);
            playerFragment.handleClickOnTrack(track, position);
        }
    }

    private Bundle put(String key, Parcelable value) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(key, value);
        return bundle;
    }

    private void loadTracks(List<Track> tracks) {
        doFillter(tracks);
        markLoadedTracks(tracks);
        mListItems.clear();
        mListItems.addAll(tracks);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getTrack(int index, boolean isNext) {
        PlayerFragmentUtils.getTrack(playerFragment, mListItems, index, isNext);
    }

    private void markLoadedTracks(List<Track> listOfTracks) {
        List<Track> tracksFromDatabase = getTracksFromDataBase();

        Map<String, Track> tracks = new HashMap();
        for (Track track : tracksFromDatabase) {
            tracks.put(track.getTitle(), track);
        }

        for (Track track : listOfTracks) {
            String title = track.getTitle();
            if (tracks.containsKey(title)) {
                Track downloadedTrack = tracks.get(title);
                track.setDowload(true);
                track.setDownloadingStatus(DownloadingStatus.DOWNLOADED);
                track.setTitle(downloadedTrack.getTitle());
                track.setStreamURL(downloadedTrack.getStreamURL());
                track.setArtworkURL(downloadedTrack.getArtworkURL());
                track.setID(downloadedTrack.getID());
                track.setDowloadIds(downloadedTrack.getDowloadIds());
            }
        }
    }

    public List<Track> getTracksFromDataBase() {
        if (null == listOfTracksFromDataBase) {
            listOfTracksFromDataBase = trackDao.getTracksFromDataBase();
        }
        return listOfTracksFromDataBase;
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

}