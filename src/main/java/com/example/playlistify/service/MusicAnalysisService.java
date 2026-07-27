package com.example.playlistify.service;

import com.example.playlistify.Util.SpotifyHttpUtil;
import com.example.playlistify.dto.response.analysis.ArtistDetails;
import com.example.playlistify.dto.response.likedsongs.Artist;
import com.example.playlistify.dto.response.likedsongs.Item;
import com.example.playlistify.dto.response.likedsongs.Track;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class MusicAnalysisService {
    private final SpotifyHttpUtil spotifyHttpUtil;

    public MusicAnalysisService(SpotifyHttpUtil spotifyHttpUtil) {
        this.spotifyHttpUtil = spotifyHttpUtil;
    }

    public Set<String> getUniqueArtistIds(List<Item> likedSongs){

    Set<String> artistIds= new HashSet<>();

    for (Item item:likedSongs){
        Track track=item.getTrack();
        List<Artist> artists = track.getArtists();
        for(Artist artist  : artists){
            String artistId=artist.getId();
            artistIds.add(artistId);
        }

    }
    return artistIds;
    }

    public ArtistDetails fetchArtistDetails(String artistId) throws Exception{
        String url = "https://api.spotify.com/v1/artists/" + artistId;

        return spotifyHttpUtil.get(url, ArtistDetails.class);

    }
    public Map<String, ArtistDetails> fetchArtistDetails(Set<String> artistIds) throws Exception{
        Map<String, ArtistDetails> artistDetailsMap = new HashMap<>();
        for(String artistid:artistIds){
            ArtistDetails artistDetails=fetchArtistDetails(artistid);
            artistDetailsMap.put(artistid, artistDetails);
        }
        return artistDetailsMap;

    }

}
