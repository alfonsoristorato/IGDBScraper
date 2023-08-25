package com.alfonsoristorato.igdbscraper.service.igdb;

import com.alfonsoristorato.igdbscraper.models.TwitchResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface IgdbClient {
    @PostExchange
    Mono<ResponseEntity<String>> requestGameData(
            @RequestHeader("Client-Id") String clientId,
            @RequestHeader() String authorization,
            @RequestBody() String body
    );
}
