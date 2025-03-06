package com.website.thymeleaf.web.service;

import java.util.List;

import com.website.thymeleaf.web.model.Movie;
import com.website.thymeleaf.web.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, Movie movie) {
        movie.setId(id);
        return movieRepository.save(movie);
    }

    public void removeMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public Movie getMovie(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    public List<Movie> listMovie() {
        System.out.println("MovieService.listMovie() called");
        List<Movie> movies = movieRepository.findAll();
        System.out.println("MovieService.listMovie() found " + movies.size() + " movies");
        for(Movie movie : movies){
            System.out.println("Movie: " + movie.toString());
        }
        return movies;
    }
}