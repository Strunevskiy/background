package com.irronsoft.aleh_struneuski.audio_back.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.DownloadingStatus;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;
import com.irronsoft.aleh_struneuski.audio_back.constants.ProjectConstants;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.listener.FetchListener;
import com.tonyodev.fetch.request.Request;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import java.util.List;

/**
 * Created by alehstruneuski on 6/13/16.
 */
public class TrackAdapter extends BaseAdapter implements FetchListener {

    private Context mContext;
    private List<Track> mTracks;

    private Fetch fetch;

    public TrackAdapter(Context context, List<Track> tracks) {
        mContext = context;
        mTracks = tracks;
        fetch = Fetch.getInstance(context);
        fetch.enableLogging(true);
        fetch.setConcurrentDownloadsLimit(3);
        fetch.addFetchListener(this);
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        Track track = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.track_list_row, parent, false);
            holder = new ViewHolder();
            holder.trackImageView = (ImageView) convertView.findViewById(R.id.track_image);
            holder.trackTitleTextView = (TextView) convertView.findViewById(R.id.track_title);
            holder.trackSingerTextView = (TextView) convertView.findViewById(R.id.track_singer);
            holder.dowloadImageView = (ImageView) convertView.findViewById(R.id.dowload_image);
            holder.progressView = (GeometricProgressView) convertView.findViewById(R.id.progressView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (track.isDowload()) {
            holder.dowloadImageView.setBackgroundResource(R.drawable.ic_remove);
        } else {
            holder.dowloadImageView.setBackgroundResource(R.drawable.ic_dowload);
        }

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#2E2E2E"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#373737"));
        }


        holder.dowloadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Track track = getItem(position);
                if (!track.isDowload() && track.getDownloadingStatus() == DownloadingStatus.NOT_DOWNLOADED) {
                    String url = track.getStreamURL();
                    String fileName = url.replaceAll("[^a-zA-z0-9]*", "_");

                    Request request = new Request(url + "?client_id=" + ProjectConstants.CLIENT_ID, fileName);
                    long downloadId = fetch.enqueue(request);

                    track.setDownloadingStatus(DownloadingStatus.IN_PROGRESS);
                    track.setDowloadId(downloadId);
                    notifyDataSetChanged();
                } else if (track.isDowload() && track.getDownloadingStatus() == DownloadingStatus.DOWNLOADED) {
                    fetch.remove(track.getDowloadId());
                    track.setDownloadingStatus(DownloadingStatus.NOT_DOWNLOADED);
                    track.setDowload(false);
                    notifyDataSetChanged();
                }
            }
        });

        holder.trackSingerTextView.setText(track.getTitle().split("(-|–)")[0].trim());
        holder.trackTitleTextView.setText(track.getTitle().split("(-|–)")[1].trim());

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
        TextView trackTitleTextView;
        TextView trackSingerTextView;
        ImageView dowloadImageView;
        GeometricProgressView progressView;
    }

    @Override
    public void onUpdate(long id, int status, int progress, long downloadedBytes, long fileSize, int error) {
        if (status == Fetch.STATUS_DONE) {
            int count = 0;
            for (Track track : mTracks) {
                count++;
                if (track.getDowloadId() == id) {
                    track.setDownloadingStatus(DownloadingStatus.DOWNLOADED);
                    track.setDowload(true);
                    notifyDataSetChanged();
                    break;
                }
            }
        }

        if (status == Fetch.STATUS_DONE || status == Fetch.STATUS_REMOVED) {
        }

    }


}