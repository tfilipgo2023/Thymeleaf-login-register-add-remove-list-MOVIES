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

    @GetMapping("/add-movie")
    public String showAddMovieForm(Model model) {
        model.addAttribute("movie", new Movie()); // Adiciona um objeto Movie ao Model
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

    @GetMapping("/update/{id}")
    public String showUpdateMovieForm(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovie(id);
        model.addAttribute("movie", movie);
        return "updateMovie";
    }

    @PostMapping("/update/{id}")
    public String updateMovie(@PathVariable Long id, @ModelAttribute Movie movie) {
        movieService.updateMovie(id, movie);
        return "redirect:/movies/list";
    }

    @GetMapping("/delete/{id}")
    public String removeMovie(@PathVariable Long id) {
        movieService.removeMovie(id);
        return "redirect:/movies/list";
    }

    @GetMapping("/{id}")
    public String getMovie(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovie(id);
        model.addAttribute("movie", movie);
        return "movieDetails";
    }

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
}
