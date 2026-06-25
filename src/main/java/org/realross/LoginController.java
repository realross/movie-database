package org.realross;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final MovieService movieService;
    private final UserMovieRepository usermovierepository;

    // Страница логина
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Обработка логина
    @PostMapping("/login")
    public String login(@RequestParam("login") String login,
                        HttpSession session,
                        Model model) {
        Optional<User> userOpt = userService.findByLogin(login);
        if (userOpt.isPresent()) {
            // Сохраняем пользователя в сессии
            session.setAttribute("user", userOpt.get());
            return "redirect:/movies";
        } else {
            model.addAttribute("error", "Пользователь с логином " + login + " не найден");
            return "login";
        }
    }


    @GetMapping("/movies/new")
    public String showAddForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "add-movie";
    }

    @GetMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        movieService.deleteMovie(id);
        return "redirect:/movies";
    }

    @PostMapping("/movies")
    public String addMovie(@ModelAttribute Movie movie, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        movieService.saveMovie(movie);
        return "redirect:/movies";
    }

    // Список фильмов (только для авторизованных)
    @GetMapping("/movies")
    public String movies(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Movie> movies = movieService.getAllMovies();
        // Собираем оценки пользователя
        Map<Integer, Double> userRatings = new HashMap<>();
        for (Movie movie : movies) {
            UserMovieID id = new UserMovieID(user.getId(), movie.getId());
            Optional<UserMovie> userMovieOpt = usermovierepository.findById(id);
            userMovieOpt.ifPresent(um -> userRatings.put(movie.getId(), um.getRating()));
        }
        model.addAttribute("movies", movies);
        model.addAttribute("user", user);
        model.addAttribute("userRatings", userRatings);
        return "movies";
    }

    // Выход
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/rate")
    public String rateMovie(@RequestParam("movieId") Integer movieId,
                            @RequestParam("rating") Double rating,
                            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        movieService.rateMovie(user.getId(), movieId, rating);
        return "redirect:/movies";
    }
}
