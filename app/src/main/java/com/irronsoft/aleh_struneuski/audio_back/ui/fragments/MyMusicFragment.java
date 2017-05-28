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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyMusicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyMusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMusicFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, OnTrackListener {

    private List<Track> mListItems;
    private TrackAdapter mAdapter;

    private PlayerFragment playerFragment;
    private boolean isPlayerAttached = false;

    private TrackDaoImpl trackDao;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_music, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           // case R.id.back_button:
            //    break;
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

    @Override
    public void getTrack(int currentTrack, boolean isNext) {

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

    private Bundle put(String key, Parcelable value){
        Bundle bundle = new Bundle();
        bundle.putParcelable(key, value);
        return bundle;
    }

}
