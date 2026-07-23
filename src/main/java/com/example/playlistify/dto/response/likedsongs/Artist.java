package com.example.playlistify.dto.response.likedsongs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    private String id;
    private String name;
    private String type;
    private String uri;
    private ExternalUrls external_urls;
}
