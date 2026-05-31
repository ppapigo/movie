package com.example.movie.movie;

import com.example.movie.genre.TmdbGenre;
import com.example.movie.person.TmdbPerson;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie_person",
        uniqueConstraints = @UniqueConstraint(columnNames = {"movie_id","person_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TmdbMoviePerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable=false)
    private TmdbMovie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable=false)
    private TmdbPerson person;
}
