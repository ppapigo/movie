package com.example.movie.person;

import com.example.movie.common.IngestResult;
import com.example.movie.movie.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class PersonService {
    private final RestClient restClient;
    private final PersonRepository personRepository;
    private final MovieRepository movieRepository;

    @Value("${tmdb.default-language}")
    private String defaultLanguage;

    private int total;
    private int saved;
    private int skipped;

    public TmdbPersonResponse fetchAll(int page){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/person/popular")
                    .queryParam("language",defaultLanguage)
                .queryParam("page",page)
                .build())
                .retrieve()
                .body(TmdbPersonResponse.class);

    }

    @Transactional
    public IngestResult sync(int page) {
        TmdbPersonResponse response = fetchAll(page);

        IngestResult result = new IngestResult();


        List<TmdbPersonDTO> personDTOs = response.getResults();
        total = personDTOs.size();
        saved = 0;
        skipped = 0;
        Map<Long, TmdbMovie> movieMap = movieRepository.findAll().stream()
                .collect(Collectors.toMap(TmdbMovie::getId, movie -> movie));

        personDTOs.forEach(tmdbPersonDTO -> {
            try {

                if (tmdbPersonDTO.getProfilePath() == null) {
                    skipped++;
                    return;
                }
                TmdbPerson person = TmdbPerson.fromDTO(tmdbPersonDTO);

                if (tmdbPersonDTO.getKnownFor() != null) {

                    tmdbPersonDTO.getKnownFor().forEach(movieDto -> {
                        if (!"movie".equals(movieDto.getMediaType())) {
                            return;
                        }
                        TmdbMovie movie =
                                movieMap.get(movieDto.getId());

                        if (movie == null) {

                            movie = TmdbMovie.fromDTO(movieDto);

                            movieRepository.save(movie);

                            movieMap.put(movie.getId(), movie);
                        }

                        TmdbMoviePerson moviePerson =
                                TmdbMoviePerson.builder()
                                        .movie(movie)
                                        .person(person)
                                        .build();

                        person.getMoviePersonList().add(moviePerson);
                    });
                }

                personRepository.save(person);

                saved++;

            } catch (IllegalArgumentException |
                     OptimisticLockingFailureException ex) {

                skipped++;
            }
        });
        return new IngestResult(total, saved, skipped);
    }

    public Page<TmdbPersonDTO> findByName(String pname, @Min(1) int page, int pageSize) {
        Pageable pageable = PageRequest.of(page-1,pageSize);
        return personRepository.findByName(pname,pageable).map(
                TmdbPerson::toDTO);
    }
}
