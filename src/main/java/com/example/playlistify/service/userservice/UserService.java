package com.example.playlistify.service.userservice;



import com.example.playlistify.dto.response.UserProfileResponse.UserProfileResponse;
import com.example.playlistify.dto.response.likedsongs.Item;
import com.example.playlistify.dto.response.likedsongs.LikedSongsResponse;
import com.example.playlistify.Util.SpotifyHttpUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final SpotifyHttpUtil spotifyHttpService;

    public UserService(SpotifyHttpUtil spotifyHttpService) {
        this.spotifyHttpService = spotifyHttpService;
    }
    public UserProfileResponse userProfileResponse() throws Exception{

        return spotifyHttpService.get(
                "https://api.spotify.com/v1/me",
                UserProfileResponse.class
        );

    }

    public List<Item> getLikedSongs() throws Exception {

        List<Item> songs = new ArrayList<>();

        String url = "https://api.spotify.com/v1/me/tracks?limit=50";

        while (url != null) {

            LikedSongsResponse response = spotifyHttpService.get(url, LikedSongsResponse.class);

            songs.addAll(response.getItems());

            url = response.getNext();
        }

        System.out.println("Total songs fetched: " + songs.size());

        return songs;
    }
}




