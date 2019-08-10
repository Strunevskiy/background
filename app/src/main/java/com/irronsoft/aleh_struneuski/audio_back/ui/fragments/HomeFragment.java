package com.irronsoft.aleh_struneuski.audio_back.ui.fragments;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.irronsoft.aleh_struneuski.audio_back.Background;
import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.PlayList;
import com.irronsoft.aleh_struneuski.audio_back.broadcast.network.ConnectionBroadcastReceiver;
import com.irronsoft.aleh_struneuski.audio_back.ui.adapters.GridViewAdapter;
import com.irronsoft.aleh_struneuski.audio_back.utils.NetworkUtils;
import com.irronsoft.aleh_struneuski.audio_back.utils.ResolutionUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ConnectionBroadcastReceiver mConnectionBroadcastReceiver;

    private Background mBackground;

    private List<PlayList> mPlayListGridData;

    private GridView mGridView;
    private GridViewAdapter mGridAdapter;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBackground = (Background) getActivity().getApplication();

        mGridView = (GridView) getView().findViewById(R.id.gridView);
        mGridView.setHorizontalSpacing(ResolutionUtils.convertPercentToPixelWidth(getContext().getApplicationContext(), 1.25f));
        mGridView.setVerticalSpacing(ResolutionUtils.convertPercentToPixelHight(getContext().getApplicationContext(), 0.7525f));

        mPlayListGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(getContext().getApplicationContext(), R.layout.grid_item_layout, mPlayListGridData);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(mGridAdapter);

        mBackground.extractPlayList(mGridAdapter);
        if (mBackground.isErrorPlayList() && !NetworkUtils.isNetworkAvailable(getContext())) {
            mBackground.showToast(getString(R.string.error_internet));
            mBackground.setErrorPlayList(false);
        }

        mConnectionBroadcastReceiver = new ConnectionBroadcastReceiver(mBackground, mGridAdapter, NetworkUtils.isNetworkAvailable(getContext()));
        getActivity().registerReceiver(mConnectionBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mConnectionBroadcastReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
