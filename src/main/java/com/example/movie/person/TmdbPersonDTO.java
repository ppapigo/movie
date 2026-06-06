package com.example.movie.person;

import com.example.movie.movie.MovieSimpleDTO;
import com.example.movie.movie.TmdbMovie;
import com.example.movie.movie.TmdbMovieDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TmdbPersonDTO {
    private Boolean adult;
    private String gender;
    private long id;

    @JsonProperty("known_for_department")
    private String knownForDepartment;

    private String name;

    @JsonProperty("original_name")
    private String originalName;

    private double popularity;

    @JsonProperty("profile_path")
    private String profilePath;

    @JsonProperty("known_for")
    private List<TmdbMovieDTO> knownFor;

    private List<MovieSimpleDTO> movies;
}
