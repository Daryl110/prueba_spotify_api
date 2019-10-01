package com.test.spotify.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.test.spotify.R;

import java.util.ArrayList;

public class PlayListAdapter extends ArrayAdapter<PlayList> {

    AppCompatActivity appCompatActivity;
    ArrayList<PlayList> playLists;

    PlayListAdapter(AppCompatActivity context, ArrayList<PlayList> playLists) {
        super(context, R.layout.playlist_item, playLists);
        this.playLists = playLists;
        appCompatActivity = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        View item = inflater.inflate(R.layout.playlist_item, null);

        TextView lblNamePlayList = item.findViewById(R.id.lblPlayListName);
        lblNamePlayList.setText(playLists.get(position).getName());

        ImageView imgPlayList = item.findViewById(R.id.imgPlayList);
        Picasso.with(item.getContext()).load(playLists.get(position).getImage()).into(imgPlayList);

        TextView lblNumTracks = item.findViewById(R.id.lblPlayListName);
        lblNumTracks.setText(playLists.get(position).getCountTracks());

        return(item);
    }
}
