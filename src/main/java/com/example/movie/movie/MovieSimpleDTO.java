package com.example.movie.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieSimpleDTO {

    private Long id;
    private String title;
    private String posterPath;
    private Float voteAverage;

    public static MovieSimpleDTO toDTO(TmdbMovie movie) {
        MovieSimpleDTO dto = new MovieSimpleDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setPosterPath(movie.getPosterPath());
        dto.setVoteAverage(movie.getVoteAverage());

        return dto;
    }
}