package com.test.spotify.models;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.test.spotify.R;

import java.util.ArrayList;
import java.util.List;

public class PlayList {

    private String id;
    private String image;
    private String name;
    private String tracksURL;
    private int countTracks;
    private PlayList playList;

    public PlayList(String id, String image, String name, String tracksURL, int countTracks) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.tracksURL = tracksURL;
        this.countTracks = countTracks;
        this.playList = this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTracksURL() {
        return tracksURL;
    }

    public void setTracksURL(String tracksURL) {
        this.tracksURL = tracksURL;
    }

    public int getCountTracks() {
        return countTracks;
    }

    public void setCountTracks(int countTracks) {
        this.countTracks = countTracks;
    }

    @Override
    public String toString() {
        return "{id: "+id+", name: "+name+"}";
    }
}
