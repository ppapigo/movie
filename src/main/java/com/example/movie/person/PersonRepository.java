package com.example.movie.person;

import com.example.movie.genre.TmdbGenre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.net.http.HttpHeaders;
import java.util.Arrays;
import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<TmdbPerson, Long> {

    @Query("""
        SELECT DISTINCT p
        FROM TmdbPerson p
        LEFT JOIN FETCH p.moviePersonList mp
        LEFT JOIN FETCH mp.movie
        WHERE p.name LIKE CONCAT('%', :pname, '%')
        ORDER BY p.name DESC
    """)
    Page<TmdbPerson> findByName(@Param("pname") String pname, Pageable pageable);


}
