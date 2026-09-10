package com.example.playlistify.musicbrainz.service;
import com.example.playlistify.model.ArtistDetails;
import com.example.playlistify.musicbrainz.client.MusicBrainzClient;
import com.example.playlistify.musicbrainz.dto.response.MusicBrainzArtistSearchResponse;
import com.example.playlistify.service.ArtistMetadataProvider;

public class MusicBrainzProvider implements ArtistMetadataProvider{

    private final MusicBrainzClient musicBrainzClient;
    public MusicBrainzProvider(MusicBrainzClient musicBrainzClient) {
    this.musicBrainzClient = musicBrainzClient;
}

    @Override
    public ArtistDetails fetchArtistDetails (String artistName)throws Exception{
    
           MusicBrainzArtistSearchResponse searchResponse =musicBrainzClient.searchArtist(artistName);

            String mbid = searchResponse.getArtists().get(0).getId();

             System.out.println("Artist: " + artistName);
            System.out.println("MBID: " + mbid);


        return null;

    }
}