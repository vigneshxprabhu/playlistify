package com.example.playlistify.service;
import com.example.playlistify.Util.SpotifyHttpUtil;
import com.example.playlistify.dto.request.CreatePlaylistRequest;
import com.example.playlistify.dto.response.playlistresponse.PlaylistResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {
    private final SpotifyHttpUtil spotifyHttpUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public PlaylistService(SpotifyHttpUtil spotifyHttpUtil) {
        this.spotifyHttpUtil = spotifyHttpUtil;
    }
    public PlaylistResponse createPlaylist(String name, String description, boolean isPublic) throws Exception{
        CreatePlaylistRequest request=new CreatePlaylistRequest();
        request.setName(name);
        request.setDescription(description);
        request.setPublic(isPublic);
        request.setCollaborative(false);
        String requestBody = objectMapper.writeValueAsString(request);

        String url = "https://api.spotify.com/v1/me/playlists";


        return spotifyHttpUtil.post(url, requestBody, PlaylistResponse.class);

    }



}
