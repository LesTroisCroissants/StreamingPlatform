package domain;

/**
 * Responsible for containing the actual playable episode.
 */

public class Episode implements Playable{

    private final String pathToFile;


    public Episode (String pathToFile){
        this.pathToFile = pathToFile;
    }


    @Override
    public String getPathToFile() {
        return pathToFile;
    }
}
