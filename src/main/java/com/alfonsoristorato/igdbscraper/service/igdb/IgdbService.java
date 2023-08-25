package com.alfonsoristorato.igdbscraper.service.igdb;

import com.alfonsoristorato.igdbscraper.service.twitch.TwitchAuthenticatorService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class IgdbService {
    private final IgdbClient igdbClient;
    private final TwitchAuthenticatorService twitchAuthenticatorService;

    public IgdbService(IgdbClient igdbClient, TwitchAuthenticatorService twitchAuthenticatorService) {
        this.igdbClient = igdbClient;
        this.twitchAuthenticatorService = twitchAuthenticatorService;
    }

    // repeat operation below until full db has been scraped changing the offset based on the last ID received
    // limitations: 4 requests per sec, 500 records per request
    @EventListener(ApplicationReadyEvent.class)
    public void getDataFromIgdb() {
        Flux.fromIterable(twitchAuthenticatorService.getTokens()
                        .flatMapIterable(el -> el).toIterable()
                )
                .flatMap(igdbHeaders ->
                        igdbClient.requestGameData(igdbHeaders.clientId(), igdbHeaders.authorization(), "fields name; limit 500; sort id asc; offset 501;"))
                .doOnNext(System.out::println).subscribe();

    }

}
