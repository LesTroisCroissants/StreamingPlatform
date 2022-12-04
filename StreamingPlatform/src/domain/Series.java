package domain;

import java.awt.*;
import java.util.List;
import java.util.Set;


/**
 * Responsible for containing the Season objects of a series
 */

public class Series extends Media{

    private final List<Season> seasons;


    public Series(String title, String year, Set<String> categories, double rating, Image coverImage, List<Season> seasons){
        super(title, year, categories, rating, coverImage);

        this.seasons = seasons;
    }

    public List<Season> getSeasons() {
        return seasons;
    }
}
