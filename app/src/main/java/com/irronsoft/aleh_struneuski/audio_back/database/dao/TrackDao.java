package com.irronsoft.aleh_struneuski.audio_back.database.dao;

import com.irronsoft.aleh_struneuski.audio_back.bean.soundclound.Track;

import java.util.List;

/**
 * Created by alehstruneuski on 5/1/17.
 */
public interface TrackDao {

    public List<Track> getTracksFromDataBase();
    public boolean insertRecord(Track record, String soundFilePath, String imageFilePath);
    public boolean updateSoundDataByStreamUrl(String streamUrl, String streamFilePath, long idStreamFilePath);
    public boolean updateImageDataByStreamUrl(String streamUrl, String imageFilePath, long idImageFilePath);
    public Track getRecordByTitle(String title);
    public boolean removeRecordByTitle(String title);
    public boolean containRecordByTitle(String title);
    public void tagDowloadedTracks(List<Track> items);

}
