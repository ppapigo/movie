package com.example.movie.movie;

import com.example.movie.genre.TmdbGenre;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "movie_genre",
        uniqueConstraints = @UniqueConstraint(columnNames = {"movie_id","genre_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TmdbMovieGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable=false)
    private TmdbMovie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable=false)
    private TmdbGenre genre;
}
