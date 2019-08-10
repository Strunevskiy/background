package com.irronsoft.aleh_struneuski.audio_back.bean.soundclound;

/**
 * Created by alehstruneuski on 4/23/17.
 */
public enum DownloadingStatus {

    //The item has not been started for downloading.
    NOT_DOWNLOADED("notDownloaded"),
    //The item has been started for downloading, but due to other downloads, it is in waiting.
    WAITING("waiting"),
    //The item is downloading.
    IN_PROGRESS("inProgress"),
    //The item has been downloaded.
    DOWNLOADED("downloaded");

    private String downloadStatus;

    DownloadingStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public static DownloadingStatus getValue(String status) {
        for (DownloadingStatus downloadingStatus : DownloadingStatus.values()) {
            if (downloadingStatus.getDownloadStatus().equalsIgnoreCase(status)) {
                return downloadingStatus;
            }
        }
        return null;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

}
