package com.alfonsoristorato.igdbscraper.service.igdb;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "downstreams.igdb")
@Validated
public record IGDBConfigProperties(
        @NotBlank String baseUrl
) {
}
