package com.example.movie.controller;

import com.example.movie.dto.TmdbMovieResponse;
import com.example.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public TmdbMovieResponse fetchPopularMovies(
            @RequestParam(name="page", defaultValue = "1")Long page
    ){
        return movieService.fetchPopularMovies(page);
    }

}
