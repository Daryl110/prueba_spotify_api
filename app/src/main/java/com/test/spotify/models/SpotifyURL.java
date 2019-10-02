package com.test.spotify.models;

public class SpotifyURL {

    public static final String BASIC_URL = "https://api.spotify.com/v1";
    public static final String USER_INFO = BASIC_URL + "/me";
    public static final String USER_PLAY_LIST = USER_INFO + "/playlists";
    public static final String UPDATE_PLAY_LIST = BASIC_URL + "/playlists/";
}
