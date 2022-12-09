package data;

import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.*;

public class Data implements DataAccess {
    private final String dataPath = "StreamingPlatform/src/data/Data/";
    private final String moviePath = dataPath + "film.txt";
    private final String movieCoverImgPath = dataPath + "filmplakater/";
    private final String seriesPath = dataPath + "serier.txt";
    private final String seriesCoverImgPath = dataPath + "serieforsider/";


    private HashSet<String> categories = new HashSet<>();

    private ArrayList<String> movieInfo;
    private ArrayList<String> seriesInfo;

    public Data() throws FileNotFoundException{
        // Getting and formatting data from the files
        movieInfo = formatFileData(moviePath, movieCoverImgPath);
        seriesInfo = formatFileData(seriesPath, seriesCoverImgPath);

        loadCategories(movieInfo);
        loadCategories(seriesInfo);
    }

    private void loadCategories(ArrayList<String> info) {
        for (String line : info){
            String[] lineArray = line.split(";");
            String[] categories = lineArray[2].split(",");// 2 because the categories always are the third element in the line

            for (String category : categories) {
                this.categories.add(category.strip());
            }
        }
    }

    private ArrayList<String> loadFile(String filePath) throws FileNotFoundException {
        ArrayList<String> array = new ArrayList<>();

        File file = new File(filePath);
        Scanner myReader = new Scanner(file, "windows-1252");
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            array.add(data.strip());
        }
        myReader.close();
        return array;
    }


    private ArrayList<String> formatFileData(String path, String coverPath) throws FileNotFoundException{
        ArrayList<String> fileLines = loadFile(path);
        for (int i = 0; i < fileLines.size(); i++) {
            String mediaName = fileLines.get(i).split(";")[0];
            fileLines.set(i, fileLines.get(i) + coverPath + mediaName + ".jpg;");
        }
        return fileLines;
    }

    /**
     * Returns all possible categories
     */
    @Override
    public HashSet<String> getCategories() {
        return categories;
    }

    /**
     * Returns an ArrayList<String> with all the lines about movies
     */
    @Override
    public List<String> getMovieInfo() {
        return movieInfo;
    }

    /**
     * Returns an ArrayList<String> with all the lines about series
     */
    @Override
    public List<String> getSeriesInfo() {
        return seriesInfo;
    }

}
