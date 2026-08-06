package com.example.playlistify.service;

import com.example.playlistify.dto.response.analysis.ArtistDetails;

public interface ArtistMetadataProvider {

    ArtistDetails fetchArtistDetails(String artistName) throws Exception;
}