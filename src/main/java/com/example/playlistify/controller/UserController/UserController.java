 package com.example.playlistify.controller.UserController;


 import com.example.playlistify.dto.response.UserProfileResponse.UserProfileResponse;
 import com.example.playlistify.service.UserService;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RestController;

 @RestController
 @RequestMapping("/playlistify")
 public class UserController {
     private final UserService userService;

      public UserController( UserService  userService){
         this.userService=userService;
      }

      @GetMapping("/profile")
     public UserProfileResponse user () throws Exception {
       return userService.userProfileResponse();

      }





 }
