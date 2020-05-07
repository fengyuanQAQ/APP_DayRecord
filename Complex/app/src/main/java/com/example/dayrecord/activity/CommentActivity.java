package com.example.dayrecord.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dayrecord.R;
import com.example.dayrecord.Utils.GlobalManager;
import com.example.dayrecord.Utils.NoQuickClick;
import com.example.dayrecord.data.Comment;
import com.example.dayrecord.data.User;
import com.example.dayrecord.server.InfoHub;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {

    private InfoHub mInfoHub;//管理网络交互信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        mInfoHub=new InfoHub();
        init();
    }

    private void init(){
        //返回按钮
        ImageButton btnBack=findViewById(R.id.discover_comment_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //显示评论
        RecyclerView recyclerView=findViewById(R.id.discover_comment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom=2;
            }
        });

        //获取分享的id,加载评论
        Bundle extras = getIntent().getExtras();
        long id=0;
        String userid="user";
        if (extras != null) {
             id = extras.getLong("id");
             userid = extras.getString("userid");
        }
        //加载评论
        mInfoHub.loadComment(this,id,recyclerView);
        //发送评论
        EditText edtContent=findViewById(R.id.discover_comment_sendContent);
        TextView send=findViewById(R.id.discover_comment_send);
        //发送评论
        long finalId = id;
        send.setOnClickListener(new NoQuickClick.OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                String content=edtContent.getText().toString();
                if ("".equals(content)) {
                    Toast.makeText(CommentActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    //提交评论 重新加载
                    User user= GlobalManager.user;
                    String time = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
                    Comment comment=new Comment(0,0,  content,user.getId(),time,user.getNickname(),
                            (int) finalId,user.getHeadimage());
                    //添加数据到数据库
                    mInfoHub.addComment(CommentActivity.this,comment,recyclerView);

                    //清除评论
                    edtContent.setText("");
                }
            }
        });
    }

}
