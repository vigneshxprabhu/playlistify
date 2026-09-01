package com.example.playlistify.controller;

import com.example.playlistify.service.SpotifyService;
import org.springframework.web.bind.annotation.*;

import com.example.playlistify.spotify.service.AuthService;

@RestController
@RequestMapping("/playlistify")
public class  SpotifyController {

    private final SpotifyService spotifyService;
    private final AuthService authService;

    public SpotifyController(SpotifyService spotifyService, AuthService authService) {
        this.spotifyService = spotifyService;
        this.authService = authService;
    }

}

