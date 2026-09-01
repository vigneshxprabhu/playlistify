 package com.example.playlistify.controller;


import com.example.playlistify.spotify.dto.response.UserProfileResponse;
import com.example.playlistify.spotify.dto.response.Item;
import com.example.playlistify.spotify.dto.response.Track;
import com.example.playlistify.spotify.service.TrackService;
import com.example.playlistify.spotify.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

 @RestController
 @RequestMapping("/playlistify")
 public class UserController {
     private final UserService userService;
     private final TrackService trackService;

      public UserController(UserService userService, TrackService trackService){
          this.userService = userService;
          this.trackService=trackService;}

      @GetMapping("/profile")
     public UserProfileResponse getuser () throws Exception {
       return userService.userProfileResponse();

      }

      @GetMapping("/likedsongs")
     public List<Item> getLikedsongs() throws Exception{
          return trackService.getLikedSongs();


      }



 }
