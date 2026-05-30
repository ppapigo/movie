package com.example.movie.entity;

import com.example.movie.dto.TmdbGenreDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "genre")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TmdbGenre {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 500)
    private String name;


    public static TmdbGenre fromDTO(TmdbGenreDTO dto) {
        return TmdbGenre.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}

