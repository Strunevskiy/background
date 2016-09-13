package com.irronsoft.aleh_struneuski.audio_back.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.PlayList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by alehstruneuski on 9/6/16.
 */
public class GridViewAdapter extends ArrayAdapter {


    public GridViewAdapter(Context context, int resource) {
        super(context, resource);
    }
}