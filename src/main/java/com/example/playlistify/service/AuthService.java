package com.example.playlistify.service;

import com.example.playlistify.dto.response.tokenresponse.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class AuthService {

       @Value("${spotify.client-id}")
       private String clientId;

       @Value("${spotify.client-secret}")
       private String clientSecret;

       @Value("${spotify.redirect-uri}")
       private String redirectUri;
       public TokenResponse exchangeCodeForToken(String code) throws Exception {

              String credentials = clientId + ":" + clientSecret;

              String basicAuth = Base64.getEncoder()
                      .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

              String body =
                      "grant_type=authorization_code" +
                              "&code=" + code +
                              "&redirect_uri=" + redirectUri;

              HttpRequest request = HttpRequest.newBuilder()
                      .uri(URI.create("https://accounts.spotify.com/api/token"))
                      .header("Authorization", "Basic " + basicAuth)
                      .header("Content-Type", "application/x-www-form-urlencoded")
                      .POST(HttpRequest.BodyPublishers.ofString(body))
                      .build();
              System.out.println(request.headers().map());
              System.out.println(body);
              HttpClient client = HttpClient.newHttpClient();

              HttpResponse<String> response =
                      client.send(request, HttpResponse.BodyHandlers.ofString());

              System.out.println("Status: " + response.statusCode());
              System.out.println(response.body());

              ObjectMapper mapper = new ObjectMapper();
              return mapper.readValue(response.body(), TokenResponse.class);
       }
}