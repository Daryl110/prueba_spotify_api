package com.test.spotify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.test.spotify.models.PlayList;
import com.test.spotify.models.SpotifyConnector;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static SpotifyConnector spotifyConnector;
    public static String tokenUser;
    private TextView lblUserName, lblFollowers;
    private ImageView imgUser;
    private ListView lstPlayList;
    public static ArrayList<PlayList> playLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spotifyConnector = new SpotifyConnector(this);
        spotifyConnector.connect();

        this.lblUserName = findViewById(R.id.lblUserName);
        this.lblFollowers = findViewById(R.id.lblFollowers);
        this.imgUser = findViewById(R.id.imgUser);
        this.lstPlayList = findViewById(R.id.lstPlayLists);

        this.onClickPlayList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SpotifyConnector.REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

            switch (response.getType()) {

                case TOKEN:
                    spotifyConnector.setEditor(getSharedPreferences("SPOTIFY", 0).edit());
                    tokenUser = response.getAccessToken();
                    spotifyConnector.getEditor().putString("token", tokenUser);
                    Log.i("Connect successfully", tokenUser);
                    spotifyConnector.getEditor().apply();
                    try {
                        spotifyConnector.extractDataUser(tokenUser,
                                this.lblUserName,
                                this.lblFollowers,
                                this.imgUser,
                                this.lstPlayList
                        );
                    }catch (Exception e){
                        Log.e("[Error]", e.getMessage(), e);
                    }
                    break;
                case ERROR:
                    Log.e("[Error]", response.getError());
                    break;
                default:
                    Log.d("Not Connected", response.getCode());
                    break;
            }
        }
    }

    private void onClickPlayList() {
        Context those = this;
        this.lstPlayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PlayList playList = playLists.get(i);

                Intent intent = new Intent(those, PlayListDetailActivity.class);

                intent.putExtra("playList", playList);

                startActivity(intent);
            }
        });
    }
}
