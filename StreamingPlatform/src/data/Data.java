package data;

import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner; // Import the Scanner class to read text files

public class Data implements DataAccess {
	private String dataPath = "/Users/philip/Library/CloudStorage/OneDrive-ITU/uni/Semester 1/Grundlæggende Programmering/group_project/StreamingPlatform/StreamingPlatform/src/data/Data/";
	private String moviePath = dataPath + "film.txt";
	private String seriesPath = "serier.txt";

	private String testy = "StreamingPlatform/src/data/Data/film.txt";

	/**
	 * hello!
	 */
	void readFile(String filePath){
		try {
			File file = new File(filePath);
			Scanner myReader = new Scanner(file);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				System.out.println(data);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	String readMovies(){
		readFile(testy);

		return null;
	}

	@Override
	public HashSet<String> getCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getMovieInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSeriesInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
