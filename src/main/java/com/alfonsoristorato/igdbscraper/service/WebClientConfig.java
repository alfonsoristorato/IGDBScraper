package com.alfonsoristorato.igdbscraper.service;

import com.alfonsoristorato.igdbscraper.service.twitch.TwitchAuthenticator;
import com.alfonsoristorato.igdbscraper.service.twitch.TwitchConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {
    private final TwitchConfigProperties twitchConfigProperties;

    public WebClientConfig(TwitchConfigProperties twitchConfigProperties) {
        this.twitchConfigProperties = twitchConfigProperties;
    }

    @Bean
    TwitchAuthenticator twitchAuthenticator() {
        WebClient webClient = WebClient.builder()
                .baseUrl(twitchConfigProperties.baseUrl())
                .build();

        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory
                        .builder(WebClientAdapter.forClient(webClient))
                        .build();
        return httpServiceProxyFactory.createClient(TwitchAuthenticator.class);
    }
}
