package com.example.playlistify.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.playlistify.Util.SpotifyHttpUtil;
import com.example.playlistify.dto.request.AddTracksRequest;
import com.example.playlistify.dto.request.CreatePlaylistRequest;
import com.example.playlistify.dto.response.analysis.ArtistDetails;
import com.example.playlistify.dto.response.likedsongs.Item;
import com.example.playlistify.dto.response.likedsongs.Track;
import com.example.playlistify.dto.response.playlistresponse.PlaylistResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PlaylistService {
    private final SpotifyHttpUtil spotifyHttpUtil;
    private final TrackService trackService;
    private final MusicAnalysisService musicAnalysisService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PlaylistService(
            SpotifyHttpUtil spotifyHttpUtil,
            TrackService trackService,
            MusicAnalysisService musicAnalysisService) {

        this.spotifyHttpUtil = spotifyHttpUtil;
        this.trackService = trackService;
        this.musicAnalysisService = musicAnalysisService;
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

   public void addTracksToPlaylist(String playlistId, List<String> trackUris) throws Exception {

    AddTracksRequest request = new AddTracksRequest();
    request.setUris(trackUris);

    String requestBody = objectMapper.writeValueAsString(request);

    String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";

    spotifyHttpUtil.post(url, requestBody, Object.class);
}

   public void createGenrePlaylist(String genre) throws Exception {
        long startTime = System.currentTimeMillis();
        List<Item> likedSongs = trackService.getLikedSongs();

        System.out.println("Liked Songs: " + likedSongs.size());

        Set<String> artistIds = musicAnalysisService.getUniqueArtistIds(likedSongs);

        System.out.println("Unique Artists: " + artistIds.size());
        Map<String, ArtistDetails> artistDetails =
        musicAnalysisService.fetchArtistDetails(artistIds);
        long endTime = System.currentTimeMillis();

        System.out.println("--------------------------------");
        System.out.println("Total Execution Time: "
        + (endTime - startTime) + " ms");
        System.out.println("--------------------------------");

        System.out.println("Artist details fetched: " + artistDetails.size());

    }



}
