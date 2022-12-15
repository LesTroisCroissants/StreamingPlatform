package domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MediaRegistryTests {

    private MediaRegistry mediaRegistry;


    @Test
    public void testMediaRegistryInitialization(){
        assertTrue(mediaRegistry.getAllMedia().size() > 0);
    }

    @Test
    public void testSortByRating(){
        //Tests ascending
        boolean mistakeObserved = false;

        double previousRating = 0;
        for (Media m : mediaRegistry.sortRating(mediaRegistry.getAllMedia(), true)){
            if (m.getRating() < previousRating)
                mistakeObserved = true;
            previousRating = m.getRating();
        }

        assertFalse(mistakeObserved);

        //Tests descending
        mistakeObserved = false;

        previousRating = Integer.MAX_VALUE;
        for (Media m : mediaRegistry.sortRating(mediaRegistry.getAllMedia(), false)){
            if (m.getRating() > previousRating)
                mistakeObserved = true;
            previousRating = m.getRating();
        }

        assertFalse(mistakeObserved);
    }

    @Test
    public void testSortByYear(){
        //Tests ascending
        boolean mistakeObserved = false;

        int previousYear = 0;
        for (Media m : mediaRegistry.sortYear(mediaRegistry.getAllMedia(), true)){
            if (m.getYear() < previousYear)
                mistakeObserved = true;
            previousYear = m.getYear();
        }

        assertFalse(mistakeObserved);

        //Tests descending
        mistakeObserved = false;

        previousYear = Integer.MAX_VALUE;
        for (Media m : mediaRegistry.sortYear(mediaRegistry.getAllMedia(), false)){
            if (m.getYear() > previousYear)
                mistakeObserved = true;
            previousYear = m.getYear();
        }

        assertFalse(mistakeObserved);
    }


    @Test
    public void testFilter(){
        boolean mistakeObserved = false;
        String testCategory = mediaRegistry.getCategories().toArray()[0].toString().toLowerCase(); // is made lower case to simulate category data on media objects
        for (Media m : mediaRegistry.filter(testCategory, mediaRegistry.getAllMedia())){
            if (!m.getCategories().contains(testCategory)) {
                mistakeObserved = true;
                break;
            }
        }

        assertFalse(mistakeObserved); // tests if any media returned does not contain the particular category
        assertTrue(mediaRegistry.filter(testCategory, mediaRegistry.getAllMedia()).size() > 0); // tests that the filtering is not empty
    }

    @Test
    public void testSearch(){
        List<Media> allMedia = mediaRegistry.getAllMedia();

        // tests empty search
        assertEquals(mediaRegistry.search("", allMedia), allMedia);

        // creates object for other tests
        Media testMedia = allMedia.get(0);
        String testCategory = testMedia.getCategories().toArray()[0].toString();

        // tests case sensitivity
        assertEquals(mediaRegistry.search(testMedia.getTitle().toLowerCase(), allMedia), mediaRegistry.search(testMedia.getTitle().toUpperCase(), allMedia));

        // tests category search
        assertTrue(mediaRegistry.search(testCategory, allMedia)
                .containsAll(mediaRegistry.filter(testCategory, allMedia)));

        // tests case sensitivity in category search
        assertTrue(mediaRegistry.search(testCategory.toUpperCase(), allMedia).containsAll(mediaRegistry.filter(testCategory, allMedia)));

        // tests filtered search
        assertTrue(mediaRegistry.search(testMedia.getTitle(), mediaRegistry.filter(testCategory, allMedia)).size() > 0);
        assertTrue(mediaRegistry.filter(testCategory, allMedia).containsAll(mediaRegistry.search(testMedia.getTitle(), mediaRegistry.filter(testCategory, allMedia))));
    }

    @Test
    public void testFavoriteAddingAndChecking(){
        mediaRegistry.setFavorite(mediaRegistry.getAllMedia().get(0), true);
        mediaRegistry.setFavorite(mediaRegistry.getAllMedia().get(1), true);
        mediaRegistry.setFavorite(mediaRegistry.getAllMedia().get(0), false);

        assertEquals(mediaRegistry.getAllMedia().get(1), mediaRegistry.getFavorites().get(0));
        assertFalse(mediaRegistry.getFavorites().contains(mediaRegistry.getAllMedia().get(0)));
    }

    @Before
    public void setup(){
        try {
            mediaRegistry = new MediaRegistry();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @After
    public void tearDown(){
        mediaRegistry = null;
    }

}
