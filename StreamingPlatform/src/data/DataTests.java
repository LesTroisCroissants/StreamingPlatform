package data;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataTests {
	private Data data;

	private String dataPath = "StreamingPlatform/src/data/Data/";
	private String moviePath = dataPath + "film.txt";
	private String seriesPath = "serier.txt";

	
	@Test
	public void TestCategorySet(){
		try {
			data = new Data();
			System.out.println(data.getCategories().toString());
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void TestGetMovieInfo(){
		try {
			data = new Data();
			System.out.println(data.getMovieInfo().toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Before
	public void Setup(){

	}

	@After
	public void tearDown(){

	}
}

