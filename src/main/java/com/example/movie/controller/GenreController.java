package com.example.movie.controller;

import com.example.movie.dto.IngestResult;
import com.example.movie.dto.TmdbGenreResponse;
import com.example.movie.service.GenreService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genre")
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    TmdbGenreResponse fetchAllGenres() {
        return genreService.fetchAllGenres();

    }
    @PostMapping("/sync")
    public IngestResult sync(
    ){
        return genreService.sync();
    }
}
