package com.irronsoft.aleh_struneuski.audio_back.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;
import com.irronsoft.aleh_struneuski.audio_back.network.httpclient.RestClient;
import com.irronsoft.aleh_struneuski.audio_back.network.httpclient.services.SoundCloundService;
import com.irronsoft.aleh_struneuski.audio_back.ui.adapters.TrackAdapter;
import com.irronsoft.aleh_struneuski.audio_back.ui.fragments.components.PlayerFragment;
import com.irronsoft.aleh_struneuski.audio_back.ui.listeners.OnTrackListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchForTrackFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchForTrackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchForTrackFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, OnTrackListener {

    private List<Track> mListItems;
    private TrackAdapter mAdapter;

    private MenuItem searchItem;
    private SearchView searchView;

    private PlayerFragment playerFragment;
    private boolean isPlayerAttached = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchForTrackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchForTrackFragment.
     */
    // TODO: Rename and change types and number of parameters
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
                             Bundle savedInstanceState)
    {
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

        mListItems = new ArrayList<>();
        mAdapter = new TrackAdapter(getContext(), mListItems);

        ListView listView = (ListView) getView().findViewById(R.id.track_list_view);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onDestroy() {
        super.onDestroy();
        if (null != playerFragment) {
            playerFragment.onDestroy();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

        Call<List<Track>> playList = soundCloundService.getTracksByQuery(query);
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
            case R.id.back_button:
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

    private void loadTracks(List<Track> tracks) {
        doFillter(tracks);
        mListItems.clear();
        mListItems.addAll(tracks);
        mAdapter.notifyDataSetChanged();
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

    public void getTrack(int index, boolean isNext) {
        PlayerFragment playerFragment = (PlayerFragment) getFragmentManager().findFragmentById(R.id.player_control_container);
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