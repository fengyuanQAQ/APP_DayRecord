package com.example.dayrecord.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.dayrecord.data.DailyThings;
import com.example.dayrecord.data.Sharings;
import com.example.dayrecord.data.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class GlobalManager {
    public static final String DBNAEM = "commom";
    public static User user;
    public static SQLiteDatabase sqlite;

    //头像字节流缓存
    public static byte[] headBytes;

    //记录日常事件 键值对存储 键：标签 值：DailyThings列表
    public static Map<String,List<DailyThings>> dailyMap;
    public static List<String> stringList;//辅助map的列表
//    public static List<DailyThings> dailyThingsList;//显示最近添加的日常
    //记录分享事件
    public static List<Sharings> sharingList;

    public static void init(Context context){
        MyDBHelper myHelper=new MyDBHelper(context, GlobalManager.DBNAEM, null, 1);
        sqlite=myHelper.getReadableDatabase();
        dailyMap=new HashMap<>();
        sharingList =new ArrayList<>();
        stringList=new ArrayList<>();
//        dailyThingsList=new ArrayList<>();
        user=new User();
    }

    public static void loadDaily(String number){
        //查找用户日常
        Cursor cursor=sqlite.query("daily",null,null,null,null,
                null,null);
        if(cursor!=null){
            while(cursor.moveToNext()){
                //账号匹配
                if(cursor.getString(cursor.getColumnIndex("num")).equals(number)){
                    String title=cursor.getString(cursor.getColumnIndex("title"));
                    String label =cursor.getString(cursor.getColumnIndex("label"));
                    String content =cursor.getString(cursor.getColumnIndex("content"));
                    String time=cursor.getString(cursor.getColumnIndex("shijian"));
                    String pos=cursor.getString(cursor.getColumnIndex("pos"));
                    DailyThings daily=new DailyThings(title,content,label,number,time,pos);

                    //存在标签
                    if(dailyMap.containsKey(label)){
                        dailyMap.get(label).add(daily);
                    }else{
                        //不存在标签
                        stringList.add(label);//添加进入
                        List<DailyThings> list=new ArrayList<>();
                        list.add(daily);
                        dailyMap.put(label,list);
                    }
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    //查询数据库的元素
    public static String getDataFromDb(SQLiteDatabase sq, String table, String identity) {
        Cursor cursor = sq.query(table, null, null, null, null
                , null, null);
        String result = "";
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (user.getId().equals(cursor.getString(cursor.getColumnIndex("num")))) {
                    result = cursor.getString(cursor.getColumnIndex(identity));
                    break;
                }
            }
        }
        cursor.close();
        return result;
    }

    //动态获取权限
    public static void getWriterPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE}; //验证是否许可权限
            for (String str : permissions) {
                if (activity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    activity.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }


    //根据图片路径加载图片
    public static void loadHeadImage(Activity activity,ImageView iv,String imagePath){
        //加载头像
            if (!"".equals(imagePath)) {

                Uri uri = Uri.fromFile(new File(imagePath));
                try {
                    iv.setImageBitmap(MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    //设置透明度
    public static void setTransparent(Activity activity,PopupWindow popWnd){
        final Window window=activity.getWindow();
        final WindowManager.LayoutParams lp=window.getAttributes();
        lp.alpha=0.6f;//设置透明度
        window.setAttributes(lp);
        popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //恢复此时的透明度
                lp.alpha=1f;
                window.setAttributes(lp);
            }
        });
    }

    //文件转换为字节数组
    public static byte[] fileToBytes(File file){
        byte[] buf=null;
        FileInputStream fis=null;
        ByteArrayOutputStream bos=null;

        //获取输入流
        try {
            fis=new FileInputStream(file);
            bos=new ByteArrayOutputStream();
            byte[] bytes=new byte[1024];//读取数组
            int len=0;
            while((len=fis.read(bytes))!=-1){
                bos.write(bytes,0,len);
            }
            //转换为byte数组
            buf=bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buf;
    }

}
