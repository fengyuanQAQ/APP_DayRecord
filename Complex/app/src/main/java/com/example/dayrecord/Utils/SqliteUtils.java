package com.example.dayrecord.Utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqliteUtils {

     /*
     * 打印数据库数据
     */
    public static void printDB(SQLiteDatabase sqlite) {
        Cursor cur = sqlite.query("sharing_stars", null, null, null
                , null, null, null);
        if (cur != null) {
            while (cur.moveToNext()) {
                String userid = cur.getString(cur.getColumnIndex("userid"));
                int id = cur.getInt(cur.getColumnIndex("mark"));
                Log.d("11111", "userid->" + userid + " id->" + id);
            }
        }
    }


}
