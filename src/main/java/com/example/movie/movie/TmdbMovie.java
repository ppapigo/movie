package com.example.movie.movie;

import com.example.movie.genre.TmdbGenre;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "movie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbMovie {

    @Column(name = "adult", nullable = false)
    private Boolean adult;

    @JsonProperty("backdrop_path")
    @Column(name = "backdrop_path", length = 500)
    private String backdropPath;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @JsonProperty("original_language")
    @Column(name = "original_language")
    private String originalLanguage;

    @JsonProperty("original_title")
    @Column(name = "original_title", nullable = false)
    private String originalTitle;

    @Column(name = "overview", nullable = false, length = 5000)
    private String overview;

    @Column(name = "popularity")
    private Float popularity;

    @Column(name = "softcore")
    private Boolean softcore;

    @Column(name = "video")
    private Boolean video;

    @Column(name = "poster_path", length = 500)
    private String posterPath;

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "vote_average")
    private Float voteAverage;

    @Column(name = "vote_count")
    private Integer voteCount;

    @Builder.Default
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TmdbMovieGenre> movieGenres = new ArrayList<>();

    public static TmdbMovie fromDTO(TmdbMovieDTO dto) {
        return TmdbMovie.builder()
                .id(dto.getId())
                .adult(dto.getAdult())
                .title(dto.getTitle())
                .overview(dto.getOverview())
                .popularity(dto.getPopularity())
                .backdropPath(dto.getBackdropPath())
                .originalLanguage(dto.getOriginalLanguage())
                .originalTitle(dto.getOriginalTitle())
                .softcore(dto.getSoftcore())
                .video(dto.getVideo())
                .posterPath(dto.getPosterPath())
                .releaseDate(dto.getReleaseDate())
                .voteAverage(dto.getVoteAverage())
                .voteCount(dto.getVoteCount())
                .build();
    }

    public static TmdbMovie fromDtoWithGenres(TmdbMovieDTO dto, Map<Long, TmdbGenre> genreMap) {
        TmdbMovie movie = new TmdbMovieBuilder()
                .id(dto.getId())
                .adult(dto.getAdult())
                .title(dto.getTitle())
                .overview(dto.getOverview())
                .popularity(dto.getPopularity())
                .backdropPath(dto.getBackdropPath())
                .originalLanguage(dto.getOriginalLanguage())
                .originalTitle(dto.getOriginalTitle())
                .softcore(dto.getSoftcore())
                .video(dto.getVideo())
                .posterPath(dto.getPosterPath())
                .releaseDate(dto.getReleaseDate())
                .voteAverage(dto.getVoteAverage())
                .voteCount(dto.getVoteCount())
                .build();


        if (dto.getGenreIds() != null) {
            dto.getGenreIds().forEach(genreId -> {
                TmdbGenre genre = genreMap.get(genreId.longValue());
                if (genre != null) {
                    movie.getMovieGenres().add(
                            TmdbMovieGenre.builder().movie(movie).genre(genre).build()
                    );

                }
            });
        }
/*        for (Integer genre_id : dto.getGenreIds()) {
            if (genre_id != null) {
                movie.getMovieGenres().add(
                        TmdbMovieGenre.builder()
                                .movie(movie)
                                .genre(genreMap.get(genre_id))
                                .build()
                );
            }
        }
        return movie;

 */
        return movie;
    }

    public static TmdbMovieDTO toDTO(TmdbMovie movie){
        TmdbMovieDTO dto =new TmdbMovieDTO();
        dto.setId(movie.getId());
        dto.setAdult(movie.getAdult());
        dto.setOverview(movie.getOverview());
        dto.setPopularity(movie.getPopularity());
        dto.setBackdropPath(movie.getBackdropPath());
        dto.setTitle(movie.getTitle());
        dto.setVideo(movie.getVideo());
        dto.setOriginalLanguage(movie.getOriginalLanguage());
        dto.setOriginalTitle(movie.getOriginalTitle());
        dto.setPosterPath(movie.getPosterPath());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setSoftcore(movie.getSoftcore());
        dto.setVoteCount(movie.getVoteCount());
        dto.setVoteAverage(movie.getVoteAverage());
        List<Integer> genreIds = movie.getMovieGenres().stream().map(
                g->g.getId().intValue()).toList();
        dto.setGenreIds(genreIds);
        return dto;
    }
}