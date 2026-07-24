package com.example.playlistify.service.httpservice;

import com.example.playlistify.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpRequest;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;


@Service
public class SpotifyHttpService {

    private final AuthService authService;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public SpotifyHttpService(AuthService authService) {
        this.authService = authService;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

//GET
    public <T> T get(String url, Class<T> responseType) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + authService.getAccessToken())
                .GET()
                .build();

        return send(request, responseType);
    }

    //post
    public <T> T post(String url, String body, Class<T> responseType) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + authService.getAccessToken())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return send(request, responseType);
    }

    //put
    public <T> T put(String url,
                     String body,
                     Class<T> responseType) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + authService.getAccessToken())
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return send(request, responseType);
    }
    //delete
    public void delete(String url) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + authService.getAccessToken())
                .DELETE()
                .build();

        send(request, Void.class);
    }
    private <T> T send(HttpRequest request,
                       Class<T> responseType) throws Exception {

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), responseType);
    }

}
