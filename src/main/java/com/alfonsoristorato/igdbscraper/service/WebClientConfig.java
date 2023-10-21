package com.alfonsoristorato.igdbscraper.service;

import com.alfonsoristorato.igdbscraper.service.igdb.IGDBConfigProperties;
import com.alfonsoristorato.igdbscraper.service.igdb.IGDBClient;
import com.alfonsoristorato.igdbscraper.service.twitch.TwitchAuthenticatorClient;
import com.alfonsoristorato.igdbscraper.service.twitch.TwitchConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {
    private final TwitchConfigProperties twitchConfigProperties;
    private final IGDBConfigProperties igdbConfigProperties;

    public WebClientConfig(TwitchConfigProperties twitchConfigProperties, IGDBConfigProperties igdbConfigProperties) {
        this.twitchConfigProperties = twitchConfigProperties;
        this.igdbConfigProperties = igdbConfigProperties;
    }

    @Bean
    TwitchAuthenticatorClient twitchAuthenticator() {
        WebClient webClient = WebClient.builder()
                .baseUrl(twitchConfigProperties.baseUrl())
                .build();

        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory
                        .builder(WebClientAdapter.forClient(webClient))
                        .build();
        return httpServiceProxyFactory.createClient(TwitchAuthenticatorClient.class);
    }

    @Bean
    IGDBClient igdbClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(igdbConfigProperties.baseUrl())
                .codecs(codecs -> codecs
                        .defaultCodecs()
                        .maxInMemorySize(1000 * 1024))

                .build();

        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory
                        .builder(WebClientAdapter.forClient(webClient))
                        .build();
        return httpServiceProxyFactory.createClient(IGDBClient.class);
    }
}
