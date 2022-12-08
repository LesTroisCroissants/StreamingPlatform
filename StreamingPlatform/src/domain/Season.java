package domain;

import java.util.List;

/**
 * Responsible for containing the Episode objects of a season.
 */


public class Season {

    private final List<Episode> episodes;


    public Season(List<Episode> episodes){
        this.episodes = episodes;
    }

    public List<Episode> getEpisodes(){
        return episodes;
    }

}
