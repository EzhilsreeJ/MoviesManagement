package com.mrs.repository;

import com.mrs.model.Movie;
import com.mrs.util.FileUtil;

import java.io.FileWriter;
import java.util.*;

public class MovieRepository {

    private List<Movie> movies = new ArrayList<>();
    private Set<String> movieTitles = new HashSet<>();
    private String filePath;

    public MovieRepository(String filePath) {
        this.filePath = filePath;

        for (Movie m : FileUtil.loadMovies(filePath)) {
            if (movieTitles.add(m.getTitle().toLowerCase())) {
                movies.add(m);
            }
        }
    }

    public List<Movie> getAllMovies() {
        return movies;
    }


    public boolean addMovie(Movie movie) {
        if (movieTitles.contains(movie.getTitle().toLowerCase())) return false;

        movies.add(movie);
        movieTitles.add(movie.getTitle().toLowerCase());
        saveToFile(movie);
        return true;
    }

    private void saveToFile(Movie m) {
        try (FileWriter fw = new FileWriter(filePath, true)) {
            fw.write("\n" + m.getTitle() + "," + m.getGenre() + "," + m.getRating() + "," + m.getYear());
        } catch (Exception e) {
            System.out.println("Error saving file");
        }
    }

    public boolean deleteMovie(String title) {
        Iterator<Movie> it = movies.iterator();

        while (it.hasNext()) {
            Movie m = it.next();
            if (m.getTitle().equalsIgnoreCase(title)) {
                it.remove();
                movieTitles.remove(title.toLowerCase());
                rewriteFile();
                return true;
            }
        }
        return false;
    }

    public boolean updateMovie(String oldTitle, Movie updated) {

        for (int i = 0; i < movies.size(); i++) {
            Movie m = movies.get(i);

            if (m.getTitle().equalsIgnoreCase(oldTitle)) {

                if (!oldTitle.equalsIgnoreCase(updated.getTitle()) &&
                        movieTitles.contains(updated.getTitle().toLowerCase())) {
                    return false;
                }

                movieTitles.remove(oldTitle.toLowerCase());
                movieTitles.add(updated.getTitle().toLowerCase());

                movies.set(i, updated);
                rewriteFile();
                return true;
            }
        }
        return false;
    }

    private void rewriteFile() {
        try (FileWriter fw = new FileWriter(filePath)) {

            fw.write("title,genre,rating,year\n");

            for (Movie m : movies) {
                fw.write(m.getTitle() + "," + m.getGenre() + "," +
                        m.getRating() + "," + m.getYear() + "\n");
            }

        } catch (Exception e) {
            System.out.println("Error rewriting file");
        }
    }
}