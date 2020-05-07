package com.example.dayrecord.activity.home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dayrecord.R;

import java.util.List;

public class EventhistoryAdapter extends ArrayAdapter<Eventhistory> {
    private int resourceId;

    public EventhistoryAdapter(Activity eventHistoryActivity, int evethistory_item, List<Eventhistory> eventhistoryList) {
        super(eventHistoryActivity, evethistory_item, eventhistoryList);
        resourceId=evethistory_item;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Eventhistory eventhistory=getItem(position);
        View view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView EventHistory_time=view.findViewById(R.id.time);
        TextView EventHistory_title=view.findViewById(R.id.title);
        TextView EventHistory_type=view.findViewById(R.id.type);
        TextView EventHistory_desc=view.findViewById(R.id.desc);
        EventHistory_time.setText(eventhistory.getDate());
        EventHistory_desc.setText(eventhistory.getTitle());
        return view;


    }
}
