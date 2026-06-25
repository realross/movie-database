package org.realross;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMovieRepository extends JpaRepository<UserMovie, UserMovieID> {
    Optional<UserMovie> findById(UserMovieID id);
    void deleteByMovieId(Integer movieId);
    @Query("SELECT AVG(um.rating) FROM UserMovie um WHERE um.movie.id = :movieId")
    Double calculateAverageRatingByMovieId(@Param("movieId") Integer movieId);
}
