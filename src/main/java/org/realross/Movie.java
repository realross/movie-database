package org.realross;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Movies")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "Year")
    private Integer year;

    @Column(name = "Genre")
    private String genre;

    @Column(name = "Title")
    private String posterUrl;   // ссылка на постер

    @Column(name = "Site")
    private String videoUrl;    // ссылка на видео

    @Column(name = "rating")
    private Double rating;
}