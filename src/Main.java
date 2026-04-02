import com.mrs.service.MovieService;
import com.mrs.service.AuthService;
import com.mrs.repository.MovieRepository;
import com.mrs.model.Movie;
import com.mrs.exception.MovieNotFoundException;
import com.mrs.exception.DuplicateMovieException;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        MovieService service = new MovieService(new MovieRepository("movies.csv"));
        AuthService auth = new AuthService();

        while (true) {

            System.out.println("\n===== LOGIN =====");

            System.out.print("Username: ");
            String username = sc.nextLine();

            System.out.print("Password: ");
            String password = sc.nextLine();

            String role = auth.login(username, password);

            if (role == null) {
                System.out.println(" Invalid credentials!");
                continue;
            }

            System.out.println(" Login successful as " + role);

            if (role.equals("USER")) {
                System.out.println("\nPopular Movies (Rating = 10):");
                List<Movie> popular = service.getPopularMovies();

                if (popular.isEmpty()) {
                    System.out.println("No popular movies found.");
                } else {
                    popular.forEach(System.out::println);
                }
            }

            while (true) {

                System.out.println("\n=== Movie CLI ===");
                System.out.println("1. View All Movies");
                System.out.println("2. Search Movie");
                System.out.println("3. Filter by Genre");
                System.out.println("4. Top Rated Movies");
                System.out.println("5. Dashboard");

                if (role.equals("ADMIN")) {
                    System.out.println("6. Add Movie");
                    System.out.println("7. Delete Movie");
                    System.out.println("8. Update Movie");
                    System.out.println("9. Logout");
                } else {
                    System.out.println("6. Logout");
                }

                int choice;

                try {
                    choice = sc.nextInt();
                    sc.nextLine();
                } catch (Exception e) {
                    System.out.println(" Invalid input! Enter numbers only.");
                    sc.nextLine();
                    continue;
                }

                try {
                    switch (choice) {

                        case 1:
                            service.getAllMovies().forEach(System.out::println);
                            break;

                        case 2:
                            System.out.print("Enter movie title: ");
                            String title = sc.nextLine();
                            System.out.println(service.searchByTitle(title));
                            break;

                        case 3:
                            List<String> genres = service.getAllGenres();

                            for (int i = 0; i < genres.size(); i++) {
                                System.out.println((i + 1) + ". " + genres.get(i));
                            }

                            System.out.print("Select genre number: ");
                            int g = sc.nextInt();
                            sc.nextLine();

                            if (g >= 1 && g <= genres.size()) {
                                service.filterByGenre(genres.get(g - 1))
                                        .forEach(System.out::println);
                            } else {
                                System.out.println(" Invalid genre choice!");
                            }
                            break;

                        case 4:
                            service.getTopRated(5).forEach(System.out::println);
                            break;

                        case 5:
                            if (role.equals("ADMIN")) {
                                service.showStats();
                            } else {
                                service.showUserDashboard();
                            }
                            break;

                        case 6:
                            if (role.equals("ADMIN")) {

                                System.out.print("Title: ");
                                String t = sc.nextLine();

                                System.out.print("Genre: ");
                                String ge = sc.nextLine();

                                System.out.print("Rating: ");
                                double r = sc.nextDouble();

                                System.out.print("Year: ");
                                int y = sc.nextInt();
                                sc.nextLine();

                                boolean added = service.addMovie(new Movie(t, ge, r, y));

                                if (!added) {
                                    throw new DuplicateMovieException("Movie already exists!");
                                }

                                System.out.println(" Movie added!");

                            } else {
                                System.out.println(" Logging out...");
                                break;
                            }
                            break;

                        case 7:
                            if (role.equals("ADMIN")) {
                                System.out.print("Enter title to delete: ");
                                String del = sc.nextLine();

                                if (service.deleteMovie(del)) {
                                    System.out.println(" Movie deleted!");
                                } else {
                                    System.out.println(" Movie not found!");
                                }
                            }
                            break;

                        case 8:
                            if (role.equals("ADMIN")) {
                                System.out.print("Old Title: ");
                                String oldT = sc.nextLine();

                                System.out.print("New Title: ");
                                String newT = sc.nextLine();

                                System.out.print("Genre: ");
                                String newG = sc.nextLine();

                                System.out.print("Rating: ");
                                double newR = sc.nextDouble();

                                System.out.print("Year: ");
                                int newY = sc.nextInt();
                                sc.nextLine();

                                boolean updated = service.updateMovie(
                                        oldT,
                                        new Movie(newT, newG, newR, newY)
                                );

                                if (updated) {
                                    System.out.println(" Movie updated!");
                                } else {
                                    System.out.println(" Update failed!");
                                }
                            }
                            break;

                        case 9:
                            if (role.equals("ADMIN")) {
                                System.out.println(" Logging out...");
                                break;
                            }

                        default:
                            System.out.println(" Invalid choice!");
                    }

                } catch (MovieNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (DuplicateMovieException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println(" Unexpected error: " + e.getMessage());
                }


                if ((role.equals("ADMIN") && choice == 9) ||
                        (role.equals("USER") && choice == 6)) {
                    break;
                }
            }
        }
    }
}