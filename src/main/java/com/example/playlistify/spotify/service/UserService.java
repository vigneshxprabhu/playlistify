package com.example.playlistify.spotify.service;


import com.example.playlistify.spotify.dto.response.UserProfileResponse;
import com.example.playlistify.spotify.dto.response.Item;
import com.example.playlistify.spotify.dto.response.LikedSongsResponse;
import com.example.playlistify.spotify.client.SpotifyHttpUtil;
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


}