package com.example.playlistify.controller.playlistcontroller;

import com.example.playlistify.dto.response.playlistresponse.PlaylistResponse;
import com.example.playlistify.service.PlaylistService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/playlistify")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/playlist/create")
    public PlaylistResponse createPlaylist() throws Exception {

        return playlistService.createPlaylist(
                "Rock Playlist",
                "Created by Playlistify",
                false
        );
    }
}
