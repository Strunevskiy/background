package com.irronsoft.aleh_struneuski.audio_back.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by alehstruneuski on 6/13/16.
 */
public class TrackAdapter extends BaseAdapter {

    private Context mContext;
    private List<Track> mTracks;

    public TrackAdapter(Context context, List<Track> tracks) {
        mContext = context;
        mTracks = tracks;
    }

    @Override
    public int getCount() {
        return mTracks.size();
    }

    @Override
    public Track getItem(int position) {
        return mTracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Track track = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.track_list_row, parent, false);
            holder = new ViewHolder();
            holder.trackImageView = (ImageView) convertView.findViewById(R.id.track_image);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.track_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleTextView.setText(track.getTitle());

        String iconUrl = track.getArtworkURL();
        if (null == iconUrl || iconUrl.isEmpty()){
            Picasso.with(mContext).load(R.mipmap.ic_launcher).fit().into(holder.trackImageView);
        } else {
            Picasso.with(mContext).load(track.getArtworkURL()).fit().into(holder.trackImageView);
        }


        return convertView;
    }

    static class ViewHolder {
        ImageView trackImageView;
        TextView titleTextView;
    }

}