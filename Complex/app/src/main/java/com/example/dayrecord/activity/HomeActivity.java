package com.example.dayrecord.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dayrecord.R;
import com.example.dayrecord.fragment.DiscoverFragment;
import com.example.dayrecord.fragment.HomeFragement;
import com.example.dayrecord.fragment.MineFragment;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mIvHome,mIvMine,mIvDis,mIvAdd;
    private  Integer[] imageBefore={R.mipmap.ic_home_home_no,R.mipmap.ic_home_dis_no,
    R.mipmap.ic_home_mine_no};
    private  Integer[] imageAfter={R.mipmap.ic_home_home_yes,R.mipmap.ic_home_dis_yes,
            R.mipmap.ic_home_mine_yes};

    //主页的fragment
    private HomeFragement homeFragement;

    //mine的fragment
    private MineFragment mineFragment;

    //发现的fragment
    private DiscoverFragment discoverFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //资源的一些初始化
        mIvHome=findViewById(R.id.home_home);
        mIvDis=findViewById(R.id.home_discover);
        mIvMine =findViewById(R.id.home_mine);
        mIvAdd=findViewById(R.id.home_add);


        //设置事件监听
        mIvHome.setOnClickListener(this);
        mIvDis.setOnClickListener(this);
        mIvMine.setOnClickListener(this);
        mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        //-----主页面管理--------
        mIvHome.setImageResource(imageAfter[0]);
        //首先加载主页面
        homeFragement=new HomeFragement();
        getSupportFragmentManager().beginTransaction().add(R.id.home_frameLayout,homeFragement).commitAllowingStateLoss();

    }

    @Override
    public void onClick(View v) {
        //点击的时候先全部设置为 没有填充的图片
        mIvHome.setImageResource(imageBefore[0]);
        mIvDis.setImageResource(imageBefore[1]);
        mIvMine.setImageResource(imageBefore[2]);
        switch (v.getId()){
            //被点击的图片 设置填充
            case R.id.home_home:
                mIvHome.setImageResource(imageAfter[0]);
                playCategoriesAni(mIvHome);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_frameLayout,homeFragement).commitAllowingStateLoss();
                break;
            case R.id.home_discover:
                mIvDis.setImageResource(imageAfter[1]);
                playCategoriesAni(mIvDis);
                if(discoverFragment==null) {
                    discoverFragment=new DiscoverFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.home_frameLayout,discoverFragment).commitAllowingStateLoss();
                break;
            case R.id.home_mine:
                mIvMine.setImageResource(imageAfter[2]);
                playCategoriesAni(mIvMine);
                if(mineFragment==null) {
                    mineFragment=new MineFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.home_frameLayout,mineFragment).commitAllowingStateLoss();
                break;
        }
    }

    private void playCategoriesAni(View view){
        PropertyValuesHolder holde1=PropertyValuesHolder.ofFloat("scaleX",1,1.5f,1);
        PropertyValuesHolder holde2=PropertyValuesHolder.ofFloat("scaleY",1,1.5f,1);
        ObjectAnimator objectAnim=ObjectAnimator.ofPropertyValuesHolder(view,holde1,holde2);
        objectAnim.setDuration(480).start();
    }

}
