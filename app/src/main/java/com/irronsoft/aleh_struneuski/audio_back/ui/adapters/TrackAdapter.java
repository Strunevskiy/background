package com.irronsoft.aleh_struneuski.audio_back.ui.adapters;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.DownloadingStatus;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;
import com.irronsoft.aleh_struneuski.audio_back.constants.ProjectConstants;
import com.irronsoft.aleh_struneuski.audio_back.database.dao.TrackDao;
import com.irronsoft.aleh_struneuski.audio_back.database.dao.impl.TrackDaoImpl;
import com.irronsoft.aleh_struneuski.audio_back.ui.activities.PlayerListActivity;
import com.irronsoft.aleh_struneuski.audio_back.ui.fragments.components.PlayerFragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.listener.FetchListener;
import com.tonyodev.fetch.request.Request;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alehstruneuski on 6/13/16.
 */
public class TrackAdapter extends BaseAdapter implements FetchListener {

    private Context mContext;
    private List<Track> mTracks;

    private TrackDao trackDao;
    private Fetch fetch;

    public TrackAdapter(Context context, List<Track> tracks) {
        mContext = context;
        mTracks = tracks;
        trackDao = new TrackDaoImpl(context);
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
            holder.progressView.setVisibility(View.GONE);

            holder.dowloadImageView.setBackgroundResource(R.drawable.ic_remove);
            holder.dowloadImageView.setVisibility(View.VISIBLE);
        } else {
            holder.dowloadImageView.setBackgroundResource(R.drawable.ic_dowload);
        }

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#2E2E2E"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#373737"));
        }

        final View finalConvertView = convertView;
        holder.dowloadImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Track track = getItem(position);
                if (!track.isDowload() && track.getDownloadingStatus() == DownloadingStatus.NOT_DOWNLOADED) {

                    GeometricProgressView progressView = (GeometricProgressView) finalConvertView.findViewById(R.id.progressView);
                    progressView.setVisibility(View.VISIBLE);

                    view.setVisibility(View.GONE);

                    List<Request> requests = new ArrayList<>();

                    String urlSound = track.getStreamURL();
                    if (null != urlSound) {
                        urlSound += "?client_id=" + ProjectConstants.CLIENT_ID;
                        String fileNameSound = urlSound.replaceAll("[^a-zA-z0-9]*", "_");
                        Request requestSound = new Request(urlSound, fileNameSound);
                        requests.add(requestSound);
                    }

                    String urlArtworkUrl = track.getArtworkURL();
                    if (null != urlArtworkUrl && urlArtworkUrl.startsWith("http")) {
                        String fileNameImage = urlArtworkUrl.replaceAll("[^a-zA-z0-9]*", "_");
                        Request requestImage = new Request(urlArtworkUrl, fileNameImage);
                        requests.add(requestImage);
                    }

                    List<Long> downloadIds = fetch.enqueue(requests);

                    track.setDowloadIds(downloadIds);
                    track.setDownloadingStatus(DownloadingStatus.IN_PROGRESS);
                    notifyDataSetChanged();
                } else if (track.isDowload() && track.getDownloadingStatus() == DownloadingStatus.DOWNLOADED) {
                    if (!(mContext instanceof PlayerListActivity)) {
                        mTracks.remove(position);
                    } else {
                        Track storedTrack = trackDao.getRecordByTitle(track.getTitle());
                        track.setStreamURL(storedTrack.getStreamURL());
                        track.setArtworkURL(storedTrack.getArtworkURL());
                        track.setDowload(false);
                        track.setDownloadingStatus(DownloadingStatus.NOT_DOWNLOADED);
                    }

                    for (Long id : track.getDowloadIds()) {
                        File fileDowloaded = fetch.getDownloadedFile(id);
                        if (null != fileDowloaded) {
                            fileDowloaded.delete();
                        }
                        fetch.remove(id);
                    }
                    trackDao.removeRecordByTitle(track.getTitle());
                    notifyDataSetChanged();
                }
            }
        });

        holder.trackSingerTextView.setText(track.getTitle().split("(-|–)")[0].trim());
        holder.trackTitleTextView.setText(track.getTitle().split("(-|–)")[1].trim());

        String iconUrl = track.getArtworkURL();
        if (null == iconUrl || iconUrl.isEmpty() || iconUrl.equals("null")){
            Picasso.with(mContext).load(R.mipmap.ic_launcher).fit().into(holder.trackImageView);
        } else if (!track.isDowload()) {
            Picasso.with(mContext).load(iconUrl).fit().into(holder.trackImageView);
        } else if (track.isDowload()) {
            Picasso.with(mContext).load(new File(iconUrl)).fit().into(holder.trackImageView);
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
            for (Track track : mTracks) {
                List<Long> ids = track.getDowloadIds();
                if (ids.contains(id)) {
                    boolean isRecord = trackDao.containRecordByTitle(track.getTitle());
                    if (!isRecord) {
                        trackDao.insertRecord(track, "", "");
                    }

                    String filePath = fetch.get(id).getFilePath();
                    if (ids.get(0).longValue() == id) {
                        trackDao.updateSoundDataByStreamUrl(track.getStreamURL(), filePath, id);
                        track.setStreamURL(filePath);
                        track.setDownloadingStatus(DownloadingStatus.DOWNLOADED);
                        track.setDowload(true);
                        notifyDataSetChanged();
                    } else if (ids.get(1).longValue() == id) {
                        trackDao.updateImageDataByStreamUrl(track.getStreamURL(), filePath, id);
                        track.setArtworkURL(filePath);
                    }
                    break;
                }
            }
        }

    }

}