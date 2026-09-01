package com.example.playlistify.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.playlistify.metrics.PerformanceMetrics;
import com.example.playlistify.model.ArtistDetails;
import com.example.playlistify.spotify.dto.response.Artist;
import com.example.playlistify.spotify.dto.response.Item;
import com.example.playlistify.spotify.dto.response.Track;

@Service
public class MusicAnalysisService {

    private final ArtistMetadataProvider artistMetadataProvider;
    private final PerformanceMetrics performanceMetrics;
    private final ArtistCacheService artistCacheService;

    public MusicAnalysisService(
            ArtistMetadataProvider artistMetadataProvider,
            ArtistCacheService artistCacheService,
            PerformanceMetrics performanceMetrics) {
        this.artistMetadataProvider = artistMetadataProvider;
        this.performanceMetrics = performanceMetrics;
        this.artistCacheService = artistCacheService;
    }

    public Set<String> getUniqueArtistIds(List<Item> likedSongs) {
        Set<String> artistIds = new HashSet<>();
        for (Item item : likedSongs) {
            Track track = item.getTrack();
            List<Artist> artists = track.getArtists();
            for (Artist artist : artists) {
                String artistId = artist.getId();
                artistIds.add(artistId);
            }
        }
        return artistIds;
    }

    public ArtistDetails fetchArtistDetails(String artistName) throws Exception {
        ArtistDetails cachedArtist = artistCacheService.get(artistName);
        if (cachedArtist != null) {
            performanceMetrics.setCacheHits(
                    performanceMetrics.getCacheHits() + 1);
            System.out.println("Cache Hit: " + artistName);
            return cachedArtist;
        }
        performanceMetrics.setCacheMisses(
                performanceMetrics.getCacheMisses() + 1);
        performanceMetrics.setApiCalls(
                performanceMetrics.getApiCalls() + 1);

        System.out.println("Fetching artist metadata from provider: " + artistName);

        ArtistDetails artist = artistMetadataProvider.fetchArtistDetails(artistName);
        System.out.println(artist.getName());
        System.out.println(artist.getGenres());
        System.out.println("Successfully fetched: " + artistName);

        artistCacheService.put(artistName, artist);
        System.out.println("Cached: " + artist.getName());

        return artist;
    }

    public Map<String, ArtistDetails> fetchArtistDetails(Set<String> artistNames) throws Exception {
        Map<String, ArtistDetails> artistDetailsMap = new HashMap<>();
        performanceMetrics.reset();
        performanceMetrics.setArtistsRequested(artistNames.size());
        performanceMetrics.startTimer();

        try {
            for (String artistName : artistNames) {
                ArtistDetails artistDetails = fetchArtistDetails(artistName);
                artistDetailsMap.put(artistName, artistDetails);
            }
        } finally {
            performanceMetrics.stopTimer();
            artistCacheService.save();
            performanceMetrics.printReport();
        }
        return artistDetailsMap;
    }

    public Map<String, Set<Track>> buildGenreMap(
            List<Item> likedSongs,
            Map<String, ArtistDetails> artistDetailsById) {
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