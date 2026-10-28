package com.example.playlistify.musicbrainz.client;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

import com.example.playlistify.musicbrainz.dto.response.MusicBrainzArtistResponse;
import com.example.playlistify.musicbrainz.dto.response.MusicBrainzArtistSearchResponse;
import com.example.playlistify.musicbrainz.dto.response.MusicBrainzArtistSearchResult;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MusicBrainzClient {

    public MusicBrainzArtistSearchResponse searchArtist(String artistName) throws Exception {

        String encodedArtistName = URLEncoder.encode(artistName, StandardCharsets.UTF_8);

        String url = "https://musicbrainz.org/ws/2/artist/?query=artist:"
                + encodedArtistName
                + "&fmt=json&limit=1";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Playlistify/1.0")
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(
                response.body(),
                MusicBrainzArtistSearchResponse.class
        );
    }

    public MusicBrainzArtistResponse getArtist(String mbid) throws Exception {

        String url = "https://musicbrainz.org/ws/2/artist/"
                + mbid
                + "?inc=genres&fmt=json";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Playlistify/1.0")
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(
                response.body(),
                MusicBrainzArtistResponse.class
        );
    }

    public static void main(String[] args) throws Exception {

        MusicBrainzClient client = new MusicBrainzClient();

        MusicBrainzArtistSearchResponse search =
                client.searchArtist("Metallica");

        MusicBrainzArtistSearchResult result =
                search.getArtists().get(0);

        String artistName = result.getName();
        String mbid = result.getId();

        System.out.println("Artist: " + artistName);
        System.out.println("MBID: " + mbid);

        MusicBrainzArtistResponse artist =
                client.getArtist(mbid);

        System.out.println("Artist: " + artist.getName());
        System.out.println("MBID: " + artist.getId());

        System.out.println("Genres:");

        artist.getGenres().forEach(
                genre -> System.out.println("- " + genre.getName())
        );
    }
}