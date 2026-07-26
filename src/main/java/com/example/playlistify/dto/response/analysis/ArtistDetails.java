package com.example.playlistify.dto.response.analysis;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDetails {

    private String id;
    private String name;
    private List<String> genres;
    private Integer popularity;
    private Integer followers;
}
