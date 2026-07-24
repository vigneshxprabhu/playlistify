package com.example.playlistify.service.userservice;



import com.example.playlistify.dto.response.UserProfileResponse.UserProfileResponse;
import com.example.playlistify.dto.response.likedsongs.LikedSongsResponse;
import com.example.playlistify.service.AuthService;
import com.example.playlistify.service.httpservice.SpotifyHttpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class UserService {

    private final SpotifyHttpService spotifyHttpService;

    public UserService(SpotifyHttpService spotifyHttpService) {
        this.spotifyHttpService = spotifyHttpService;
    }
    public UserProfileResponse userProfileResponse() throws Exception{

        return spotifyHttpService.get(
                "https://api.spotify.com/v1/me",
                UserProfileResponse.class
        );

    }

    public LikedSongsResponse likedSongsResponse() throws Exception{

        return spotifyHttpService.get(
                "https://api.spotify.com/v1/me/tracks",
                LikedSongsResponse.class
        );
    }
}




