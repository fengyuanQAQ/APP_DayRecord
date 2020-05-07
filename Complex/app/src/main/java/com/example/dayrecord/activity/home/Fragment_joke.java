package com.example.dayrecord.activity.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dayrecord.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Fragment_joke extends Fragment {

    private List<Joke> jokeList=new ArrayList<Joke>();
    private String Urlpath="http://api.laifujiangxiaohua.com/open/xiaohua.json";   //接口地址
    private Button btn;
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
        View view = inflater.inflate(R.layout.activity_joke, container,false);
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
                parseJSONWithJSONObject1(data);
            }
            return false;
        }
    });

    //提取数据
    private void parseJSONWithJSONObject(String data,int num) {
        try{
            JSONObject jsonObject=new JSONObject(data);
            switch (num){
                case 1:
                    String data1=jsonObject.getString("joke");
//                    Log.i("内容1","   :"+data1);
                    Joke item=new Joke("",data1);
                    jokeList.add(item);

                    //parseJSONWithJSONObject(data1,2);
                    break;
                case 2:

                    String data2=jsonObject.getString("data");
                    parseJSONWithJSONObject1(data2);
                    break;
            }
            JokeAdapter adapter = new JokeAdapter(getActivity(),
                    R.layout.joke_item,jokeList);
            ListView listView=thisView.findViewById(R.id.list_joke_item);
            listView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //数据提取2
    private void parseJSONWithJSONObject1(String string) {
        try{
            JSONArray jsonArray=new JSONArray(string);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String title=jsonObject.getString("title");
                String content=jsonObject.getString("content");
                String poster=jsonObject.getString("poster");
                String data=content.replace("<br/><br/>","\n");

                Joke item=new Joke(title,data);
                jokeList.add(item);



                NextNeedDo(title,content,poster);
            }

            JokeAdapter adapter = new JokeAdapter(getActivity(),
                    R.layout.joke_item,jokeList);
            ListView listView=thisView.findViewById(R.id.list_joke_item);
            listView.setAdapter(adapter);



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //接着做
    private void NextNeedDo(String title, String content, String poster) {
//        Log.i("标题","   :"+title);
//        Log.i("内容","   :"+content);
//        Log.i("插图","   :"+poster);
    }


}
