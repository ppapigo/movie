package com.example.movie.person;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<TmdbPerson, Long> {


    Page<TmdbPerson> findAllByOrderByName(Pageable pageable);


    @Query("""
        SELECT p
        FROM TmdbPerson p
        WHERE p.name LIKE CONCAT('%', :pname, '%')
        ORDER BY p.name DESC
    """)
    Page<TmdbPerson> findByName(@Param("pname") String pname, Pageable pageable);


    @Query("SELECT p FROM TmdbPerson p ORDER BY p.popularity DESC LIMIT 10")
    List<TmdbPerson> findTop10ByOrderByPopularityDesc();
}
