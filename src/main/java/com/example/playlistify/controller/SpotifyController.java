package com.example.playlistify.controller;

import com.example.playlistify.model.Track;
import com.example.playlistify.service.SpotifyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlistify")
public class SpotifyController {

    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/getlikedsongs")
    public List<Track> getLikedSongs() {
        return spotifyService.getLikedSongs();
    }

    @GetMapping("/ooo")
    public String test() {
        return "Working 🚀";
    }
}