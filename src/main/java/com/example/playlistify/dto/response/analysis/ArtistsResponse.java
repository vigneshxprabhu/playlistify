package com.example.playlistify.dto.response.analysis;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ArtistsResponse {

    private List<ArtistDetails> artists;
}