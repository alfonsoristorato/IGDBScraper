package com.alfonsoristorato.igdbscraper.service.twitch;

import com.alfonsoristorato.igdbscraper.models.IGDBHeaders;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TwitchAuthenticatorService {

    private final TwitchAuthenticator twitchAuthenticator;
    private final String CLIENT_ID;
    private final List<String> CLIENT_SECRETS;
    private final String GRANT_TYPE;
    public TwitchAuthenticatorService(TwitchAuthenticator twitchAuthenticator, TwitchConfigProperties twitchConfigProperties) {
        this.twitchAuthenticator = twitchAuthenticator;
        this.CLIENT_ID = twitchConfigProperties.clientId();
        this.CLIENT_SECRETS = twitchConfigProperties.clientSecrets();
        this.GRANT_TYPE = twitchConfigProperties.grantType();
    }

    @EventListener(ApplicationReadyEvent.class)
    public Mono<IGDBHeaders> getToken() {
        return twitchAuthenticator.authenticate(CLIENT_ID, CLIENT_SECRETS.get(0), GRANT_TYPE)
                .map(response ->

                        new IGDBHeaders(CLIENT_ID, String.join(" ", response.tokenType(), response.accessToken()))

                );


    }
}
