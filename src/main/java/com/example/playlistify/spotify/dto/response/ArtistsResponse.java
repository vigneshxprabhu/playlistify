package com.example.playlistify.spotify.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import com.example.playlistify.model.ArtistDetails;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ArtistsResponse {

    private List<ArtistDetails> artists;
}