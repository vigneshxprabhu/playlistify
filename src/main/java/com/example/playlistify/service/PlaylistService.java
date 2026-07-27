package com.example.playlistify.service;
import com.example.playlistify.Util.SpotifyHttpUtil;
import com.example.playlistify.dto.request.CreatePlaylistRequest;
import com.example.playlistify.dto.response.UserProfileResponse.UserProfileResponse;
import com.example.playlistify.dto.response.playlistresponse.PlaylistResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PlaylistService {
    private final SpotifyHttpUtil spotifyHttpUtil;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public PlaylistService(SpotifyHttpUtil spotifyHttpUtil,
                           UserService userService) {
        this.spotifyHttpUtil = spotifyHttpUtil;
        this.userService = userService;
    }
    public PlaylistResponse createPlaylist(String name, String description, boolean isPublic) throws Exception{
        UserProfileResponse user=userService.userProfileResponse();
        String userId= user.getId();

        CreatePlaylistRequest request=new CreatePlaylistRequest();
        request.setName(name);
        request.setDescription(description);
        request.setPublic(isPublic);
        request.setCollaborative(false);
        String requestBody = objectMapper.writeValueAsString(request);

        String url = "https://api.spotify.com/v1/users/" + userId + "/playlists";


      return   spotifyHttpUtil.put( url,requestBody, PlaylistResponse.class);

    }



}
