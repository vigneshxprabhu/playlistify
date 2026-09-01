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
public class LikedSongsResponse {

    private String href;
    private int limit;
    private String next;
    private int offset;
    private String previous;
    private int total;
    private List<Item> items;
}
