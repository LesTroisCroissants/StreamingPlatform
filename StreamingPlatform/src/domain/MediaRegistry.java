package domain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import data.DataAccess;
import data.Data;

import javax.imageio.ImageIO;

public class MediaRegistry implements MediaInfo {
    private DataAccess data;
    private List<Media> movies;
    private List<Media> series;
    private Set<String> categories;

    public MediaRegistry() throws FileNotFoundException {
        movies = new ArrayList<>();
        series = new ArrayList<>();
        initialize();

    }

    private void initialize() throws FileNotFoundException {
        data = new Data();

        for (String movieInfo : data.getMovieInfo()) {
            try {
                movies.add(createMovie(movieInfo));
            } catch (IOException e) {
                System.out.println("Image path could not be resolved for movie");
            }
        }

        for (String seriesInfo : data.getSeriesInfo()) {
            try {
                series.add(createSeries(seriesInfo));
            } catch (IOException e) {
                System.out.println("Image path could not be resolved for series");
            }
        }

        loadFavorites();
        categories = data.getCategories();
    }

    private Movie createMovie(String data) throws IOException {
        // 0: Title, 1: release year, 2: categories, 3: rating, 4: cover path
        String[] properties = data.trim().split(";");

        // Iterate movies
        return new Movie(
                properties[0].trim(),
                Integer.parseInt(properties[1].trim()),
                parseCategories(properties[2]),
                parseRating(properties[3]),
                getCoverImage(properties[4]),
                ""
        );
    }

    private Series createSeries(String data) throws IOException {
        // 0: Title, 1: release year, 2: category, 3: rating, 4: season-episode, 5: cover path
        String[] properties = data.trim().split(";");

        List<Season> seasons = new ArrayList<>();

        String[] seasonsInfo = properties[4].trim().split(",");
        for (String season : seasonsInfo) {
            seasons.add(generateSeason(season));
        }

        String[] years = properties[1].trim().split("-");
        int yearFrom = Integer.parseInt(years[0]);
        int yearTo = years.length > 1 ? Integer.parseInt(years[1]) : 0;

        // Iterate movies
        return new Series(
                properties[0],
                yearFrom,
                yearTo,
                parseCategories(properties[2]),
                parseRating(properties[3]),
                getCoverImage(properties[5]),
                seasons
        );
    }

    private Season generateSeason(String seasonInfo) {
        int episodes = Integer.parseInt(seasonInfo.split("-")[1]);

        return new Season(
                generateEpisodes(episodes)
        );
    }

    private List<Episode> generateEpisodes(int amount) {
        List<Episode> episodes = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            episodes.add(new Episode(""));
        }

        return episodes;
    };

    private Set<String> parseCategories(String rawCategories) {
        Set<String> categories = new HashSet<>();
        for (String category : rawCategories.toLowerCase().split(",")) {
            categories.add(category.trim());
        }
        return categories;
    }

    private BufferedImage getCoverImage(String path) throws IOException {
        File coverPath = new File(path);
        return ImageIO.read(coverPath);
    }

    private Double parseRating(String rawRating) {
        return Double.parseDouble(rawRating.replace(",","."));
    }

    /**
     * Marks all media registered as a favorite in the data as a favorite in the object
     */
    private void loadFavorites(){
        List<String> favorites = data.getFavorites();
        for (Media m : getAllMedia())
            if (favorites.contains(m.getTitle()))
                m.setFavorite(true);
    }

    /**
     * Takes a search argument and returns a list of media.
     * Sorted by most relevant by title to relevant by category.
     */
    @Override
    public List<Media> search(String input, List<Media> media) {
        if (input.length() == 0) return media;
        List<Media> resultsTitle = media.stream()
                .filter(x -> x.getTitle()
                        .toLowerCase()
                        .contains(input.toLowerCase()))
                .toList();

        List<Media> results = new ArrayList<>(resultsTitle);

        List<Media> resultsCategory = filter(input, media).stream()
                .filter(x -> !results.contains(x))
                .toList();
        results.addAll(resultsCategory);

        return results;
    }

    @Override
    public List<Media> getAllMedia() {
        List<Media> allMedia = new ArrayList<>();
        allMedia.addAll(movies);
        allMedia.addAll(series);
        return allMedia;
    }

    /**
     * Filter by category
     */
    @Override
    public List<Media> filter(String category, List<Media> media) {
        return new ArrayList<>(getAllMedia().stream().filter(x -> x.getCategories().contains(category.toLowerCase())).toList());
    }

    /**
     * Sort by year
     */
    @Override
    public List<Media> sortYear(List<Media> media, boolean ascending) {
        for (int i = 0; i < media.size(); i++) {
            for (int j = i + 1; j < media.size(); j++) {
                Media media1 = media.get(i);
                Media media2 = media.get(j);
                if (media1.getYear() > media2.getYear() && ascending
                        || media1.getYear() < media2.getYear() && !ascending) {
                    media.set(i, media2);
                    media.set(j, media1);
                }
            }
        }
        return media;
    }

    /**
     * Sort by rating
     */
    @Override
    public List<Media> sortRating(List<Media> media, boolean ascending) {
        for (int i = 0; i < media.size(); i++) {
            for (int j = i + 1; j < media.size(); j++) {
                Media media1 = media.get(i);
                Media media2 = media.get(j);
                if (media1.getRating() > media2.getRating() && ascending
                        || media1.getRating() < media2.getRating() && !ascending) {
                    media.set(i, media2);
                    media.set(j, media1);
                }
            }
        }
        return media;
    }

    /**
     * Marks a given media as favorite
     */
    @Override
    public void setFavorite(Media media, boolean shouldBeFavorite) {
        media.setFavorite(shouldBeFavorite);
    }

    /**
     * Sends the list of favorites to the data layer for saving
     */
    @Override
    public void saveFavorites() throws FileNotFoundException{
        List<String> favorites = new ArrayList<>();
        for (Media m : getFavorites())
            favorites.add(m.getTitle());
        data.saveFavorites(favorites);
    }

    /**
     * Returns all favorites
     */
    @Override
    public List<Media> getFavorites(){
        List<Media> favorites = new ArrayList<>();
        for (Media m : getAllMedia())
            if (m.isFavorite())
                favorites.add(m);
        return favorites;
    }

    @Override
    public List<Media> getMovies() {
        return movies;
    }

    @Override
    public List<Media> getSeries() {
        return series;
    }

    @Override
    public Set<String> getCategories() {
        return categories;
    }
    
}
