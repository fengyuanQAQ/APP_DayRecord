package com.example.dayrecord.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayrecord.R;
import com.example.dayrecord.adapter.ShareAdapter;

public class ShowShareFragment extends Fragment {

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_share,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //找到recyclerView
        mRecyclerView = view.findViewById(R.id.show_share_recycler);
        LinearLayoutManager lLayoutManager=new LinearLayoutManager(getContext());
        //设置滚动方向
        lLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//垂直滚动
        mRecyclerView.setLayoutManager(lLayoutManager);
        mRecyclerView.setAdapter(new ShareAdapter(this,mRecyclerView));
    }

    public void updateAdapter(){
        mRecyclerView.setAdapter(new ShareAdapter(this,mRecyclerView));
    }
}
