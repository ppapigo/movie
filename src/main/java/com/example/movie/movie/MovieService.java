package com.example.movie.movie;

import com.example.movie.genre.GenreRepository;
import com.example.movie.common.IngestResult;
import com.example.movie.genre.TmdbGenreDTO;
import com.example.movie.genre.TmdbGenre;

import com.example.movie.person.TmdbPerson;
import com.example.movie.person.TmdbPersonDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

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
    public Page<TmdbMovieDTO> allMovies(int page, int pageSize){
        Pageable pageable = PageRequest.of(page-1,pageSize);
        return movieRepository.findAllWithGenres(pageable).map(
                TmdbMovie::toDTO);

    }

    @Transactional
    public Page<TmdbMovieDTO> findByGenreId(Long genreId, int page, int pageSize){
        Pageable pageable = PageRequest.of(page-1,pageSize);
        return movieRepository.findAllByGenreId(genreId, pageable).map(
                TmdbMovie::toDTO);
    }


    @Transactional
    public Page<TmdbMovieDTO> findByOriginalLanguage(String lang,int page, int pageSize) {
        Pageable pageable = PageRequest.of(page-1,pageSize);
        return movieRepository.findAllByOriginalLanguage(lang, pageable).map(
                TmdbMovie::toDTO);

    }

    @Transactional
    public Page<TmdbMovieDTO> findByTitle(String title, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page-1,pageSize);
        return movieRepository.findAllByTitle(title, pageable).map(
                TmdbMovie::toDTO);

    }

    @Transactional
    public Page<TmdbMovieDTO> listByVoteAvg(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page-1,pageSize);
        return movieRepository.findAllByOrderByVoteAverage(pageable).map(
                TmdbMovie::toDTO);
    }

    @Transactional
    public List<TmdbGenreDTO> genres(long movieId) {
        return movieRepository.findGenresByMovieId(movieId).stream().map(
                TmdbGenre::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<TmdbPersonDTO> personWithMovie(Long id) {
        TmdbMovie movie =
                movieRepository.findById(id)
                        .orElseThrow();

        return movie.getMoviePersonList()
                .stream()
                .map(mp ->
                        TmdbPerson.toDTO(
                                mp.getPerson()
                        )
                )
                .toList();
    }
}
