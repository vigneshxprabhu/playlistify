package com.example.playlistify.dto.response.UserProfileResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserProfileResponse {

    @JsonProperty("account_id")
    private String accountId;

    private String country;

    @JsonProperty("display_name")
    private String displayName;

    private String email;

    @JsonProperty("explicit_content")
    private ExplicitContentDto explicitContent;

    @JsonProperty("external_urls")
    private ExternalUrlsDto externalUrls;

    private FollowersDto followers;

    private String href;

    private String id;

    private List<ImageDto> images;

    private String product;

    private String type;

    private String uri;
}
