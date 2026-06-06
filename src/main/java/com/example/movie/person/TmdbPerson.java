package com.example.movie.person;

import com.example.movie.genre.TmdbGenre;
import com.example.movie.movie.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TmdbPerson {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "adult", nullable = false)
    private Boolean adult;

    @Column(name = "gender")
    private String gender;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "popularity", nullable = false)
    private double popularity;

    @Column(name = "known_for_department", length = 100)
    private String knownForDepartment;

    @Column(name = "original_name", nullable = false, length = 500)
    private String originalName;

    @Column(name = "profile_path", nullable = false, length = 500)
    private String profilePath;

    @Builder.Default
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TmdbMoviePerson> moviePersonList = new ArrayList<>();



    public static TmdbPerson fromDTO(TmdbPersonDTO dto) {
        return TmdbPerson.builder()
                .id(dto.getId())
                .adult(dto.getAdult())
                .gender(dto.getGender())
                .name(dto.getName())
                .popularity(dto.getPopularity())
                .knownForDepartment(dto.getKnownForDepartment())
                .originalName(dto.getOriginalName())
                .profilePath(dto.getProfilePath())
                .build();


    }
    public static TmdbPerson fromDtoWithMovie(TmdbPersonDTO dto, Map<Long, TmdbMovie> movieMap) {
        TmdbPerson person = new TmdbPersonBuilder()
                .adult(dto.getAdult())
                .gender(dto.getGender())
                .id(dto.getId())
                .knownForDepartment(dto.getKnownForDepartment())
                .name(dto.getName())
                .originalName(dto.getOriginalName())
                .popularity(dto.getPopularity())
                .profilePath(dto.getProfilePath())
                .build();

        if (dto.getKnownFor() != null) {
            dto.getKnownFor().forEach(movieDTO -> {
                TmdbMovie movie = movieMap.get(movieDTO.getId());
                if (movie != null) {
                    person.getMoviePersonList().add(
                            TmdbMoviePerson.builder().person(person).movie(movie).build()
                    );

                }
            });
        }

        return person;
    }

    public static TmdbPersonDTO toDTO(TmdbPerson person) {
        TmdbPersonDTO dto =new TmdbPersonDTO();
        dto.setId(person.getId());
        dto.setAdult(person.getAdult());
        dto.setGender(person.getGender());
        dto.setName(person.getName());
        dto.setPopularity(person.getPopularity());
        dto.setKnownForDepartment(person.getKnownForDepartment());
        dto.setOriginalName(person.getOriginalName());
        dto.setProfilePath(person.getProfilePath());

        List<MovieSimpleDTO> movies =
                person.getMoviePersonList()
                        .stream()
                        .map(mp -> MovieSimpleDTO.toDTO(mp.getMovie()))
                        .toList();

        dto.setMovies(movies);

        return dto;
    }
}
