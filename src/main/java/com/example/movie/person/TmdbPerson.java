package com.example.movie.person;

import com.example.movie.movie.TmdbMovieGenre;
import com.example.movie.movie.TmdbMoviePerson;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private Long adult;

    @Column(name = "gender")
    private String gender;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "popularity", nullable = false)
    private String popularity;

    @Column(name = "known_for_department", length = 100)
    private String KnownForDepartment;

    @Column(name = "original_name", nullable = false, length = 500)
    private String originalName;

    @Column(name = "profile_path", nullable = false, length = 500)
    private String profilePath;

    @Builder.Default
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TmdbMoviePerson> moviePersonList = new ArrayList<>();

}
