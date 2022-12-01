package domain;

import java.awt.*;
import java.util.Set;

public abstract class Media {

    private final String title;
    private final String year;
    private final Set<String> categories;
    private final double rating;
    private final Image coverImage;


    public Media(String title, String year, Set<String> categories, double rating, Image coverImage){
        this.title = title;
        this.year = year;
        this.categories = categories;
        this.rating = rating;
        this.coverImage = coverImage;
    }



    public String getTitle(){
        return title;
    }

    public String getYear(){
        return year;
    }

    public Set<String> getCategories(){
        return categories;
    }

    public double getRating(){
        return rating;
    }

    public Image getCoverImage(){
        return coverImage;
    }

}
