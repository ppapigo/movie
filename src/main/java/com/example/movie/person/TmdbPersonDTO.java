package com.example.movie.person;

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
    private boolean adult;
    private int gender;
    private long id;

    @JsonProperty("known_for_department")
    private String KnownForDepartment;

    private String name;

    @JsonProperty("original_name")
    private String originalName;

    private double popularity;

    @JsonProperty("profile_path")
    private String profilePath;

    @JsonProperty("known_for")
    private List<TmdbMovieDTO> KnownFor;
}
