package org.realross;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "UserMovies")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserMovie {

    @EmbeddedId
    private UserMovieID id;

    @Column(name = "Rating")
    private Double rating;

    @Column(name = "Watched")
    private Boolean watched;

    @Column(name = "Favorite")
    private Boolean favorite;

    @ManyToOne
    @MapsId("UserID")
    @JoinColumn(name = "User_ID")
    private User user;

    @ManyToOne
    @MapsId("MovieID")
    @JoinColumn(name = "Movie_ID")
    private Movie movie;
}
