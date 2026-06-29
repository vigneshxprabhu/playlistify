package com.example.playlistify.service;

import org.springframework.stereotype.Service;

@Service
public class SpotifyService {

    private final AuthService authService;

    public SpotifyService(AuthService authService) {
        this.authService = authService;
    }

    public String testSpotifyService() {

        String token = authService.getAccessToken();

        return token;
    }

}