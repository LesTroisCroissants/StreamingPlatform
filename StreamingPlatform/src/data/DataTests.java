package data;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataTests {
	private Data data;

	private final String dataPath = "StreamingPlatform/src/data/Data/";
	private final String moviePath = dataPath + "film.txt";
	private final String seriesPath = dataPath + "serier.txt";

	
	@Test
	public void testGetCategories(){
		String[] movieCategories = new String[] {"Crime", "Drama", "Biography", "Sport", "History", "Romance", "War", "Mystery", "Adventure", "Family", "Fantasy", "Thriller", "Horror", "Film-Noir", "Action", "Sci-fi", "Comedy" , "Musical", "Western", "Music"};
		String[] seriesCategories = new String[] {"Talk-show", "Documentary", "Crime", "Drama", "Action", "Adventure", "Drama", "Comedy", "Fantasy", "Animation", "Horror", "Sci-fi", "War", "Thriller", "Mystery", "Biography", "History", "Family", "Western", "Romance", "Sport"};

		HashSet<String> allCategories = new HashSet<>();

		Collections.addAll(allCategories, movieCategories);
		Collections.addAll(allCategories, seriesCategories);

		assertEquals(allCategories, data.getCategories());
	}

	@Test
	public void testGetMovieInfo(){
		List<String> movieInfo = data.getMovieInfo();

		int formalSemicolonAmount = 5;
		int actualSemicolonAmount = movieInfo.get(0).split(";").length;
		assertEquals(formalSemicolonAmount, actualSemicolonAmount);

		String firstElement = "The Godfather; 1972; Crime, Drama; 9,2;StreamingPlatform/src/data/Data/filmplakater/The Godfather.jpg;";
		assertEquals(firstElement, movieInfo.get(0));

		String lastElement = "Yankee Doodle Dandy; 1942; Biography, Drama, Musical; 7,7;" + "StreamingPlatform/src/data/Data/filmplakater/" + "Yankee Doodle Dandy.jpg;";
		assertEquals(lastElement, movieInfo.get(movieInfo.size() - 1));
	}

	@Test
	public void testGetSeriesInfo(){
		List<String> seriesInfo = data.getSeriesInfo();

		int formalSemicolonAmount = 6;
		int actualSemicolonAmount = seriesInfo.get(0).split(";").length;
		assertEquals(formalSemicolonAmount, actualSemicolonAmount);

		String firstElement = "Twin Peaks; 1990-1991; Crime, Drama, Mystery; 8,8; 1-8, 2-22;StreamingPlatform/src/data/Data/serieforsider/Twin Peaks.jpg;";
		assertEquals(firstElement, seriesInfo.get(0));

		String lastElement = "Dexter; 2006-2013; Crime, Drama, Mystery; 8,7; 1-12, 2-12, 3-12, 4-12, 5-12, 6-12, 7-12,8-12;" + "StreamingPlatform/src/data/Data/serieforsider/" + "Dexter.jpg;";
		assertEquals(lastElement, seriesInfo.get(seriesInfo.size() - 1));
	}

	@Test
	public void testSavingAndReadingFavorites(){
		try {
			List<String> favoritesSave = new ArrayList<>(List.of("Totoro", "Nausicäa", "Princess Mononoke", "Spirited Away"));

			data.saveFavorites(favoritesSave);
			List<String> favoriteGet = data.getFavorites();

			assertEquals(favoritesSave, favoriteGet);

		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Before
	public void setup(){
		try {
			data = new Data();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	@After
	public void tearDown(){
		data = null;
	}
}

