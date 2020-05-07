package com.example.dayrecord.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_DAILY="create table daily("+
            "id integer primary key autoincrement,"+
            "title text,"+
            "label text,"+
            "num text,"+
            "shijian text,"+
            "pos text,"+    //label:pos
            "content text)";

    private static final String CREATE_TABLE_SHARING_STARS="create table sharing_stars("+
            "id integer primary key autoincrement,"+
            "userid text,"+
            "mark integer)";

    public MyDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //创建一个daily表 记录行程
        db.execSQL(CREATE_TABLE_DAILY);

        //创建一个记录点赞情况的数据表
        db.execSQL(CREATE_TABLE_SHARING_STARS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
