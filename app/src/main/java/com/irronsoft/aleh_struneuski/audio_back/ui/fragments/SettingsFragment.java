package com.irronsoft.aleh_struneuski.audio_back.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.irronsoft.aleh_struneuski.audio_back.Background;
import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.utils.PreferenceUtils;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private int selectedLimit, selectedOrder;
    private SharedPreferences prefs;
    private Background background;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        background = (Background) getContext().getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView limitText = (TextView) getView().findViewById(R.id.limit);
        limitText.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public  void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.limit:
                new AlertDialog.Builder(getContext()).setTitle(R.string.limit).setSingleChoiceItems(R.array.limits, PreferenceUtils.getLimit(getContext()), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedLimit = which;
                    }
                }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("TTTTT", String.valueOf(selectedLimit));
                        prefs.edit().putInt(PreferenceUtils.LIMIT, selectedLimit).apply();
                        background.showToast(getString(R.string.restart_msg));
                        dialog.dismiss();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
            default:
                break;
        }
    }

}