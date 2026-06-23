package com.example.playlistify.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    @GetMapping("/getlikedsongs")
    public String getlikedsongs(){
        return "working too";
    }

    @GetMapping("/ooo")
    public String test() {
        return "Working 🚀";
    }
}