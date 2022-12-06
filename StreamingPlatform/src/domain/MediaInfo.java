package domain;

import java.io.FileNotFoundException;
import java.util.List;

public interface MediaInfo {
    List<Media> search(String input);
    List<Media> filter(String category, int yearFrom, int yearTo, int rating);
}
