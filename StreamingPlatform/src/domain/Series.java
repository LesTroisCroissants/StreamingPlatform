package domain;

import java.awt.*;
import java.util.List;
import java.util.Set;


/**
 * Responsible for containing the Season objects of a series
 */

public class Series extends Media{

    private final int endYear;
    private final List<Season> seasons;

    public Series(String title, int startYear, int endYear, Set<String> categories, double rating, Image coverImage, List<Season> seasons, boolean favorite){
        super(title, startYear, categories, rating, coverImage, favorite);

        this.endYear = endYear;
        this.seasons = seasons;
    }



    public int getEndYear() {
        return endYear;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

}
