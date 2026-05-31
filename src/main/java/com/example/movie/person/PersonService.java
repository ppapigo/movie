package com.example.movie.person;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final RestClient restClient;

    @Value("${tmdb.default-language}")
    private String defaultLanguage;

    public TmdbPersonResponse fetchAll(int page){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/person/popular")
                    .queryParam("language",defaultLanguage)
                .queryParam("page",page)
                .build())
                .retrieve()
                .body(TmdbPersonResponse.class);

    }
}
