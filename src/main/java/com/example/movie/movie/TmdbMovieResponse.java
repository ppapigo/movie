package com.example.movie.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TmdbMovieResponse {

    private int page;

    @JsonProperty("page_size")
    private int pageSize;

    private List<TmdbMovieDTO> results;

    @JsonProperty("total_pages")
    private Long totalPages;

    @JsonProperty("total_results")
    private Long totalResults;


}
