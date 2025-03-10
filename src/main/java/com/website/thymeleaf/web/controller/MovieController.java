package com.website.thymeleaf.web.controller;

import com.website.thymeleaf.web.model.Movie;
import com.website.thymeleaf.web.repository.MovieRepository;
import com.website.thymeleaf.web.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/lobby")
    public String showLobby() {
        return "lobby";
    }

    //------------------------add-movie--------------------//
    @GetMapping("/add-movie")
    public String showAddMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "add-movie";
    }

    @PostMapping("/add-movie")
    public String addMovie(@Valid @ModelAttribute Movie movie, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Erro ao adicionar filme. Verifique os dados.");
            return "add-movie";
        }
        try {
            movieService.addMovie(movie);
            model.addAttribute("successMessage", "Filme adicionado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erro ao adicionar filme. Verifique os dados.");
            e.printStackTrace();
        }
        return "add-movie";
    }
    //------------------------remove-movie--------------------//
    @GetMapping("/remove-movie")
    public String showRemoveMovieForm() {
        return "remove-movie";
    }

    @PostMapping("/remove-movie")
    public String removeMovie(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("errorMessage", "Por favor, insira o ID do filme.");
            return "remove-movie";
        }
        try {
            if (!movieRepository.existsById(id)) {
                model.addAttribute("errorMessage", "Filme com ID " + id + " não encontrado.");
                return "remove-movie";
            }
            movieService.removeMovie(id);
            model.addAttribute("successMessage", "Filme removido com sucesso!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erro ao remover filme. Verifique o ID.");
            e.printStackTrace();
        }
        return "remove-movie";
    }
    //------------------------movie-list--------------------//
    @GetMapping("/list")
    public String listMovies(@RequestParam(required = false) Long id, Model model) {
        List<Movie> movies = movieService.listMovie();
        model.addAttribute("movies", movies);

        if (id != null) {
            Movie selectedMovie = movieRepository.findById(id).orElse(null);
            if (selectedMovie != null) {
                model.addAttribute("selectedMovie", selectedMovie);
            } else {
                model.addAttribute("errorMessage", "Filme com ID " + id + " não encontrado.");
            }
        }
        return "movie-list";
    }
//------------------------movie-list-actions--------------------//
    @GetMapping("/list-actions")
    public String listMoviesWithActions(Model model) {
        List<Movie> movies = movieService.listMovie();
        model.addAttribute("movies", movies);
        return "movie-list-actions";
    }

    @GetMapping("/remove/{id}")
    public String removeMovieAction(@PathVariable Long id) {
        movieService.removeMovie(id);
        return "redirect:/movies/list-actions";
    }

    @GetMapping("/edit/{id}")
    public String editMovie(@PathVariable Long id, Model model) {
        Movie movie = movieRepository.findById(id).orElse(null);
        model.addAttribute("movie", movie);
        return "edit-movie.html";
    }

    @PostMapping("/update")
    public String updateMovie(@ModelAttribute Movie movie) {
        movieService.updateMovie(movie);
        return "redirect:/movies/list-actions";
    }
}
