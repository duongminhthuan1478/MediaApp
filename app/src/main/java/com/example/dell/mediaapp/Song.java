package com.example.dell.mediaapp;

/**
 * Created by Dell on 19-Jan-18.
 */

public class Song {
    private String mSongTitle;
    private int mImageResourceID;
    private int mAudioResourceID;

    public Song(String mSongTitle, int mAudioResourceID) {
        this.mSongTitle = mSongTitle;
        this.mAudioResourceID = mAudioResourceID;
    }

    public Song(String mSongTitle, int mImageResourceID, int mAudioResourceID) {

        this.mSongTitle = mSongTitle;
        this.mImageResourceID = mImageResourceID;
        this.mAudioResourceID = mAudioResourceID;
    }

    public String getmSongTitle() {
        return mSongTitle;
    }

    public void setmSongTitle(String mSongTitle) {
        this.mSongTitle = mSongTitle;
    }

    public int getmImageResourceID() {
        return mImageResourceID;
    }

    public void setmImageResourceID(int mImageResourceID) {
        this.mImageResourceID = mImageResourceID;
    }

    public int getmAudioResourceID() {
        return mAudioResourceID;
    }

    public void setmAudioResourceID(int mAudioResourceID) {
        this.mAudioResourceID = mAudioResourceID;
    }
}
