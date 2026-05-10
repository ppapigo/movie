package com.example.movie.service;

import com.example.movie.dto.TmdbMovieResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final RestClient tmdbRestClient;

    @Value("${tmdb.default-language}")
    private String defaultLanguage;


    public TmdbMovieResponse fetchPopularMovies(Long page){
        return tmdbRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/movie/popular")
                        .queryParam("language", defaultLanguage)
                        .queryParam("page",page)
                        .build())
                .retrieve()
                .body(TmdbMovieResponse.class);

    }

    //fetchTopHeadLines:
    //fetchPopularMovies


}
