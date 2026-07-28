package com.example.playlistify.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.playlistify.Util.SpotifyHttpUtil;
import com.example.playlistify.dto.request.AddTracksRequest;
import com.example.playlistify.dto.request.CreatePlaylistRequest;
import com.example.playlistify.dto.response.likedsongs.Track;
import com.example.playlistify.dto.response.playlistresponse.PlaylistResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    private List<String> extractTrackUris(Set<Track> tracks){
        List<String> trackUris = new ArrayList<>();
        for(Track track:tracks){
          trackUris.add(track.getUri());
        }
            return trackUris;
        
    }

    public void addTracksToPlaylist(String playlistId, List<String> trackUris)throws Exception{

        AddTracksRequest request=new AddTracksRequest();

        

    }



}
