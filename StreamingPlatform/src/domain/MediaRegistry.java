package domain;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

import data.Data;
import data.DataAccess;

public class MediaRegistry implements MediaInfo {
    private List<Media> movies;
    private List<Media> series;
    private Set<String> categories;

    @Override
    public List<Media> search(String input) {
        // Implement search function
        return null;
    }

    @Override
    public List<Media> filter(String category, int yearFrom, int yearTo, int rating) {
        // Implement filter function
        return null;
    }

    @Override
    public void initialize() throws FileNotFoundException {
        Data data = new Data();

    }

    private void populateMovies(List<String> rawData) {
        for (String movie : rawData) {
            // Title, release year, category, rating, cover path
            String[] properties = movie.split(";");
            // Iterate movies
        }
    }

    public List<Media> getMovies() {
        return movies;
    }

    public List<Media> getSeries() {
        return series;
    }

    public Set<String> getCategories() {
        return categories;
    }
    
}
