package com.example.dayrecord.activity.home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dayrecord.R;

import java.util.List;

public class JokeAdapter extends ArrayAdapter<Joke> {
    private int resourceId;

    public JokeAdapter(Activity jokeActivty, int joke_item, List<Joke> jokeList) {
        super(jokeActivty, joke_item, jokeList);
        resourceId=joke_item;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Joke joke=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView Joke_title=view.findViewById(R.id.title);
        TextView Joke_content=view.findViewById(R.id.content);
        ImageView Joke_image=view.findViewById(R.id.joke_pic);
        Joke_title.setText(joke.getTitle());
        Joke_content.setText(joke.getContent());
        return view;
    }
}
