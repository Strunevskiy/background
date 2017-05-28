package com.irronsoft.aleh_struneuski.audio_back.database.dao.impl;

import android.content.Context;
import android.database.Cursor;

import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.DownloadingStatus;
import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;
import com.irronsoft.aleh_struneuski.audio_back.database.DatabaseHelper;
import com.irronsoft.aleh_struneuski.audio_back.database.dao.TrackDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alehstruneuski on 5/1/17.
 */
public class TrackDaoImpl implements TrackDao {

    private Context context;

    public TrackDaoImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<Track> getTracksFromDataBase() {
        List<Track> tracks = new ArrayList<>();
        Cursor cursor = DatabaseHelper.getInstance(context).get();
            try {
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        Track track = new Track();
                        track.getDowloadIds().add(cursor.getLong(DatabaseHelper.INDEX_COLUMN_ID_SOUND_FILEPATH));
                        track.getDowloadIds().add(cursor.getLong(DatabaseHelper.INDEX_COLUMN_ID_IMAGE_FILEPATH));
                        track.setTitle(cursor.getString(DatabaseHelper.INDEX_COLUMN_TITLE));
                        track.setStreamURL(cursor.getString(DatabaseHelper.INDEX_COLUMN_SOUND_FILEPATH));
                        track.setArtworkURL(cursor.getString(DatabaseHelper.INDEX_COLUMN_IMAGE_FILEPATH));
                        track.setDownloadingStatus(DownloadingStatus.DOWNLOADED);
                        track.setDowload(true);
                        tracks.add(track);
                    }
                }
            } finally {
                cursor.close();
            }
        return tracks;
    }

    @Override
    public void tagDowloadedTracks(List<Track> tracks) {
        for (Track track: tracks) {
            Cursor cursor = DatabaseHelper.getInstance(context).getByStreamUrl(track.getStreamURL());
            try {
                if (null != cursor && cursor.getCount() > 0 && cursor.moveToFirst()) {
                    track.getDowloadIds().add(cursor.getLong(DatabaseHelper.INDEX_COLUMN_ID_SOUND_FILEPATH));
                    track.getDowloadIds().add(cursor.getLong(DatabaseHelper.INDEX_COLUMN_ID_IMAGE_FILEPATH));
                    track.setStreamURL(cursor.getString(DatabaseHelper.INDEX_COLUMN_SOUND_FILEPATH));
                    track.setArtworkURL(cursor.getString(DatabaseHelper.INDEX_COLUMN_IMAGE_FILEPATH));
                    track.setDownloadingStatus(DownloadingStatus.DOWNLOADED);
                    track.setDowload(true);
                }
            } finally {
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
    public boolean updateSoundDataByStreamUrl(String streamUrl, String filePath, long idStreamFilePath) {
        return DatabaseHelper.getInstance(context).updateSoundData(streamUrl, filePath, idStreamFilePath);
    }

    @Override
    public boolean updateImageDataByStreamUrl(String streamUrl, String filePath, long idImageFilePath) {
        return DatabaseHelper.getInstance(context).updateImageData(streamUrl, filePath, idImageFilePath);
    }

    @Override
    public Track getRecordByTitle(String title) {
        Cursor cursor = DatabaseHelper.getInstance(context).getByTitle(title);
        try {
            if (null != cursor && cursor.getCount() > 0 && cursor.moveToFirst()) {
                Track track = new Track();
                track.setTitle(cursor.getString(DatabaseHelper.INDEX_COLUMN_TITLE));
                track.setStreamURL(cursor.getString(DatabaseHelper.INDEX_COLUMN_STREAM_URL));
                track.setArtworkURL(cursor.getString(DatabaseHelper.INDEX_COLUMN_ARTWORK_URL));
                return track;
            }
        } finally {
            cursor.close();
        }
        return null;
    }

}
