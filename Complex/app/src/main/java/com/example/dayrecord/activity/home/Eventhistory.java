package com.example.dayrecord.activity.home;

public class Eventhistory {
    private String date;//日期
    private String title;//标题


    public Eventhistory(String date, String title){
        this.date=date;
        this.title=title;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

}
