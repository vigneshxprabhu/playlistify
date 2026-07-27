package com.example.playlistify.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlaylistRequest {

    private String name;

    private String description;

    @JsonProperty("public")
    private boolean isPublic;

    private boolean collaborative;
}
