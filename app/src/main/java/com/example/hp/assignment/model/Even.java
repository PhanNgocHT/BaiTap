package com.example.hp.assignment.model;

/**
 * Created by hp on 10/20/2017.
 */

public class Even {
    private String mId;
    private Image[] mImages;
    private String mTitle;
    private String mContent;
    private String mDate;
    private String mTime;
    private String mAlarm;
    private int mBackground;

    public Even() {
    }

    public Even(String id, String title, String content, String date, String time, String alarm, int background) {
        mId = id;
        mTitle = title;
        mContent = content;
        mDate = date;
        mTime = time;
        mAlarm = alarm;
        mBackground=background;
    }


    public Even(String id, Image[] images, String title, String content, String date, String time, String alarm) {
        mId = id;
        mImages=images;
        mTitle = title;
        mContent = content;
        mDate = date;
        mTime = time;
        mAlarm = alarm;
    }

    public int getmBackground() {
        return mBackground;
    }

    public void setmBackground(int mBackground) {
        this.mBackground = mBackground;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public Image[] getmImages() {
        return mImages;
    }

    public void setmImages(Image[] mImages) {
        this.mImages = mImages;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmAlarm() {
        return mAlarm;
    }

    public void setmAlarm(String mAlarm) {
        this.mAlarm = mAlarm;
    }
}
