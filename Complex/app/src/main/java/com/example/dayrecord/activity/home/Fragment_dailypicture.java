package com.example.dayrecord.activity.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dayrecord.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Fragment_dailypicture extends Fragment {
    private String Urlpath="https://v1.alapi.cn/api/bing?format=json";   //接口地址
    private HttpURLConnection connection;
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private int GET_DATA_SUCCESS=1;//获取文本数据成功的标志
    private int GET_IMAGE_SUCCESS=2;//获取图片成功的标志
    private int GET_DATA_FLAG=101;//获取文本数据标志
    private int GET_IMAGE_FLAG=102;//获取图片的标志
    private ImageView imagePic;
    private TextView picData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dailypicture, container,false);
        /**
         * 初始化控件
         */
        InitUI(view);
        InitData();
        return view;
    }
    //初始化数据
    private void InitData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetDataFromUrl(Urlpath,GET_DATA_FLAG);
            }
        }).start();
//        Toast.makeText(getContext(), "刷新成功！", Toast.LENGTH_LONG).show();

    }

    //初始化控件
    private void InitUI(View view) {
        imagePic = view.findViewById(R.id.dailypicture);
        picData = view.findViewById(R.id.picdec);

    }


    //从网络中获取数据
    private String GetDataFromUrl(String urlpath, int FLAG) {
        try{
            //1.创建URL
            URL url = new URL(urlpath);
            //2.打开链接
            connection = (HttpURLConnection)url.openConnection();
            //3.判断并处理结果
            if(connection.getResponseCode()==200){
                //获取输入流
                inputStream = connection.getInputStream();
                switch (FLAG){
                    case 101:
                        //文本数据
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder stringBuilder=new StringBuilder();
                        for(String line="";(line= bufferedReader.readLine())!=null;){
                            stringBuilder.append(line);
                        }
                        String data= stringBuilder.toString();
                        Message message = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("data",data);
                        message.setData(bundle);
                        message.what=GET_DATA_SUCCESS;
                        //向主线程发送数据
                        handler.sendMessage(message);
                        break;

                    case 102:
                        //图片数据
                        //使用工厂把网络的输入流生产Bitmap
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        //利用Message把图片发给Handler
                        Message msg = Message.obtain();
                        msg.obj = bitmap;
                        msg.what = GET_IMAGE_SUCCESS;
                        handler.sendMessage(msg);
                        break;


                }



            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
                if(inputStream!=null){
                    inputStream.close();
                }
                if(connection!=null){
                    connection.disconnect();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "";

    }

    //线程通信
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if(msg.what==GET_DATA_SUCCESS){
                //文本数据
                String data=msg.getData().getString("data");
//                Log.i("数据","   :"+data);
                parseJSONWithJSONObject(data,1);
            }
            if(msg.what==GET_IMAGE_SUCCESS){
                //图片数据
                Bitmap bitmap = (Bitmap) msg.obj;
                imagePic.setImageBitmap(bitmap);
            }
            return false;
        }
    });

    //提取文本数据
    private void parseJSONWithJSONObject(String data,int num) {
        try{
            JSONObject jsonObject=new JSONObject(data);
            switch (num){
                case 1:
                    final String Data=jsonObject.getString("data");

                    parseJSONWithJSONObject(Data,2);
                    break;
                case 2:
                    final String imgurl=jsonObject.getString("url");
//                    Log.i("图片链接","   :"+imgurl);
                    //设置图片
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            GetDataFromUrl(imgurl,GET_IMAGE_FLAG);
                        }
                    }).start();
                    String info=jsonObject.getString("copyright");
                    NextNeedDo(info);

                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //接着做
    private void NextNeedDo(String info) {
        //Log.i("时间","   :"+date);
//        Log.i("描述","   :"+info);
        picData.setText("      "+info);
    }

}
