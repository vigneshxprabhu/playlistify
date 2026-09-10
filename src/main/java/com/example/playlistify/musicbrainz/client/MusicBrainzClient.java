package com.example.playlistify.musicbrainz.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.example.playlistify.musicbrainz.dto.response.MusicBrainzArtistResponse;
import com.example.playlistify.musicbrainz.dto.response.MusicBrainzArtistSearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MusicBrainzClient {

    public MusicBrainzArtistSearchResponse searchArtist(String artistName) throws Exception {

        String url = "https://musicbrainz.org/ws/2/artist/?query=artist:"
                + artistName
                + "&fmt=json&limit=1";

                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Playlistify/1.0")
                    .GET()
                    .build();

                    HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

                    System.out.println(response.body());

                    return null;

    }
    public MusicBrainzArtistResponse getArtist(String mbid) throws Exception {

        String url ="https://musicbrainz.org/ws/2/artist/"
        +mbid
        +"?inc=genres&fmt=json";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("User-Agent", "Playlistify/1.0")
        .GET()
        .build();


        HttpResponse <String> response=client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        ObjectMapper objectMapper = new ObjectMapper();

        MusicBrainzArtistResponse artistResponse =
        objectMapper.readValue(
                response.body(),
                MusicBrainzArtistResponse.class
        );
        return artistResponse;

    

    }

    public static void main(String[] args) throws Exception {

    MusicBrainzClient client = new MusicBrainzClient();

    client.searchArtist("Metallica");
    MusicBrainzArtistResponse artist =
            client.getArtist("65f4f0c5-ef9e-490c-aee3-909e7ae6b2ab");

    System.out.println("Artist: " + artist.getName());
    System.out.println("Genres: " + artist.getGenres());
    }
}