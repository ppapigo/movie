package com.example.movie.service;

import com.example.movie.Repository.GenreRepository;
import com.example.movie.Repository.MovieRepository;
import com.example.movie.dto.IngestResult;
import com.example.movie.dto.TmdbMovieDTO;
import com.example.movie.dto.TmdbMovieResponse;
import com.example.movie.entity.TmdbGenre;
import com.example.movie.entity.TmdbMovie;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final RestClient tmdbRestClient;
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    @Value("${tmdb.default-language}")
    private String defaultLanguage;

    private int total;
    private int saved;
    private int skipped;

    public TmdbMovieResponse fetchPopularMovies(Long page) {
        return tmdbRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/movie/popular")
                        .queryParam("language", defaultLanguage)
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .body(TmdbMovieResponse.class);

    }

    @Transactional
    public IngestResult sync(Long page) {
        TmdbMovieResponse response = fetchPopularMovies(page);

        IngestResult result = new IngestResult();


        List<TmdbMovieDTO> movieDTOs = response.getResults();
        total=movieDTOs.size();
        saved=0;
        skipped =0;
        Map<Long, TmdbGenre> genreMap = genreRepository.findAll().stream()
                .collect(Collectors.toMap(TmdbGenre::getId, genre-> genre));

        movieDTOs.forEach(tmdbMovieDTO -> {
          try{

              TmdbMovie movie = TmdbMovie.fromDtoWithGenres(tmdbMovieDTO,genreMap);
              movieRepository.save(movie);
              saved++;

          } catch (IllegalArgumentException | OptimisticLockingFailureException ex) {
             skipped++;
          }


        });
        
        return new IngestResult(total,saved,skipped);
    }

    @Transactional(readOnly = true)
    public List<TmdbMovieDTO> allMovies(){
        return movieRepository.findAllWithGenres().stream().map(
                TmdbMovie::toDTO).toList();

    }
}
