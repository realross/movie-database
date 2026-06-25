package org.realross;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final UserMovieRepository usermovierepository;
    private final UserRepository userrepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public void rateMovie(Integer userId, Integer movieId, Double rating) {
        UserMovieID id = new UserMovieID(userId, movieId);
        UserMovie userMovie = usermovierepository.findById(id).orElse(new UserMovie());
        userMovie.setId(id);
        userMovie.setRating(rating);
        User user = userrepository.getReferenceById(userId);
        Movie movie = movieRepository.getReferenceById(movieId);
        userMovie.setUser(user);
        userMovie.setMovie(movie);
        usermovierepository.save(userMovie);

        // После сохранения оценки пересчитываем средний рейтинг фильма
        updateMovieAverageRating(movieId);
    }

    public void updateMovieAverageRating(Integer movieId) {
        // Получаем среднее значение оценок для фильма
        Double avgRating = usermovierepository.calculateAverageRatingByMovieId(movieId);
        // Если оценок нет, avgRating будет null – тогда ставим 0 или null
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Фильм не найден"));
        movie.setRating(avgRating != null ? avgRating : 0.0);
        movieRepository.save(movie);
    }

    @Transactional
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Transactional
    public void deleteMovie(Integer id) {
        // Если у вас настроен ON DELETE CASCADE в БД, то записи UserMovies удалятся автоматически.
        // Если нет – удаляем вручную:
        // userMovieRepository.deleteByMovieId(id);
        movieRepository.deleteById(id);
    }
}
