import java.io.*;
import java.util.*;

public class MovieLoader {
    public static Movie[] loadMovies(String filename) {
        Movie[] movies = new Movie[3];
        try (Scanner sc = new Scanner(new File(filename))) {
            for (int i = 0; i < movies.length; i++) {
                if (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");
                    String title = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    movies[i] = new Movie(title, price);
                }
            }
        } catch (Exception e) {
            System.out.println("Error baca file: " + e.getMessage());
        }
        return movies;
    }

    public static void saveMovies(Movie[] movies, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Movie m : movies) {
                pw.println(m.getTitle() + "," + m.getBasePrice());
            }
        } catch (IOException e) {
            System.out.println("Error simpan file: " + e.getMessage());
        }
    }
}