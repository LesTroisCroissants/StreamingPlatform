package domain;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MediaRegistryTests {

    private MediaRegistry mediaRegistry;


    @Test
    public void testFavoriteAddingAndChecking(){
        mediaRegistry.setFavorite(mediaRegistry.getAllMedia().get(0), true);
        mediaRegistry.setFavorite(mediaRegistry.getAllMedia().get(1), true);
        mediaRegistry.setFavorite(mediaRegistry.getAllMedia().get(0), false);

        assertEquals(mediaRegistry.getAllMedia().get(1), mediaRegistry.getFavorites().get(0));
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
