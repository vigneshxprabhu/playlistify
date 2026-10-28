package com.example.playlistify.musicbrainz.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.playlistify.model.ArtistDetails;
import com.example.playlistify.musicbrainz.client.MusicBrainzClient;
import com.example.playlistify.musicbrainz.dto.response.MusicBrainzArtistResponse;
import com.example.playlistify.musicbrainz.dto.response.MusicBrainzArtistSearchResponse;
import com.example.playlistify.musicbrainz.dto.response.MusicBrainzGenre;
import com.example.playlistify.service.ArtistMetadataProvider;

@Service
public class MusicBrainzProvider implements ArtistMetadataProvider {

    // MusicBrainz allows ~1 request per second (503s otherwise).
    private static final long RATE_LIMIT_DELAY_MS = 1100;

    private final MusicBrainzClient musicBrainzClient;

    public MusicBrainzProvider(MusicBrainzClient musicBrainzClient) {
        this.musicBrainzClient = musicBrainzClient;
    }

    @Override
    public ArtistDetails fetchArtistDetails(String artistName) throws Exception {

        // Throttle only on actual provider calls (cache misses).
        Thread.sleep(RATE_LIMIT_DELAY_MS);

        MusicBrainzArtistSearchResponse searchResponse =
                musicBrainzClient.searchArtist(artistName);

        if (searchResponse.getArtists() == null || searchResponse.getArtists().isEmpty()) {
            throw new IllegalStateException(
                    "No MusicBrainz artist found for: " + artistName);
        }

        String mbid = searchResponse.getArtists().get(0).getId();

        MusicBrainzArtistResponse artistResponse =
                musicBrainzClient.getArtist(mbid);

        return toArtistDetails(artistResponse);
    }

    private ArtistDetails toArtistDetails(MusicBrainzArtistResponse artistResponse) {
        List<String> genreNames = new ArrayList<>();
        if (artistResponse.getGenres() != null) {
            for (MusicBrainzGenre genre : artistResponse.getGenres()) {
                genreNames.add(genre.getName());
            }
        }
        return new ArtistDetails(
                artistResponse.getId(),
                artistResponse.getName(),
                genreNames,
                null,
                null);
    }
}