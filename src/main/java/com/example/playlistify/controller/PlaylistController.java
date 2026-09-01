package com.example.playlistify.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.playlistify.dto.request.GenrePlaylistRequest;
import com.example.playlistify.spotify.dto.response.PlaylistResponse;
import com.example.playlistify.service.MusicAnalysisService;
import com.example.playlistify.spotify.service.PlaylistService;

@RestController
@RequestMapping("/playlistify")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final MusicAnalysisService musicAnalysisService;

    public PlaylistController(PlaylistService playlistService ,MusicAnalysisService musicAnalysisService1) {
        this.playlistService = playlistService;
        this.musicAnalysisService=musicAnalysisService1;
    }

    @GetMapping("/playlist/create")
    public PlaylistResponse createPlaylist() throws Exception {

        return playlistService.createPlaylist(
                "Rock Playlist",
                "Created by Playlistify",
                false
        );
    }

    @PostMapping("/genre")
    public void createGenrePlaylist(@RequestBody GenrePlaylistRequest request) throws Exception {

    playlistService.createGenrePlaylist(request.getGenre());
    }

 
    
}
