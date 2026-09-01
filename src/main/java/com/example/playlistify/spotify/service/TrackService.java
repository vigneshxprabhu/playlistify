package com.example.playlistify.spotify.service;

import com.example.playlistify.spotify.client.SpotifyHttpUtil;
import com.example.playlistify.spotify.dto.response.Item;
import com.example.playlistify.spotify.dto.response.LikedSongsResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class TrackService {

    private final SpotifyHttpUtil spotifyHttpService;

    public TrackService(SpotifyHttpUtil spotifyHttpService) {
        this.spotifyHttpService = spotifyHttpService;
    }

    public List<Item> getLikedSongs() throws Exception {

        List<Item> likedSongs = new ArrayList<>();

        String url = "https://api.spotify.com/v1/me/tracks?limit=50";

        while (url != null) {

            LikedSongsResponse response = spotifyHttpService.get(url, LikedSongsResponse.class);

            likedSongs.addAll(response.getItems());

            url = response.getNext();
        }


        System.out.println("Total songs fetched : " + likedSongs.size());


        return likedSongs;
    }
}
