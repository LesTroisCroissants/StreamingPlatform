package data;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataTests {
	private Data data;

	@Test
	public void TestReadFile(){
		data = new Data();
		String str = data.readMovies();
	}

	@Before
	public void Setup(){

	}

	@After
	public void tearDown(){

	}
}

