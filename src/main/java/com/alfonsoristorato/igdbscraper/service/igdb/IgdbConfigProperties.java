package com.alfonsoristorato.igdbscraper.service.igdb;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ConfigurationProperties(prefix = "downstreams.igdb")
@Validated
public record IgdbConfigProperties(
        @NotBlank String baseUrl
) {
}
