package domain;

import java.util.List;
import java.util.Set;

public interface MediaInfo {
    List<Media> search(String input, List<Media> media);
    List<Media> filter(String category);
    List<Media> sortRating(List<Media> media, boolean ascending);
    List<Media> sortYear(List<Media> media, boolean ascending);
    List<Media> getMovies();
    List<Media> getSeries();
    Set<String> getCategories();
    List<Media> getAllMedia();
    void setFavorite(Media media, boolean shouldBeFavorite);
    void saveFavorites();
    List<Media> getFavorites();
}
