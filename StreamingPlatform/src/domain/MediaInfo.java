package domain;

import java.io.FileNotFoundException;
import java.util.List;

public interface MediaInfo {
    List<Media> search(String input);
    List<Media> filter(String category);
    List<Media> sortRating(List<Media> media, boolean ascending);
    List<Media> sortYear(List<Media> media, boolean ascending);
    List<Media> getMovies();
    List<Media> getSeries();
    List<Media> getAllMedia();
}
