package com.test.spotify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.test.spotify.models.PlayList;
import com.test.spotify.models.SpotifyConnector;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SpotifyConnector spotifyConnector;
    private String tokenUser;
    private TextView lblUserName, lblFollowers;
    private ImageView imgUser;
    private ListView lstPlayList;
    public static ArrayList<PlayList> playLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.spotifyConnector = new SpotifyConnector(this);
        this.spotifyConnector.connect();

        this.lblUserName = findViewById(R.id.lblUserName);
        this.lblFollowers = findViewById(R.id.lblFollowers);
        this.imgUser = findViewById(R.id.imgUser);
        this.lstPlayList = findViewById(R.id.lstPlayLists);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SpotifyConnector.REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

            switch (response.getType()) {

                case TOKEN:
                    this.spotifyConnector.setEditor(getSharedPreferences("SPOTIFY", 0).edit());
                    this.tokenUser = response.getAccessToken();
                    this.spotifyConnector.getEditor().putString("token", this.tokenUser);
                    Log.i("Connect successfully", this.tokenUser);
                    this.spotifyConnector.getEditor().apply();
                    try {
                        this.spotifyConnector.extractDataUser(this.tokenUser,
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


}
