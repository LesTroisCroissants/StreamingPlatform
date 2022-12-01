package data;

import java.util.HashSet;
import java.util.List;

public interface DataAccess {
    HashSet<String> getCategories();
    List<String> getMovieInfo();
    List<String> getSeriesInfo();
}
