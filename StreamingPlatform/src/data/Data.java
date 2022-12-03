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
    private String seriesPath = dataPath + "serier.txt";
    private String seriesCoverImgPath = "StreamingPlatform/src/data/Data/serieforsider/";


    private HashSet<String> categorySet = new HashSet<>();

    private ArrayList<String> movieInfo;
    private ArrayList<String> seriesInfo;

    public Data() throws FileNotFoundException{
        // Getting and formatting data from the files
        movieInfo = formatFileData(moviePath);
        seriesInfo = formatFileData(seriesPath);

        HashSet<ArrayList<String>> fileLists = new HashSet<>();
        fileLists.add(movieInfo);
        fileLists.add(seriesInfo);

        loadCategoriesToHashSet(fileLists);
    }

    // TODO Check that this function gives the correct categories
    private void loadCategoriesToHashSet(HashSet<ArrayList<String>> dataArrays) {
        for (ArrayList<String> lines : dataArrays){
            for (String line : lines){
                String[] lineArray = line.split(";");
                String[] categories = lineArray[2].split(",");// 2 because the categories always are the third element in the line

                for (String category : categories) {
                    categorySet.add(category.strip());
                }
            }
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

    // TODO check that this returns something for serier.txt
    private ArrayList<String> formatFileData(String path) throws FileNotFoundException{
        String coverPath;

        if (path.equals(moviePath)){
            // titel; årstal; kategori1, kategori2, ..; rating
            coverPath = movieCoverImgPath;
        } else if (path.equals(seriesPath)) {
            // titel; årtalFra-årstalTil; kategori1, kategori2, ..; rating; sæsonnummer-antalEpisoder, sæsonnummer-antalEpisoder...;
            coverPath = seriesCoverImgPath;
        } else {
            System.out.println("An incorrect path was specified to format the data!");
            return null;
        }

        ArrayList<String> fileLines = loadFileToArray(path);
        for (int i = 0; i < fileLines.size(); i++) {
            String mediaName = fileLines.get(i).split(";")[0];
            fileLines.set(i, fileLines.get(i) + coverPath + mediaName + ".png;");
        }
        return fileLines;
    }

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
        return seriesInfo;
    }

}
