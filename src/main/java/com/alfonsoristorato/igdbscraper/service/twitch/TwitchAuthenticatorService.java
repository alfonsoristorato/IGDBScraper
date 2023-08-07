package com.alfonsoristorato.igdbscraper.service.twitch;

import com.alfonsoristorato.igdbscraper.models.IGDBHeaders;
import com.alfonsoristorato.igdbscraper.models.TwitchResponse;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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
    //TODO: explore if the below can return a flux of headers
    public Mono<List<IGDBHeaders>> getTokens() {
        return Flux.fromIterable(CLIENT_SECRETS)
                .flatMap(secret -> twitchAuthenticator.authenticate(CLIENT_ID, secret, GRANT_TYPE))
                .map(this::transformTwitchResponse)
                .collectList();
    }

    private IGDBHeaders transformTwitchResponse(TwitchResponse twitchResponse) {
        return new IGDBHeaders(CLIENT_ID, String.join(" ", twitchResponse.tokenType(), twitchResponse.accessToken()));
    }
}
