package com.example.movie.Repository;

import com.example.movie.entity.TmdbMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<TmdbMovie,Long> {

    //모든 영화 목록을 장르와 함께 가져오기
    @Query("SELECT DISTINCT m FROM TmdbMovie m LEFT JOIN FETCH m.movieGenres mg JOIN FETCH mg.genre")
    List<TmdbMovie> findAllWithGenres();
}
