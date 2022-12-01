package domain;

import java.util.List;

public interface MediaInfo {
    List<Media> search(String input);
    List<Media> filter(String category);
    void initialize();
}
