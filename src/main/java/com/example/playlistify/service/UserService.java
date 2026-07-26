package com.example.playlistify.service;



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


}




