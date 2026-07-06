package com.example.playlistify.service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.example.playlistify.model.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class AuthService {

       private final String CLIENT_ID="my client_id";
       private final String CLIENT_SECRET="respnse type";
       private final String REDIRECT_URI="reditract uri ";

       public TokenResponse exchangeCodeForToken(String code) {
              String body =
                      "grant_type=authorization_code" +
                              "&code=" + code +
                              "&redirect_uri=" + REDIRECT_URI +
                              "&client_id=" + CLIENT_ID +
                              "&client_secret=" + CLIENT_SECRET;


              HttpRequest request = HttpRequest.newBuilder()
                      .uri(URI.create("https://accounts.spotify.com/api/token"))
                      .header("Content-Type", "application/x-www-form-urlencoded")
                      .POST(HttpRequest.BodyPublishers.ofString(body))
                      .build();
              return null;

       }
}
