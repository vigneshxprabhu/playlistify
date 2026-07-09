package com.example.playlistify.controller;
import com.example.playlistify.model.TokenResponse;
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


    @GetMapping("/login")
    public ResponseEntity<Void> login() {

        String clientId = "77cbc600bcff4fc7a57269fb6912b909";

        String redirectUri = "http://127.0.0.1:8080/playlistify/callback";
        String scope = "user-library-read";
        String state = "12345";

        String url =
                "https://accounts.spotify.com/authorize" +
                        "?client_id=" + clientId +
                        "&response_type=code" +
                        "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                        "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8) +
                        "&state=" + state;

        System.out.println(url);
        return ResponseEntity
                .status(302)
                .header(HttpHeaders.LOCATION, url)
                .build();
    }




@GetMapping("/callback")
public String callback(@RequestParam String code) throws Exception {


    TokenResponse token =
            authService.exchangeCodeForToken(code);
    System.out.println("Access Token:");
    System.out.println(token.getAccessToken());

    System.out.println("Refresh Token:");
    System.out.println(token.getRefreshToken());

    return "Authorization Code Received!";
}
}