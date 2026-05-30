package com.example.movie.Repository;

import com.example.movie.entity.TmdbGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<TmdbGenre, Long> {
}
