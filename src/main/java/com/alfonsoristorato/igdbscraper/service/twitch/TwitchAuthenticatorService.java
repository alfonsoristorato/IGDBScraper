package com.alfonsoristorato.igdbscraper.service.twitch;

import com.alfonsoristorato.igdbscraper.models.IGDBHeaders;
import com.alfonsoristorato.igdbscraper.models.TwitchResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class TwitchAuthenticatorService {

    private final TwitchAuthenticatorClient twitchAuthenticatorClient;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final String GRANT_TYPE;

    public TwitchAuthenticatorService(TwitchAuthenticatorClient twitchAuthenticatorClient, TwitchConfigProperties twitchConfigProperties) {
        this.twitchAuthenticatorClient = twitchAuthenticatorClient;
        this.CLIENT_ID = twitchConfigProperties.clientId();
        this.CLIENT_SECRET = twitchConfigProperties.clientSecret();
        this.GRANT_TYPE = twitchConfigProperties.grantType();
    }
    //TODO: explore if the below can return a flux of headers
    public Mono<List<IGDBHeaders>> getTokens() {
        return Flux.fromStream(IntStream.range(0,4).boxed())
                .flatMap(reqNumber -> twitchAuthenticatorClient.authenticate(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE))
                .map(this::transformTwitchResponse)
                .collectList();
    }

    private IGDBHeaders transformTwitchResponse(TwitchResponse twitchResponse) {
        return new IGDBHeaders(CLIENT_ID, String.join(" ", twitchResponse.tokenType(), twitchResponse.accessToken()));
    }
}
