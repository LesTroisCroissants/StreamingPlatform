package domain;

import java.io.FileNotFoundException;
import java.util.List;

public interface MediaInfo {
    List<Media> search(String input);
    List<Media> filter(String category);
    List<Media> filter(double rating);
    List<Media> filter(int yearFrom, int yearTo);
    List<Movie> getMovies();
    List<Series> getSeries();
    List<Media> getAllMedia();
}
