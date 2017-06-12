package com.irronsoft.aleh_struneuski.audio_back.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.squareup.picasso.Picasso;
import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.listener.FetchListener;
import com.tonyodev.fetch.request.Request;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
        fetch.setConcurrentDownloadsLimit(4);
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
            holder.progressView = (GeometricProgressView) convertView.findViewById(R.id.progressView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (trackItem.isDowload()) {
            holder.progressView.setVisibility(View.GONE);
            holder.dowloadImageView.setVisibility(View.VISIBLE);
            holder.dowloadImageView.setBackgroundResource(R.drawable.ic_remove);
        } else if (trackItem.getDownloadingStatus() == DownloadingStatus.IN_PROGRESS) {
            holder.dowloadImageView.setVisibility(View.GONE);
            holder.progressView.setVisibility(View.VISIBLE);
        } else {
            holder.progressView.setVisibility(View.GONE);
            holder.dowloadImageView.setVisibility(View.VISIBLE);
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
                int selectedTrack =  ((ListView) view.getParent().getParent()).getPositionForView(view);
                Track track = mTracks.get(selectedTrack);
                if (!track.isDowload() && track.getDownloadingStatus() == DownloadingStatus.NOT_DOWNLOADED) {

//                    ViewHolder viewHolder = (ViewHolder) view.getRootView().getTag();
//                    ListView llist = (ListView) view.getParent().getParent();
//                    holder.dowloadImageView.setVisibility(View.GONE);
//                    holder.progressView.setVisibility(View.VISIBLE);

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
                        mTracks.remove(selectedTrack);
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

        holder.trackSingerTextView.setText(trackItem.getTitle().split("(-|–)")[0].trim());
        holder.trackTitleTextView.setText(trackItem.getTitle().split("(-|–)")[1].trim());

        if (trackItem.getDownloadingStatus() != DownloadingStatus.IN_PROGRESS) {
            String iconUrl = trackItem.getArtworkURL();
            if (null == iconUrl || iconUrl.isEmpty() || iconUrl.equals("null")){
                Picasso.with(mContext).load(R.mipmap.ic_launcher).fit().into(holder.trackImageView);
            } else if (iconUrl.startsWith("http")) {
                Picasso.with(mContext).load(iconUrl).fit().into(holder.trackImageView);
            } else if (!iconUrl.startsWith("http")) {
                Picasso.with(mContext).load(new File(iconUrl)).fit().into(holder.trackImageView);
            }
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
        } else if (status == Fetch.STATUS_ERROR) {
            for (Track track : mTracks) {
                List<Long> ids = track.getDowloadIds();
                if (null != ids && !ids.isEmpty() && ids.contains(id)) {
                    for (Long idTrack : ids) {
                        File fileDowloaded = fetch.getDownloadedFile(idTrack);
                        if (null != fileDowloaded) {
                            fileDowloaded.delete();
                        }
                        fetch.remove(idTrack);
                    }

                    track.setDowloadIds(Collections.EMPTY_LIST);
                    track.setDowload(false);
                    track.setDownloadingStatus(DownloadingStatus.NOT_DOWNLOADED);


                    trackDao.removeRecordByTitle(track.getTitle());
                    notifyDataSetChanged();
                }
            }
        }
    }

}