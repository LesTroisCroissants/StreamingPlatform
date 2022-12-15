package data;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataTests {
	private Data data;

	// this test needs updating whenever data is updated
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

		// Checks every element for semicolon amount
		int formalSemicolonAmount = 5;
		boolean mistakeObserved = false;
		for (String element : movieInfo){
			int actualSemicolonAmount = element.split(";").length;
			if (formalSemicolonAmount != actualSemicolonAmount) {
				mistakeObserved = true;
				break;
			}
		}
		assertFalse(mistakeObserved);

		// Gets the name from the start of the formatted string, and from the formatted path
		mistakeObserved = false;
		for (String element : movieInfo){
			if (!element.split(";")[4].contains(element.split(";")[0])){
				mistakeObserved = true;
				break;
			}
		}
		assertFalse(mistakeObserved);

		// tests if the path matches
		mistakeObserved = false;
		for (String element : movieInfo){
			if (!element.contains("StreamingPlatform/src/data/Data/filmplakater/")){
				mistakeObserved = true;
				break;
			}
		}
		assertFalse(mistakeObserved);
	}

	@Test
	public void testGetSeriesInfo(){
		List<String> seriesInfo = data.getSeriesInfo();

		// Checks every element for semicolon amount
		int formalSemicolonAmount = 6;
		boolean mistakeObserved = false;
		for (String element : seriesInfo){
			int actualSemicolonAmount = element.split(";").length;
			if (formalSemicolonAmount != actualSemicolonAmount) {
				mistakeObserved = true;
				break;
			}
		}
		assertFalse(mistakeObserved);

		// Gets the name from the start of the formatted string, and from the formatted path
		mistakeObserved = false;
		for (String element : seriesInfo){
			if (!element.split(";")[5].contains(element.split(";")[0])){
				mistakeObserved = true;
				break;
			}
		}
		assertFalse(mistakeObserved);

		// tests if the path matches
		mistakeObserved = false;
		for (String element : seriesInfo){
			if (!element.contains("StreamingPlatform/src/data/Data/serieforsider/")){
				mistakeObserved = true;
				break;
			}
		}
		assertFalse(mistakeObserved);

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

