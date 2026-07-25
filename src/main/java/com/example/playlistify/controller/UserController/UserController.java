 package com.example.playlistify.controller.UserController;


 import com.example.playlistify.dto.response.UserProfileResponse.UserProfileResponse;
 import com.example.playlistify.dto.response.likedsongs.Item;
 import com.example.playlistify.dto.response.likedsongs.LikedSongsResponse;
 import com.example.playlistify.service.userservice.UserService;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RestController;

 import java.util.List;

 @RestController
 @RequestMapping("/playlistify")
 public class UserController {
     private final UserService userService;

      public UserController( UserService  userService){
         this.userService=userService;
      }

      @GetMapping("/profile")
     public UserProfileResponse getuser () throws Exception {
       return userService.userProfileResponse();

      }

      @GetMapping("/likedsongs")
     public List<Item> getLikedsongs() throws Exception{
          return userService.getLikedSongs();


      }



 }
