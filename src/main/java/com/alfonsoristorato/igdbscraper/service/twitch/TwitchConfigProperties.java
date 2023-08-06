package com.alfonsoristorato.igdbscraper.service.twitch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;
@ConfigurationProperties(prefix = "downstreams.twitch")
@Validated
public record TwitchConfigProperties(
        @NotBlank String baseUrl,
        @NotBlank String clientId,
        @NotEmpty List<String> clientSecrets,
        @NotBlank String grantType
) {
}
