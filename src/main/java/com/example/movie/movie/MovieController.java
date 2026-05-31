package com.example.movie.movie;

import com.example.movie.common.IngestResult;
import com.example.movie.genre.TmdbGenreDTO;
import com.example.movie.genre.TmdbGenreResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/test")
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

    //쿼리 파라미터를 추가하여 language를 받아 language코드가 같은 영화목록받아오기
    @GetMapping
    public TmdbMovieResponse allMovies(
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false, name = "lang") String lang,
            @RequestParam(required = false, name = "title") String title,

            @RequestParam(required = false, name = "page", defaultValue = "1") @Min(1) int page,
            @RequestParam(required = false, name = "size", defaultValue = "12") @Min(1) @Max(100) int size

    ) {
        TmdbMovieResponse response = new TmdbMovieResponse();




        Page<TmdbMovieDTO> results= null;
        if( genreId != null)
            results = movieService.findByGenreId(genreId, page, size);
        else if(lang != null)
            results = movieService.findByOriginalLanguage(lang,page, size);
        else if (title != null) {
            results = movieService.findByTitle(title,page, size);

        }
        else
            results = movieService.allMovies(page,size);

        response.setPage(results.getNumber());
        response.setPageSize(results.getSize());
        response.setTotalPages((long) results.getTotalPages());
        response.setResults(results.getContent());
        response.setTotalResults((long) results.getTotalElements());




        return response;
    }
    @GetMapping("/voteavg")
    public TmdbMovieResponse allMoviesByVoteAvg(

            @RequestParam(required = false, name = "page", defaultValue = "1") @Min(1) int page,
            @RequestParam(required = false, name = "size", defaultValue = "12") @Min(1) @Max(100) int size

    ){
        TmdbMovieResponse response = new TmdbMovieResponse();

        Page<TmdbMovieDTO> results= movieService.listByVoteAvg(page, size);


        response.setPage(results.getNumber());
        response.setPageSize(results.getSize());
        response.setTotalPages((long) results.getTotalPages());
        response.setResults(results.getContent());
        response.setTotalResults((long) results.getTotalElements());

        return response;
    }

    //특정 영화의 장르들을 가져오는 API
    @GetMapping("/{id}/genres")
    public TmdbGenreResponse genres(@PathVariable("id") Long id){
        TmdbGenreResponse response = new TmdbGenreResponse();

        List<TmdbGenreDTO> results = movieService.genres(id);
        if(results != null && !results.isEmpty()){
            response.setGenres(results);
        }
        return response;
    }

}
