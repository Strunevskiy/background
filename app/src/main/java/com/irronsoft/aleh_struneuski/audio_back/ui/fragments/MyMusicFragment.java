package com.irronsoft.aleh_struneuski.audio_back.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;
import com.irronsoft.aleh_struneuski.audio_back.database.dao.impl.TrackDaoImpl;
import com.irronsoft.aleh_struneuski.audio_back.ui.adapters.TrackAdapter;
import com.irronsoft.aleh_struneuski.audio_back.ui.fragments.components.PlayerFragment;
import com.irronsoft.aleh_struneuski.audio_back.ui.listeners.OnTrackListener;
import com.irronsoft.aleh_struneuski.audio_back.utils.PlayerFragmentUtils;

import java.util.List;

public class MyMusicFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, OnTrackListener {

    private List<Track> mListItems;
    private TrackAdapter mAdapter;

    private PlayerFragment playerFragment;
    private boolean isPlayerAttached = false;

    private TrackDaoImpl trackDao;

    private OnTrackListener mTrackListener;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyMusicFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyMusicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyMusicFragment newInstance(String param1, String param2) {
        MyMusicFragment fragment = new MyMusicFragment();
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
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_my_music, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trackDao = new TrackDaoImpl(getContext());

        mListItems = trackDao.getTracksFromDataBase();

        mAdapter = new TrackAdapter(getContext(), mListItems);
        ListView listView = (ListView) getView().findViewById(R.id.track_list_view);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        mAdapter.notifyDataSetChanged();
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
    public  void onDestroy() {
        super.onDestroy();
        if (null != playerFragment) {
            playerFragment.onDestroy();
        }
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

    private Bundle put(String key, Parcelable value){
        Bundle bundle = new Bundle();
        bundle.putParcelable(key, value);
        return bundle;
    }

    @Override
    public void getTrack(int currentTrack, boolean isNext) {
        PlayerFragmentUtils.getTrack(playerFragment, mListItems, currentTrack, isNext);
    }

}
