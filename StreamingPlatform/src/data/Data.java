package data;

import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner; // Import the Scanner class to read text files

public class Data implements DataAccess {
    private String dataPath = "StreamingPlatform/src/data/Data/";
    private String moviePath = dataPath + "film.txt";
    private String movieCoverImgPath = "StreamingPlatform/src/data/Data/filmplakater/";
    private String seriesPath = "serier.txt";
    private String seriesCoverImgPath = "StreamingPlatform/src/data/Data/serieforsider/";


    private String categoriesString = "Talk-show, Documentary, Crime, Drama, Action, Adventure, Drama, Comedy, Fantasy, Animation, Horror, Sci-fi, War, Thriller, Mystery, Biography, History, Family, Western, Romance, Sport";
    private HashSet<String> categorySet = new HashSet<>();

    private ArrayList<String> movieInfo = new ArrayList<>();

    public Data() throws FileNotFoundException{
        stringToHashSet();
        movieInfo = formatMovieData();
    }

    private void stringToHashSet() {
        String[] CategoryArray = categoriesString.split(", ");
        for (String string : CategoryArray) {
            categorySet.add(string);
        }
    }

    /**
     * hello!
     */
    private ArrayList<String> loadFileToArray(String filePath) throws FileNotFoundException {
        ArrayList<String> array = new ArrayList<>();

        File file = new File(filePath);
        Scanner myReader = new Scanner(file);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            array.add(data.strip());
        }
        myReader.close();
        return array;
    }

    private ArrayList<String> formatMovieData() throws FileNotFoundException{
        // titel; årstal; kategori1, kategori2, ..; rating
        ArrayList<String> fileLines = loadFileToArray(moviePath);

        for (int i = 0; i < fileLines.size(); i++) {
            String movieName = fileLines.get(i).split(";")[0];
            fileLines.set(i, fileLines.get(i) + movieCoverImgPath + movieName + ".png;");

        }
        return fileLines;
    }

//    String readSeries() {
//        // titel; årtalFra-årstalTil; kategori1, kategori2, ..; rating; sæsonnummer-antalEpisoder, sæsonnummer-antalEpisoder...;
//        loadFileToArray(seriesPath);
//        return null;
//    }

    @Override
    public HashSet<String> getCategories() {
        return categorySet;
    }

    @Override
    public List<String> getMovieInfo() {
        return movieInfo;
    }

    @Override
    public List<String> getSeriesInfo() {
        // TODO Auto-generated method stub
        return null;
    }

}
