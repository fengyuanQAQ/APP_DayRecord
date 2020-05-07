package com.example.dayrecord.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.dayrecord.R;
import com.example.dayrecord.activity.home.Fragmen_artitcle;
import com.example.dayrecord.activity.home.Fragment_dailypicture;
import com.example.dayrecord.activity.home.Fragment_eventhistory;
import com.example.dayrecord.activity.home.Fragment_joke;
import com.example.dayrecord.activity.home.Fragment_mustic;
import com.example.dayrecord.activity.home.Fragment_oneword;
import com.example.dayrecord.activity.home.Fragment_poetry;
import com.example.dayrecord.activity.home.Fragment_poisonsoup;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class HomeFragement extends Fragment{

    private ViewPager m_vp;
    //页面列表
    private ArrayList<Fragment> fragmentList;
    //标题列表
    ArrayList<String>  titleList = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.activity_homemain,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_vp = view.findViewById(R.id.page);
        TabLayout tabLayout =view.findViewById(R.id.tablayout);
        //设置模式
        //tabLayout.setTabMode(TabLayout.MODE_FIXED);固定
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);滚动
        //设置关联
        tabLayout.setupWithViewPager(m_vp);

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new Fragment_poetry());
        fragmentList.add(new Fragment_mustic());
        fragmentList.add(new Fragment_eventhistory());
        fragmentList.add(new Fragment_joke());
        fragmentList.add(new Fragmen_artitcle());
        fragmentList.add(new Fragment_oneword());
        fragmentList.add(new Fragment_poisonsoup());
        fragmentList.add(new Fragment_dailypicture());


        titleList.add("梦回唐宋");
        titleList.add("随机音乐");
        titleList.add("那年今日");
        titleList.add("开心一刻");
        titleList.add("随机一文 ");
        titleList.add("是他说的");
        titleList.add("大郎喝药");
        titleList.add("今日美图 ");
        for(int i=0;i<titleList.size();i++){
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(i)));
        }

        m_vp.setAdapter(new MyViewPagerAdapter(getActivity().getSupportFragmentManager()));
    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub
            return titleList.get(position);
        }
    }

}
