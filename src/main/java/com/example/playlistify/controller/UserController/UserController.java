 package com.example.playlistify.controller.UserController;


 import com.example.playlistify.dto.response.UserProfileResponse.UserProfileResponse;
 import com.example.playlistify.dto.response.likedsongs.Item;
 import com.example.playlistify.dto.response.likedsongs.Track;
 import com.example.playlistify.service.TrackService;
 import com.example.playlistify.service.UserService;
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
