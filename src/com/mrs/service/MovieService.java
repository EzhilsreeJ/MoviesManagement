package com.mrs.service;

import com.mrs.model.Movie;
import com.mrs.repository.MovieRepository;
import com.mrs.exception.MovieNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class MovieService {

    private MovieRepository repo;

    public MovieService(MovieRepository repo) {
        this.repo = repo;
    }

    public List<Movie> getAllMovies() {
        return repo.getAllMovies();
    }

    public Movie searchByTitle(String title) throws MovieNotFoundException {
        return repo.getAllMovies().stream()
                .filter(m -> m.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));
    }

    public List<String> getAllGenres() {
        return repo.getAllMovies().stream()
                .map(Movie::getGenre)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Movie> filterByGenre(String genre) {
        return repo.getAllMovies().stream()
                .filter(m -> m.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    public List<Movie> getTopRated(int limit) {
        return repo.getAllMovies().stream()
                .sorted((a, b) -> Double.compare(b.getRating(), a.getRating()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public boolean addMovie(Movie m) {
        return repo.addMovie(m);
    }

    public boolean deleteMovie(String title) {
        return repo.deleteMovie(title);
    }

    public boolean updateMovie(String oldTitle, Movie updated) {
        return repo.updateMovie(oldTitle, updated);
    }

    public void showStats() {
        List<Movie> list = repo.getAllMovies();

        System.out.println("\n Dashboard");
        System.out.println("Total Movies: " + list.size());

        long genres = list.stream().map(Movie::getGenre).distinct().count();
        System.out.println("Unique Genres: " + genres);

        double avg = list.stream().mapToDouble(Movie::getRating).average().orElse(0);
        System.out.println("Average Rating: " + avg);

        list.stream()
                .max(Comparator.comparingDouble(Movie::getRating))
                .ifPresent(m -> System.out.println("Top Movie: " + m));
    }

    public List<Movie> getPopularMovies() {
        return repo.getAllMovies().stream()
                .filter(m -> m.getRating() == 10)
                .toList();
    }

    public void showUserDashboard() {
        System.out.println("\n User Dashboard");
        showStats();
    }
}