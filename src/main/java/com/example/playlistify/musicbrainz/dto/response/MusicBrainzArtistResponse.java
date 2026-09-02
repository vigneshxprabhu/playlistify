package com.example.playlistify.musicbrainz.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicBrainzArtistResponse {

    private String id;
    private String name;
    private List<MusicBrainzGenre> genres;
}
