package com.example.playlistify.dto.response.likedsongs;
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
public class Track {

    private String id;
    private String name;
    private String uri;
    private int duration_ms;
    private int popularity;
    private boolean explicit;
    private String preview_url;
    private int track_number;
    private int disc_number;
    private String type;
    private Boolean is_local;
    private Album album;
    private List<Artist> artists;
    private ExternalUrls external_urls;
}
