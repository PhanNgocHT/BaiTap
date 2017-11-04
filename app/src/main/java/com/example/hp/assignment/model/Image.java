package com.example.hp.assignment.model;

/**
 * Created by hp on 10/23/2017.
 */

public class Image {
    private String mId;
    private String mIndex;
    private byte[] mImage;

    public Image(String id, String index, byte[] image) {
        mId = id;
        mIndex = index;
        mImage = image;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmIndex() {
        return mIndex;
    }

    public void setmIndex(String mIndex) {
        this.mIndex = mIndex;
    }

    public byte[] getmImage() {
        return mImage;
    }

    public void setmImage(byte[] mImage) {
        this.mImage = mImage;
    }
}
