package com.example.playlistify.spotify.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ImageDto {

    private String url;

    private int height;

    private int width;
}
