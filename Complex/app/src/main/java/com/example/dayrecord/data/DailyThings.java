package com.example.dayrecord.data;

public class DailyThings {
    private String mTitle;
    private String mContent;
    private String mLabel;
    private String id;
    private String time;
    private String identity;

    //构造函数
    public DailyThings(String mTitle, String mContent, String mLabel, String id) {
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mLabel = mLabel;
        this.id = id;
    }

    public DailyThings(String mTitle, String mContent, String mLabel, String id, String time, String identity) {
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mLabel = mLabel;
        this.id = id;
        this.time = time;
        this.identity = identity;
    }

    public DailyThings() {
    }

    //setter
    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
    public void setmContent(String mContent) {
        this.mContent = mContent;
    }
    public void setmLabel(String mLabel) {
        this.mLabel = mLabel;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setIdentity(String identity) {
        this.identity = identity;
    }

    //getter
    public String getmTitle() {
        return mTitle;
    }
    public String getmContent() {
        return mContent;
    }
    public String getmLabel() {
        return mLabel;
    }
    public String getId() {
        return id;
    }
    public String getTime() {
        return time;
    }
    public String getIdentity() {
        return identity;
    }
}
