package domain;

import java.awt.*;
import java.util.Set;

public abstract class Media {

    private final String title;
    private final int year;
    private final Set<String> categories;
    private final double rating;
    private final Image coverImage;
    private boolean favorite;


    public Media(String title, int year, Set<String> categories, double rating, Image coverImage, boolean favorite){
        this.title = title;
        this.year = year;
        this.categories = categories;
        this.rating = rating;
        this.coverImage = coverImage;
        this.favorite = favorite;
    }



    public String getTitle(){
        return title;
    }

    public int getYear(){
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

}
