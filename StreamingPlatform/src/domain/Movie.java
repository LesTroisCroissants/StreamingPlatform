package domain;

import java.awt.*;
import java.util.Set;


/**
 * Responsible for containing the actual playable movie.
 */

public class Movie extends Media implements Playable{

    private final String pathToFile;


    public Movie (String title, int year, Set<String> categories, double rating, Image coverImage, String pathToFile){
        super(title, year, categories, rating, coverImage);

        this.pathToFile = pathToFile;
    }


    @Override
    public String getPathToFile() {
        return pathToFile;
    }
}
