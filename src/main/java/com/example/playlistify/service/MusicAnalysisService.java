package com.example.playlistify.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.playlistify.Metrics.PerformanceMetrics;
import com.example.playlistify.Util.SpotifyHttpUtil;
import com.example.playlistify.dto.response.analysis.ArtistDetails;
import com.example.playlistify.dto.response.likedsongs.Artist;
import com.example.playlistify.dto.response.likedsongs.Item;
import com.example.playlistify.dto.response.likedsongs.Track;
@Service
public class MusicAnalysisService {
    private final SpotifyHttpUtil spotifyHttpUtil;
    private final PerformanceMetrics performanceMetrics;
    private final ArtistCacheService artistCacheService;

    public MusicAnalysisService(SpotifyHttpUtil spotifyHttpUtil ,ArtistCacheService artistCacheService ,PerformanceMetrics performanceMetrics) {
        this.spotifyHttpUtil = spotifyHttpUtil;
        this.performanceMetrics=performanceMetrics;
        this.artistCacheService=artistCacheService;
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


//     public ArtistsResponse fetchArtistDetailsBatch(List<String> artistIds) throws Exception {

//     String ids = String.join(",", artistIds);

//     String url = "https://api.spotify.com/v1/artists?ids=" + ids;

//     System.out.println(url);

//     System.out.println("Fetching batch of " + artistIds.size() + " artists");

//     return spotifyHttpUtil.get(url, ArtistsResponse.class);
// }

   public ArtistDetails fetchArtistDetails(String artistId) throws Exception {
   

    ArtistDetails cachedArtist = artistCacheService.get(artistId);

  if (cachedArtist != null) {

    performanceMetrics.setCacheHits(
            performanceMetrics.getCacheHits() + 1);

    System.out.println("Cache Hit: " + artistId);

    return cachedArtist;
    }
    performanceMetrics.setCacheMisses(
        performanceMetrics.getCacheMisses() + 1);

    performanceMetrics.setSpotifyApiCalls(
        performanceMetrics.getSpotifyApiCalls() + 1);


    String url = "https://api.spotify.com/v1/artists/" + artistId;
    System.out.println("Fetching from Spotify: " + artistId);

    ArtistDetails artist =spotifyHttpUtil.get(url, ArtistDetails.class);
    System.out.println("Successfully fetched: " + artistId);

    artistCacheService.put(artistId, artist);

    System.out.println("Cached: " + artist.getName());

    return artist;
}




        public Map<String, ArtistDetails> fetchArtistDetails(Set<String> artistIds) throws Exception {

            Map<String, ArtistDetails> artistDetailsMap = new HashMap<>();

            performanceMetrics.reset();
            performanceMetrics.setArtistsRequested(artistIds.size());
            performanceMetrics.startTimer();

            try {

                for (String artistId : artistIds) {

                    ArtistDetails artistDetails = fetchArtistDetails(artistId);
                    artistDetailsMap.put(artistId, artistDetails);
                }

            } finally {

                performanceMetrics.stopTimer();

                artistCacheService.save();

                performanceMetrics.printReport();
            }

            return artistDetailsMap;
        }

    public Map<String, Set<Track>> buildGenreMap(List<Item> likedSongs, Map<String, ArtistDetails> artistDetailsById) {

        Map<String, Set<Track>> genreMap = new HashMap<>();

        for (Item item : likedSongs) {

            Track track = item.getTrack();

            List<Artist> artists = track.getArtists();

            for (Artist artist : artists) {

                ArtistDetails artistDetails =
                        artistDetailsById.get(artist.getId());

                List<String> genres = artistDetails.getGenres();

                for (String genre : genres) {

                    if (!genreMap.containsKey(genre)) {
                        genreMap.put(genre, new HashSet<>());
                    }

                    genreMap.get(genre).add(track);
                }
            }
        }

        return genreMap;
    }
    }


