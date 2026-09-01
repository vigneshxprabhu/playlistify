package com.example.playlistify.service;

import com.example.playlistify.model.ArtistDetails;

public interface ArtistMetadataProvider {

    ArtistDetails fetchArtistDetails(String artistName) throws Exception;

}