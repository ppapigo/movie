package com.example.movie.genre;

import com.example.movie.common.IngestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final RestClient tmdbRestClient;

    @Value("${tmdb.default-language}")
    private String defaultLanguage;


    public TmdbGenreResponse fetchAllGenres() {
            return tmdbRestClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/genre/movie/list")
                            .queryParam("language", defaultLanguage)
                            .build())
                    .retrieve()
                    .body(TmdbGenreResponse.class);
    }

    public IngestResult sync()  {
    TmdbGenreResponse response = fetchAllGenres();

    List<TmdbGenreDTO> genreDTOS = response.getGenres();
        genreDTOS.forEach(tmdbgenreDTO -> {
        TmdbGenre genre =
                TmdbGenre.fromDTO(tmdbgenreDTO);

        genreRepository.save(genre);
    });
        return new IngestResult(genreDTOS.size(),0,0);
}
}
