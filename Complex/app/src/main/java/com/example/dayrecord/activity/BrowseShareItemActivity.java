package com.example.dayrecord.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dayrecord.Utils.GlobalManager;
import com.example.dayrecord.R;
import com.example.dayrecord.data.Sharings;

import java.io.File;

public class BrowseShareItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_share_item);
        //找到相关组件
        ImageButton ivBtnBack=findViewById(R.id.show_share_all_back);
        //返回
        ivBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvDay,tvDate,tvTime;
        tvDay=findViewById(R.id.show_share_all_day);
        tvDate=findViewById(R.id.show_share_all_date);
        tvTime=findViewById(R.id.show_share_all_time);
        TextView tvContent,tvAuthor;
        tvContent=findViewById(R.id.show_share_all_content);
        tvAuthor=findViewById(R.id.show_share_all_author);
        ImageView ivImage=findViewById(R.id.show_share_all_image);

        //解析传递过来的数据
        Bundle bundle=getIntent().getExtras();
        String day,time,date;
        Sharings sharingThings;
        int index;
        if(bundle!=null){
            //传递过来的信息
            index=bundle.getInt("index");//全局缓存sharingList的索引
            day=bundle.getString("day");
            time=bundle.getString("time");
            date=bundle.getString("date");
            //根据全局缓存提取SharingTHings的信息
            sharingThings= GlobalManager.sharingList.get(index);
            //加载信息
            tvDay.setText(day);
            tvDate.setText(date);
            tvTime.setText(time);

            //加在sharing的信息
            tvContent.setText(sharingThings.getContent());
            String author="——"+sharingThings.getAuthor();
            tvAuthor.setText(author);
            if(null!=sharingThings.getImage()) {
                Glide.with(this).load(new File(sharingThings.getImage())).into(ivImage);
            }
        }

    }
}
