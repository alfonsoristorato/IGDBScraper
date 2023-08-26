package com.alfonsoristorato.igdbscraper.service.igdb;

import com.alfonsoristorato.igdbscraper.service.twitch.TwitchAuthenticatorService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

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
    public void getDataFromIgdb() throws InterruptedException {
        AtomicLong offset = new AtomicLong(0);

        Flux.interval(Duration.ofMillis(250))
                .flatMap(time -> twitchAuthenticatorService.getTokens())
                .flatMapIterable(igdbHeadersList -> igdbHeadersList)
                .flatMap(igdbHeaders ->
                        igdbClient.requestGameData(
                                igdbHeaders.clientId(),
                                igdbHeaders.authorization(),
                                String.format("fields name; limit 500; sort id asc; offset %s;", offset.getAndAdd(500))))
                .doOnError(System.out::println)
                .onTerminateDetach()
//                .doOnNext(System.out::println)
                .subscribe();


    }

}
