package com.example.movie.genre;

import com.example.movie.common.IngestResult;
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
