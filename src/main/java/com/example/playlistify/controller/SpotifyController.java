package com.example.playlistify.controller;
import com.example.playlistify.dto.response.tokenresponse.TokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import com.example.playlistify.service.SpotifyService;
import org.springframework.web.bind.annotation.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.example.playlistify.service.AuthService;

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

