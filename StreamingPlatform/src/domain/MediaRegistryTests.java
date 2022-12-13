package domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MediaRegistryTests {

    private MediaRegistry mediaRegistry;


    @Test
    public void testMediaRegistryCreation(){
        assertFalse(mediaRegistry.getAllMedia().size() == 0);
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
    public void testSearch(){
        assertEquals(mediaRegistry.search(""), mediaRegistry.getAllMedia()); //tests empty search
        assertEquals(mediaRegistry.search("GiRLS"), mediaRegistry.search("girLs")); //tests case sensitivity
        assertTrue(mediaRegistry.search("Action").containsAll(mediaRegistry.filter("Action"))); //tests category search
        assertTrue(mediaRegistry.search("crImE").containsAll(mediaRegistry.filter("Crime"))); // tests case sensitivity in category search
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
