package com.example.playlistify.service.userservice;



import com.example.playlistify.dto.response.UserProfileResponse.UserProfileResponse;
import com.example.playlistify.dto.response.likedsongs.LikedSongsResponse;
import com.example.playlistify.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class UserService {

    private final AuthService authService;

    public UserService(AuthService authService) {
        this.authService = authService;
    }
    public UserProfileResponse userProfileResponse() throws Exception{

        String accessToken= authService.getAccessToken();
        HttpRequest request=  HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/me"))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpClient client=HttpClient.newHttpClient();

        HttpResponse<String> response= client.send(request , HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper=new ObjectMapper();

        UserProfileResponse user=mapper.readValue(response.body(), UserProfileResponse.class);
        return user;

    }

    public LikedSongsResponse likedSongsResponse() throws Exception{

        String accessToken= authService.getAccessToken();
        HttpRequest request=  HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/me/tracks"))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpClient client=HttpClient.newHttpClient();

        HttpResponse<String> response= client.send(request , HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper=new ObjectMapper();

        LikedSongsResponse likedsongs=mapper.readValue(response.body(), LikedSongsResponse.class);
        return likedsongs;

    }
}




