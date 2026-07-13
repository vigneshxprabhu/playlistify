package com.example.playlistify.dto.response.UserProfileResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ExplicitContentDto {

    @JsonProperty("filter_enabled")
    private boolean filterEnabled;

    @JsonProperty("filter_locked")
    private boolean filterLocked;
}
