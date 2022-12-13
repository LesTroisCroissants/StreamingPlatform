package data;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;

public interface DataAccess {
    HashSet<String> getCategories();
    List<String> getMovieInfo();
    List<String> getSeriesInfo();
    List<String> getFavorites();
    void saveFavorites(List<String> favorites) throws FileNotFoundException;
}
