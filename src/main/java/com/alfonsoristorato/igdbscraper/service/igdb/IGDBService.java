package com.alfonsoristorato.igdbscraper.service.igdb;

import com.alfonsoristorato.igdbscraper.models.IGDBHeaders;
import com.alfonsoristorato.igdbscraper.service.twitch.TwitchAuthenticatorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class IGDBService {
    private final ObjectMapper mapper;
    private final IGDBClient igdbClient;
    private final TwitchAuthenticatorService twitchAuthenticatorService;

    public IGDBService(ObjectMapper mapper, IGDBClient igdbClient, TwitchAuthenticatorService twitchAuthenticatorService) {
        this.mapper = mapper;
        this.igdbClient = igdbClient;
        this.twitchAuthenticatorService = twitchAuthenticatorService;
    }

    private Mono<Integer> getLastAvailableId() {
        return twitchAuthenticatorService.getTokens().map(igdbHeadersList -> {
                    IGDBHeaders igdbHeaders = igdbHeadersList.get(0);
                    return igdbClient.requestGameData(
                            igdbHeaders.clientId(),
                            igdbHeaders.authorization(),
                            "limit 1; sort id desc; offset 1;");
                })
                .flatMap(responseEntityMono -> responseEntityMono)
                .map(this::convertToNode)
                .map(node -> node.get(0).get("id").asInt());
    }

    private JsonNode convertToNode(ResponseEntity<String> response) {
        try {
            return mapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    // repeat operation below until full db has been scraped changing the offset based on the last ID received
    // limitations: 4 requests per sec per token, max 4 tokens, 500 records per request
    @EventListener(ApplicationReadyEvent.class)
    public void getDataFromIGDB() {
        Stopwatch timer = Stopwatch.createStarted();
        AtomicLong offset = new AtomicLong(0);
        getLastAvailableId()
                .subscribe(lastAvailableId -> {
                    Flux.interval(Duration.ofMillis(1050))
                            .takeWhile(tick -> offset.get() < lastAvailableId)
                            .flatMap(tick -> twitchAuthenticatorService.getTokens()
                                    .flatMapIterable(igdbHeadersList -> igdbHeadersList)
                                    .flatMap(igdbHeaders -> igdbClient.requestGameData(
                                            igdbHeaders.clientId(),
                                            igdbHeaders.authorization(),
                                            String.format("fields name; limit 500; sort id asc; offset %s;", offset.getAndAdd(500))))
                                    .doOnError(System.out::println)
                                    .subscribeOn(Schedulers.parallel())
                            )
//                          .doOnNext(System.out::println)
                            .doOnError(System.out::println)
                            .subscribe(el -> {
                                if (offset.get() > lastAvailableId) System.out.println(timer.stop().elapsed(TimeUnit.SECONDS));
                            });
                });
    }

}
