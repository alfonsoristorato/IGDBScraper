package com.alfonsoristorato.igdbscraper.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record TwitchResponse(
        @NotBlank @JsonProperty("access_token") String accessToken,
        @NotBlank @JsonProperty("expires_in") String expiresIn,
        @NotBlank @JsonProperty("token_type") String tokenType
) {
}
