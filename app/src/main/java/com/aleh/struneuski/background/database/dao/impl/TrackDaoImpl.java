package com.aleh.struneuski.background.database.dao.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;

import com.aleh.struneuski.background.bean.soundclound.DownloadingStatus;
import com.aleh.struneuski.background.bean.soundclound.Track;
import com.aleh.struneuski.background.database.DatabaseHelper;
import com.aleh.struneuski.background.database.dao.TrackDao;

import java.util.ArrayList;
import java.util.List;

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
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return tracks;
    }

    @Override
    public void tagDowloadedTracks(List<Track> tracks) {
        for (Track track : tracks) {
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
                if (null != cursor && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        }
    }

    public boolean removeRecordByTitle(String title) {
        String escapeTitle = DatabaseUtils.sqlEscapeString(title);
        return DatabaseHelper.getInstance(context).removeByTitle(escapeTitle);
    }

    public boolean containRecordByTitle(String title) {
        String escapeTitle = DatabaseUtils.sqlEscapeString(title);
        return DatabaseHelper.getInstance(context).containRecordByTitle(escapeTitle);
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
        String escapeTitle = DatabaseUtils.sqlEscapeString(title);
        Cursor cursor = DatabaseHelper.getInstance(context).getByTitle(escapeTitle);
        try {
            if (null != cursor && cursor.getCount() > 0 && cursor.moveToFirst()) {
                Track track = new Track();
                track.setTitle(cursor.getString(DatabaseHelper.INDEX_COLUMN_TITLE));
                track.setStreamURL(cursor.getString(DatabaseHelper.INDEX_COLUMN_STREAM_URL));
                track.setArtworkURL(cursor.getString(DatabaseHelper.INDEX_COLUMN_ARTWORK_URL));
                return track;
            }
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

}
