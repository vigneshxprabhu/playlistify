package com.example.playlistify.service;

import com.example.playlistify.dto.response.analysis.ArtistDetails;
import org.springframework.stereotype.Service;

/**
 * Stub provider that throws UnsupportedOperationException.
 * <p>
 * TODO: Replace this class with a real implementation (e.g. MusicBrainzProvider).
 * </p>
 */
@Service
public class NoOpArtistMetadataProvider implements ArtistMetadataProvider {

    @Override
    public ArtistDetails fetchArtistDetails(String artistId) throws Exception {
        throw new UnsupportedOperationException(
                "Artist metadata provider not implemented yet. "
                + "Implement ArtistMetadataProvider with a real backend (e.g. MusicBrainz)."
        );
    }
}