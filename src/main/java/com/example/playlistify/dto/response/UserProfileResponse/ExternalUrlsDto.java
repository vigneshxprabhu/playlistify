package com.example.playlistify.dto.response.UserProfileResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ExternalUrlsDto {

    private String spotify;
}
