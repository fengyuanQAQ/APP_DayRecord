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
import com.example.dayrecord.server.InfoHub;


public class DiscoverFragment extends Fragment{

    private RecyclerView recycler;
    private InfoHub mInfoHub;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discovery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInfoHub=new InfoHub();
        recycler = view.findViewById(R.id.dis_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mInfoHub.loadSharings(this,recycler);
    }

    public RecyclerView getRecycler() {
        return recycler;
    }

}
