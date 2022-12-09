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
                Integer.parseInt(properties[1].trim()),
                parseCategories(properties[2]),
                parseRating(properties[3]),
                getCoverImage(properties[4]),
                "",
                false // TODO check if movie is favorited
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

        String[] years = properties[1].split("-");
        int yearFrom = Integer.parseInt(years[0]);
        int yearTo = years.length > 1 ? Integer.parseInt(properties[1]) : 0;

        // Iterate movies
        return new Series(
                properties[0],
                yearFrom,
                yearTo,
                parseCategories(properties[2]),
                parseRating(properties[3]),
                getCoverImage(properties[4]),
                seasons,
                false // TODO check if movie is favorited
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

    /**
     * Takes a search argument and returns a list of media.
     * Sorted by most relevant by title to relevant by category.
     */
    @Override
    public List<Media> search(String input) {
        List<Media> allMedia = getAllMedia();

        List<Media> resultsTitle = allMedia.stream()
                .filter(x -> x.getTitle()
                        .toLowerCase()
                        .contains(input.toLowerCase()))
                .toList();

        List<Media> results = new ArrayList<>(resultsTitle);

        List<Media> resultsCategory = allMedia.stream()
                .filter(x -> x.getCategories()
                        .contains(input) && !results.contains(x))
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
    public List<Media> filter(String category) {
        return getAllMedia().stream().filter(x -> x.getCategories().contains(category)).toList();
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
