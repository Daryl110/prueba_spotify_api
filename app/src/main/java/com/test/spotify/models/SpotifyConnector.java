package com.test.spotify.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.squareup.picasso.Picasso;
import com.test.spotify.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SpotifyConnector {

    private SharedPreferences.Editor editor;
    private SharedPreferences userPreferences;
    private JsonObjectRequest joRequest;
    private AppCompatActivity context;

    private static final String CLIENT_ID = "4f7eda6cbb9a4475b1ab551d41f4430f";
    private static final String REDIRECT_URI = "com.test.spotify://callback";
    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private" +
            ",playlist-modify-private,user-read-private,playlist-read-private,user-library-modify," +
            "user-follow-modify,user-read-currently-playing,user-read-recently-played,user-read-email," +
            "user-library-read,user-top-read,user-follow-read,user-read-playback-state,user-modify-playback-state";
    public static final int REQUEST_CODE = 1337;

    public SpotifyConnector(AppCompatActivity context) {
        this.editor = null;
        this.userPreferences = null;
        this.context = context;
    }

    public void connect() { // function of connection with spotify account
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(
                CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI
        );

        builder.setScopes(new String[]{SCOPES});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity((Activity) this.context, REQUEST_CODE, request);
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    public SharedPreferences getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(SharedPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }

    public void extractDataUser(String tokenUser, TextView lblUserName, TextView lblFollowers, ImageView imgUser,
                                ListView playListsView) throws AuthFailureError {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this.context);

        this.joRequest = new JsonObjectRequest(Request.Method.GET, SpotifyURL.USER_INFO,
                null,
                (Response.Listener<org.json.JSONObject>) response -> {
                    Log.d("Response", response.toString());
                    this.extractUserInformation(response, lblUserName, lblFollowers, imgUser);

                    mRequestQueue.add(this.selectPlayList(tokenUser, playListsView));
                },
                (Response.ErrorListener) System.out::println) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", "Bearer " + tokenUser);
                params.put("Content-Type", "application/json");

                return params;
            }
        };

        mRequestQueue.add(this.joRequest);
    }

    private JsonObjectRequest selectPlayList(String tokenUser, ListView listPlayList) {
        return new JsonObjectRequest(Request.Method.GET, SpotifyURL.USER_PLAY_LIST,
                null,
                (Response.Listener<org.json.JSONObject>) response -> {
                    try {
                        MainActivity.playLists = new ArrayList<>();

                        JSONArray array = response.getJSONArray("items");

                        for (int i = 0; i < array.length(); i++){
                            JSONObject joObject = array.getJSONObject(i);
                            MainActivity.playLists.add(new PlayList(
                               joObject.getString("id"),
                               joObject.getJSONArray("images").getJSONObject(0).getString("url"),
                               joObject.getString("name"),
                               joObject.getJSONObject("tracks").getString("href"),
                               joObject.getJSONObject("tracks").getString("total")
                            ));
                        }

                        Log.d("[PlayLists]", Arrays.toString(MainActivity.playLists.toArray()));

                        listPlayList.setAdapter(new PlayListAdapter(this.context, MainActivity.playLists));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                (Response.ErrorListener) System.out::println) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", "Bearer " + tokenUser);
                params.put("Content-Type", "application/json");

                return params;
            }
        };
    }

    public void updatePlayList(Context context, PlayList playList) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);

        JSONObject body = new JSONObject();

        try {
            body.put("name", playList.getName());
            body.put("description", "ll");
            body.put("public", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRequestQueue.add(new JsonObjectRequest(Request.Method.PUT, SpotifyURL.UPDATE_PLAY_LIST + playList.getId(),
                body,
                (Response.Listener<org.json.JSONObject>) response -> {
                    Toast.makeText(context,
                            "Update PlayList successfully",
                            Toast.LENGTH_LONG).show();
                },
                (Response.ErrorListener) error -> Toast.makeText(context,
                        "Update PlayList successfully",
                        Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", "Bearer " + MainActivity.tokenUser);
                params.put("Content-Type", "application/json");

                return params;
            }
        });
    }

    private void extractUserInformation(JSONObject response, TextView lblUserName, TextView lblFollowers, ImageView imgUser) {
        try {
            lblUserName.setText(response.getString("display_name"));
            lblFollowers.setText(response.getJSONObject("followers").getString("total"));
            String img = response.getJSONArray("images").getJSONObject(0).getString("url");
            Picasso.with(this.context).load(img).into(imgUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
