package com.example.playlistify.musicbrainz.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.example.playlistify.musicbrainz.dto.response.MusicBrainzArtistSearchResponse;

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

    public static void main(String[] args) throws Exception {

    MusicBrainzClient client = new MusicBrainzClient();

    client.searchArtist("Metallica");
    }
}