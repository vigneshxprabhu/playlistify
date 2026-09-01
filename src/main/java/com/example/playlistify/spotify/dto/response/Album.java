package com.example.playlistify.spotify.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Album {
    private List<Artist> artists;
    private String id;
    private String name;
    private String album_type;
    private String release_date;
    private int total_tracks;
    private List<Image> images;
    private ExternalUrls external_urls;
}
