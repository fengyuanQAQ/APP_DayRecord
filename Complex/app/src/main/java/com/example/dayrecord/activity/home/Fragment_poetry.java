package com.example.dayrecord.activity.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Fragment_poetry extends Fragment {
    private String Urlpath="https://v1.jinrishici.com/all";   //接口地址
    private TextView text;
    private TextView name;
    private HttpURLConnection connection;
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private int GET_DATA_SUCCESS=1;//获取数据成功的标志


    private View thisView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_poetry, container,false);
        /**
         * 初始化控件
         */
        thisView=view;
        InitUI();
        InitData();
        return view;
    }

    //初始化数据
    private void InitData() {
        new Thread(new Runnable() {

            private String data;

            @Override
            public void run() {
                data = GetDataFromUrl();
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("data",data);
                message.setData(bundle);
                message.what=GET_DATA_SUCCESS;
                //向主线程发送数据
                handler.sendMessage(message);

            }
        }).start();
//        Toast.makeText(getContext(), "刷新成功！", Toast.LENGTH_LONG).show();

    }

    //初始化控件
    private void InitUI() {
        text = thisView.findViewById(R.id.text1);
        name = thisView.findViewById(R.id.name);
        text.setMovementMethod(ScrollingMovementMethod.getInstance());
    }


    //从网络中获取数据
    private String GetDataFromUrl() {
        try{
            //1.创建URL
            URL url = new URL(Urlpath);
            //2.打开链接
            connection = (HttpURLConnection)url.openConnection();
            //3.判断并处理结果
            if(connection.getResponseCode()==200){
                //获取输入流
                inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder=new StringBuilder();
                for(String line="";(line= bufferedReader.readLine())!=null;){
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();

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
                String data=msg.getData().getString("data");
//                Log.i("数据","   :"+data);
                parseJSONWithJSONObject(data);
            }
            return false;
        }
    });

    //提取数据
    private void parseJSONWithJSONObject(String data) {
        try{
            JSONObject jsonObject=new JSONObject(data);
            String content=jsonObject.getString("content");
            String author=jsonObject.getString("author");
            String title=jsonObject.getString("origin");
            NextNeedDo(content,author,title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //接着做
    private void NextNeedDo(String content, String author, String title) {
//        Log.i("内容","   :"+content);
//        Log.i("作者","   :"+author);
//        Log.i("名称","   :"+title);
        String string="—"+author+"《"+title+"》—";
        text.setText("      "+content);
        name.setText(string);
    }

}
