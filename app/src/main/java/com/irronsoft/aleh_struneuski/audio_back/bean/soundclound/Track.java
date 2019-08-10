package com.irronsoft.aleh_struneuski.audio_back.bean.soundclound;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Track implements Parcelable {

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
    private List<Long> mDowloadIds = new ArrayList<>();
    private DownloadingStatus downloadingStatus = DownloadingStatus.NOT_DOWNLOADED;
    private boolean isDowload;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("id")
    private int mID;
    @SerializedName("stream_url")
    private String mStreamURL;
    @SerializedName("artwork_url")
    private String mArtworkURL;

    public Track() {
    }

    protected Track(Parcel in) {
        mTitle = in.readString();
        mID = in.readInt();
        mStreamURL = in.readString();
        mArtworkURL = in.readString();
    }

    public boolean isDowload() {
        return isDowload;
    }

    public void setDowload(boolean isDowloaded) {
        isDowload = isDowloaded;
    }

    public List<Long> getDowloadIds() {
        return mDowloadIds;
    }

    public void setDowloadIds(List<Long> dowloadIds) {
        this.mDowloadIds = dowloadIds;
    }

    public DownloadingStatus getDownloadingStatus() {
        return downloadingStatus;
    }

    public void setDownloadingStatus(DownloadingStatus downloadingStatus) {
        this.downloadingStatus = downloadingStatus;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getID() {
        return mID;
    }

    public void setID(int id) {
        mID = id;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public void setStreamURL(String streamURL) {
        mStreamURL = streamURL;
    }

    public String getArtworkURL() {
        return mArtworkURL;
    }

    public void setArtworkURL(String artworkURL) {
        mArtworkURL = artworkURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeInt(mID);
        dest.writeString(mStreamURL);
        dest.writeString(mArtworkURL);
    }

}
