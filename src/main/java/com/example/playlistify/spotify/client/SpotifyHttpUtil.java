package com.example.playlistify.spotify.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.example.playlistify.spotify.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SpotifyHttpUtil {
    private static final String BASE_URL = "https://api.spotify.com/v1";
    private final AuthService authService;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public SpotifyHttpUtil(AuthService authService) {
        this.authService = authService;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public <T> T get(String url, Class<T> responseType) throws Exception {
        return send("GET", url, null, responseType);
    }

    public <T> T post(String url, String body, Class<T> responseType) throws Exception {
        return send("POST", url, body, responseType);
    }

    public <T> T put(String url, String body, Class<T> responseType) throws Exception {
        return send("PUT", url, body, responseType);
    }

    public <T> T delete(String url, Class<T> responseType) throws Exception {
        return send("DELETE", url, null, responseType);
    }

    private HttpRequest buildRequest(String method, String url, String body) {

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + authService.getAccessToken())
                .header("Content-Type", "application/json");

        switch (method.toUpperCase()) {

            case "GET":
                builder.GET();
                break;

            case "POST":
                builder.POST(HttpRequest.BodyPublishers.ofString(body));
                break;

            case "PUT":
                builder.PUT(HttpRequest.BodyPublishers.ofString(body));
                break;

            case "DELETE":
                builder.DELETE();
                break;

            default:
                throw new IllegalArgumentException("Unsupported HTTP Method");
        }

        return builder.build();
    }

    private <T> T send(String method,
                       String url,
                       String body,
                       Class<T> responseType) throws Exception {

        HttpRequest request = buildRequest(method, url, body);


        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 401) {

            authService.refreshAccessToken();

            request = buildRequest(method, url, body);

            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        }

        if (response.statusCode() >= 400) {
            System.out.println("Status Code: " + response.statusCode());
System.out.println("Headers: " + response.headers().map());
System.out.println("Body: " + response.body());
            throw new RuntimeException(
                    "Spotify API Error: " +
                            response.statusCode() +
                            "\n" +
                            response.body()
            );
        }

        return objectMapper.readValue(response.body(), responseType);
    }
}