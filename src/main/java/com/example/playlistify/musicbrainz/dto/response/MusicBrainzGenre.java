package com.example.playlistify.musicbrainz.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicBrainzGenre {

    private String id;
    private String name;
    private Integer count;
}
