package com.irronsoft.aleh_struneuski.audio_back.database.dao.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.DownloadingStatus;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;
import com.irronsoft.aleh_struneuski.audio_back.database.DatabaseHelper;
import com.irronsoft.aleh_struneuski.audio_back.database.dao.TrackDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alehstruneuski on 5/1/17.
 */
public class TrackDaoImpl implements TrackDao<Track> {

    private Context context;

    public TrackDaoImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<Track> getTracksFromDataBase() {
        List<Track> tracks = new ArrayList<>();
        Cursor cursor = DatabaseHelper.getInstance(context).get();
        if (cursor != null && cursor.getCount() > 0) {
            try {
                while (cursor.moveToNext()) {
                    Track track = new com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track();
                    track.setTitle(cursor.getString(DatabaseHelper.INDEX_COLUMN_TITLE));
                    track.setStreamURL(cursor.getString(DatabaseHelper.INDEX_COLUMN_SOUND_FILEPATH));
                    track.setArtworkURL(cursor.getString(DatabaseHelper.INDEX_COLUMN_IMAGE_FILEPATH));
                    track.setDownloadingStatus(DownloadingStatus.DOWNLOADED);
                    tracks.add(track);
                }
            } finally {
                cursor.close();
            }
        }
        return tracks;
    }

    @Override
    public void tagDowloadedTracks(List<Track> tracks) {
        for (Track track: tracks) {
            Cursor cursor = DatabaseHelper.getInstance(context).getByStreamUrl(track.getStreamURL());
            if (null != cursor && cursor.getCount() > 0) {
                track.setDownloadingStatus(DownloadingStatus.DOWNLOADED);
                track.setStreamURL(cursor.getString(DatabaseHelper.INDEX_COLUMN_STREAM_URL));
                cursor.close();
            }
        }
    }

    public boolean removeRecordByTitle(String title) {
        return DatabaseHelper.getInstance(context).removeByTitle(title);
    }

    public boolean containRecordByTitle(String title) {
        return DatabaseHelper.getInstance(context).containRecordByTitle(title);
    }

    public boolean insertRecord(Track track, String soundFilePath, String imageFilePath) {
        return DatabaseHelper.getInstance(context).insert(track.getID(), track.getTitle(), track.getStreamURL(), track.getArtworkURL(), soundFilePath, imageFilePath);
    }

    @Override
    public boolean updateSoundDataByStreamUrl(String streamUrl, String filePath) {
        return DatabaseHelper.getInstance(context).updateSoundData(streamUrl, filePath);
    }

    @Override
    public boolean updateImageDataByStreamUrl(String streamUrl, String filePath) {
        return DatabaseHelper.getInstance(context).updateImageData(streamUrl, filePath);
    }

}
