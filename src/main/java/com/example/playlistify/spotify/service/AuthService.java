package com.example.playlistify.spotify.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.playlistify.spotify.dto.response.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

@Service
public class AuthService {

       @Value("${spotify.client-id}")
       private String clientId;

       @Value("${spotify.client-secret}")
       private String clientSecret;

       @Value("${spotify.redirect-uri}")
       private String redirectUri;

       @Getter
       private String accessToken;
       @Getter
       private String refreshToken;


       
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

              HttpClient client = HttpClient.newHttpClient();

              HttpResponse<String> response =
                      client.send(request, HttpResponse.BodyHandlers.ofString());

              ObjectMapper mapper = new ObjectMapper();

              TokenResponse token = mapper.readValue(response.body(), TokenResponse.class);

              this.accessToken = token.getAccessToken();
              this.refreshToken=token.getRefreshToken();
              return token;



       }
       public TokenResponse refreshAccessToken() throws Exception {

              String credentials = clientId + ":" + clientSecret;

              String basicAuth = Base64.getEncoder()
                      .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

              String body =
                      "grant_type=refresh_token" +
                              "&refresh_token=" + refreshToken;

              HttpRequest request = HttpRequest.newBuilder()
                      .uri(URI.create("https://accounts.spotify.com/api/token"))
                      .header("Authorization", "Basic " + basicAuth)
                      .header("Content-Type", "application/x-www-form-urlencoded")
                      .POST(HttpRequest.BodyPublishers.ofString(body))
                      .build();

              HttpClient client = HttpClient.newHttpClient();

              HttpResponse<String> response =
                      client.send(request, HttpResponse.BodyHandlers.ofString());


              ObjectMapper mapper = new ObjectMapper();

              TokenResponse token =
                      mapper.readValue(response.body(), TokenResponse.class);

              this.accessToken = token.getAccessToken();

            
              if (token.getRefreshToken() != null) {
                     this.refreshToken = token.getRefreshToken();
              }

              return token;
       }


}