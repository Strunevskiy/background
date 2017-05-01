package com.irronsoft.aleh_struneuski.audio_back.database.dao;

import java.util.List;

/**
 * Created by alehstruneuski on 5/1/17.
 */
public interface TrackDao<T> {

    public List<T> getTracksFromDataBase();
    public boolean insertRecord(T record, String soundFilePath, String imageFilePath);
    public boolean updateSoundDataByStreamUrl(String streamUrl, String filePath);
    public boolean updateImageDataByStreamUrl(String streamUrl, String filePath);
    public boolean removeRecordByTitle(String title);
    public boolean containRecordByTitle(String title);
    public void tagDowloadedTracks(List<T> items);

}
