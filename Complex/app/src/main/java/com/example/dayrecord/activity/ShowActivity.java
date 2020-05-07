package com.example.dayrecord.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.dayrecord.Utils.GlobalManager;
import com.example.dayrecord.Utils.MyDBHelper;
import com.example.dayrecord.R;
import com.example.dayrecord.data.Sharings;
import com.example.dayrecord.fragment.ShowDailyFragment;
import com.example.dayrecord.fragment.ShowShareFragment;
import com.example.dayrecord.server.InfoHub;

import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends AppCompatActivity {

    private  int request_show_type;

    private InfoHub mInfoHub;//信息交互

    //数据库相关
    private SQLiteDatabase sqlite;
    private ShowShareFragment showShareFragment;
    private ShowDailyFragment showDailyFragment;

    private TextView mTvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        mInfoHub=new InfoHub();

        //获取显示标志
        request_show_type=getIntent().getExtras().getInt("showwhat",0);

        //数据库
        MyDBHelper myHelper=new MyDBHelper(this, GlobalManager.DBNAEM, null, 1);
        sqlite=myHelper.getReadableDatabase();
        //找到相关组件
        ImageButton back,edit;
        back=findViewById(R.id.mine_show_close);
        mTvTitle=findViewById(R.id.mine_show_title);
        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edit=findViewById(R.id.mine_show_edit);
        //弹出窗口
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupWindow popupWindow=new PopupWindow();
                View view= LayoutInflater.from(ShowActivity.this).inflate(R.layout.popwnd_activity_show_edit,null);
                editPopWnd(popupWindow,view);
                //删除全部
                Button btnClearAll=view.findViewById(R.id.show_edit_clearall);
                btnClearAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearAll();
                        popupWindow.dismiss();
                        if(request_show_type==1){
                            showShareFragment.updateAdapter();
                        }else
                            showDailyFragment.onResume();
                    }
                });

                //取消
                Button btnCancel=view.findViewById(R.id.show_edit_cancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });

        //显示
        show();
    }

    //显示
    private void show(){
        //share
        if(request_show_type==1){
            mTvTitle.setText("感悟");
            if(showShareFragment==null){
                showShareFragment=new ShowShareFragment();
            }
            getSupportFragmentManager().beginTransaction().add(R.id.show_framelayout
            ,showShareFragment).commitAllowingStateLoss();
        }else{
            mTvTitle.setText("备忘");
            if(showDailyFragment==null){
                showDailyFragment=new ShowDailyFragment();
            }
            getSupportFragmentManager().beginTransaction().add(R.id.show_framelayout
                    ,showDailyFragment).commitAllowingStateLoss();
        }
    }

    //清除
    private void clearAll(){
        //1 share; 2 daily
        //清除全局缓存
        if(request_show_type==1){

            List<Integer> list=new ArrayList<>();
            for (Sharings sharings : GlobalManager.sharingList) {
                list.add(sharings.getId());
//                Log.d("1111", String.valueOf(sharings.getId()));
            }

            //清除全局缓存
            GlobalManager.sharingList.clear();

            //清除用户分享
            mInfoHub.deleteAllUserShaings(this,GlobalManager.user.getId());

//            list.add(2);
//            list.add(3);
            //清除该用户分享的评论
            //获取一个list表 记录sharingid
            mInfoHub.deleteCommentsByList(this,list);

        }
        else if(request_show_type==2){
            GlobalManager.dailyMap.clear();
            GlobalManager.stringList.clear();
            String sql="delete from daily";
            sqlite.execSQL(sql);
//            GlobalManager.dailyThingsList.clear();
        }

    }

    private void editPopWnd(PopupWindow popupWindow,View view){
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setContentView(view);

        GlobalManager.setTransparent(this,popupWindow);

        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);

    }

    public interface OnDelete{
       void deleteCurrent(int pos);
    }
}
