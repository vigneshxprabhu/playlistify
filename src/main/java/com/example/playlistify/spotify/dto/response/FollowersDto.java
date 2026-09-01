package com.example.playlistify.spotify.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class FollowersDto {

    private String href;

    private int total;
}
