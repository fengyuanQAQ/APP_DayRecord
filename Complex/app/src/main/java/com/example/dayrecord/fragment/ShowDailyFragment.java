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
import com.example.dayrecord.adapter.DailyAdapter;


public class ShowDailyFragment extends Fragment {

    private View mView;
    private boolean notClick;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_daily,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView=view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final RecyclerView rvVer=mView.findViewById(R.id.show_daily_recycler);
        rvVer.setAdapter(new DailyAdapter(this));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvVer.setLayoutManager(linearLayoutManager);

//        final RecyclerView rvVer2=mView.findViewById(R.id.show_daily_recycler_lately);
//        rvVer2.setAdapter(new DailyLatelyAddAdapter(this));
//        rvVer2.setLayoutManager(new LinearLayoutManager(getContext()));
//        CardView cardView=mView.findViewById(R.id.show_daily_lately_cardview);
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(notClick){
//                    rvVer2.setVisibility(View.VISIBLE);
//                }else{
//                    rvVer2.setVisibility(View.GONE);
//                }
//                notClick=!notClick;
//            }
//        });
//
//        cardView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Toast.makeText(getContext(), "该分类不能删除", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
    }
}
