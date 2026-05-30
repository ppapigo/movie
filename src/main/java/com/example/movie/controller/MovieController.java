package com.example.movie.controller;

import com.example.movie.dto.IngestResult;
import com.example.movie.dto.TmdbGenreResponse;
import com.example.movie.dto.TmdbMovieDTO;
import com.example.movie.dto.TmdbMovieResponse;
import com.example.movie.entity.TmdbMovie;
import com.example.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/popular")
    public TmdbMovieResponse fetchPopularMovies(
            @RequestParam(name="page", defaultValue = "1")Long page
    ){
        return movieService.fetchPopularMovies(page);
    }

    @PostMapping("/sync")
    public IngestResult sync(
            @RequestParam(name="page", defaultValue = "1") Long page
    ){
        return movieService.sync(page);
    }

    @GetMapping
    public TmdbMovieResponse allMovies() {
        TmdbMovieResponse response = new TmdbMovieResponse();
        List<TmdbMovieDTO> results= movieService.allMovies();
        response.setResults(results);
        response.setPage(1);
        response.setTotalPages(1L);
        response.setTotalResults((long) results.size());

        return response;
    }
}
