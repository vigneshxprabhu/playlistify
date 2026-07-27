package com.example.playlistify.dto.response.playlistresponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistResponse {

    private String id;

    private String name;

    private String href;

    private String uri;
}