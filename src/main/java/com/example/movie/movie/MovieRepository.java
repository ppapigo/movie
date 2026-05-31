package com.example.movie.movie;

import com.example.movie.genre.TmdbGenre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<TmdbMovie,Long> {

    //모든 영화 목록을 장르와 함께 가져오기
    @Query("SELECT DISTINCT m FROM TmdbMovie m LEFT JOIN FETCH m.movieGenres mg JOIN FETCH mg.genre")
    Page<TmdbMovie> findAllWithGenres(Pageable pageable);

    @Query("SELECT DISTINCT m FROM TmdbMovie m " +
             "LEFT JOIN FETCH m.movieGenres mg " +
             "LEFT JOIN FETCH mg.genre " +
             "WHERE mg.genre.id = :genreId " +
             "ORDER BY m.releaseDate DESC")
    Page<TmdbMovie> findAllByGenreId(@Param("genreId") Long genreId, Pageable pageable);

    @Query("SELECT DISTINCT m FROM TmdbMovie m " +
            "LEFT JOIN FETCH m.movieGenres mg " +
            "LEFT JOIN FETCH mg.genre " +
            "WHERE m.originalLanguage = :lang " +
            "ORDER BY m.releaseDate DESC")
    Page<TmdbMovie> findAllByOriginalLanguage(@Param("lang") String lang, Pageable pageable);

    @Query("SELECT DISTINCT m FROM TmdbMovie m " +
            "LEFT JOIN FETCH m.movieGenres mg " +
            "LEFT JOIN FETCH mg.genre " +
            "WHERE m.title LIKE CONCAT('%', :keyword, '%')" +
            "ORDER BY m.releaseDate DESC")
    Page<TmdbMovie> findAllByTitle(@Param("keyword") String title, Pageable pageable);

    //평점 투표가 1이상인 영화를 평점순으로 정렬
    @Query("SELECT DISTINCT m FROM TmdbMovie m " +
            "LEFT JOIN FETCH m.movieGenres mg " +
            "LEFT JOIN FETCH mg.genre " +
            "WHERE  m.voteCount >= 1" +
            " ORDER BY m.voteAverage DESC ,m.releaseDate DESC")
    Page<TmdbMovie> findAllByOrderByVoteAverage(Pageable pageable);

    @Query("SELECT mg.genre FROM TmdbMovieGenre mg " +
            "WHERE  mg.movie.id = :movieId" )
    List<TmdbGenre> findGenresByMovieId(@Param("movieId") long movieId);
}

