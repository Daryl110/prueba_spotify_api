package com.test.spotify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.spotify.models.PlayList;

public class PlayListDetailActivity extends AppCompatActivity {

    private TextView lblTracks;
    private EditText txtNamePlaylist;
    private ListView lstTracks;
    private ImageView imgPlayList;
    private PlayList playList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list_detail);

        Bundle bundle = this.getIntent().getExtras();

        this.lblTracks = findViewById(R.id.lblTracks_Detail);
        this.txtNamePlaylist = findViewById(R.id.txtNamePlayList);
        this.lstTracks = findViewById(R.id.lstTracks);
        this.imgPlayList = findViewById(R.id.imgPlayList_Detail);

        this.playList = (PlayList) bundle.getSerializable("playList");

        this.lblTracks.setText(this.playList.getCountTracks());
        this.txtNamePlaylist.setText(this.playList.getName());
        Picasso.with(this).load(this.playList.getImage()).into(this.imgPlayList);
    }

    public void updatePlayList(View view) {
        this.playList.setName(this.txtNamePlaylist.getText().toString());
        MainActivity.spotifyConnector.updatePlayList(this, this.playList);
    }
}
