package domain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import data.Data;

import javax.imageio.ImageIO;

public class MediaRegistry implements MediaInfo {
    private List<Media> movies;
    private List<Media> series;
    private Set<String> categories;

    public MediaRegistry() throws FileNotFoundException {
        movies = new ArrayList<>();
        series = new ArrayList<>();
        initialize();

        if (movies.size() == 0
            && series.size() == 0) {
            throw new RuntimeException("Data could not be retrieved!");
        }
    }

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

    private void initialize() throws FileNotFoundException {
        Data data = new Data();

        for (String movieInfo : data.getMovieInfo()) {
            try {
                movies.add(createMovie(movieInfo));
            } catch (IOException e) {
                //System.out.println("Image path could not be resolved");
                System.out.println(e.getMessage());
            }
        }

        for (String seriesInfo : data.getSeriesInfo()) {
            try {
                series.add(createSeries(seriesInfo));
            } catch (IOException e) {
                //System.out.println("Image path could not be resolved");
                System.out.println(e.getMessage());
            }
        }

        categories = data.getCategories();
    }

    private Movie createMovie(String data) throws IOException {
        // 0: Title, 1: release year, 2: categories, 3: rating, 4: cover path
        String[] properties = data.trim().split(";");

        // Iterate movies
        return new Movie(
                properties[0].trim(),
                properties[1].trim(),
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

        // Iterate movies
        return new Series(
                properties[0],
                // TODO update to match Series should have from and to years
                properties[1],
                parseCategories(properties[2]),
                parseRating(properties[3]),
                getCoverImage(properties[4]),
                seasons
        );
    }

    private List<Episode> generateEpisodes(int amount) {
        // TODO add episode numbers and description?
        List<Episode> episodes = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            episodes.add(new Episode(""));
        }

        return episodes;
    };

    private Season generateSeason(String seasonInfo) {
        int episodes = Integer.parseInt(seasonInfo.split("-")[1]);

        return new Season(
                generateEpisodes(episodes)
        );
    }

    private Set<String> parseCategories(String property) {
        return Set.of(property.replaceAll(" ", "").split(","));
    }

    private BufferedImage getCoverImage(String path) throws IOException {
        File coverPath = new File(path);
        return ImageIO.read(coverPath);
    }

    private Double parseRating(String property) {
        return Double.parseDouble(property.replace(",","."));
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
