package com.irronsoft.aleh_struneuski.audio_back.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;
import com.squareup.picasso.Picasso;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.util.List;

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

        Track trackItem = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.track_list_row, parent, false);
            holder = new ViewHolder();
            holder.trackImageView = (ImageView) convertView.findViewById(R.id.track_image);
            holder.trackTitleTextView = (TextView) convertView.findViewById(R.id.track_title);
            holder.trackSingerTextView = (TextView) convertView.findViewById(R.id.track_singer);
            holder.dowloadImageView = (ImageView) convertView.findViewById(R.id.dowload_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#2E2E2E"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#373737"));
        }

        holder.trackSingerTextView.setText(trackItem.getTitle().split("(-|–)")[0].trim());
        holder.trackTitleTextView.setText(trackItem.getTitle().split("(-|–)")[1].trim());

        String iconUrl = trackItem.getArtworkURL();
        if (null == iconUrl || iconUrl.isEmpty() || iconUrl.equals("null")) {
            Picasso.with(mContext).load(R.mipmap.ic_launcher).fit().into(holder.trackImageView);
        } else if (iconUrl.startsWith("http")) {
            Picasso.with(mContext).load(iconUrl).fit().into(holder.trackImageView);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView trackImageView;
        TextView trackTitleTextView;
        TextView trackSingerTextView;
        ImageView dowloadImageView;
        GeometricProgressView progressView;
    }
}