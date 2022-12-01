package domain;

import java.util.List;
import java.util.Set;

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
    public List<Media> filter(String category) {
        // Implement filter function
        return null;
    }

    @Override
    public void initialize() {
        // Get data from data-layer
        
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
