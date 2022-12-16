package presentation;

import domain.Media;
import domain.MediaInfo;
import domain.Series;

import javax.swing.*;
import java.awt.*;

public class PopUp extends JPanel {
    private final Media media;
    private final MediaInfo mediaRegistry;
    private JPanel info;
    private JComboBox<String> seasons;
    private JComboBox<String> episodes;
    private int selectedSeason = 0;
    private int selectedEpisode = 0;
    private int episodeAmount = 0;

    Color backgroundColor = new Color(80, 80, 80);

    public PopUp(Media media, MediaInfo mediaRegistry) {
        super();
        this.media = media;
        this.mediaRegistry = mediaRegistry;
        if (media instanceof Series) {
            episodeAmount = ((Series) media).getSeasons().get(selectedSeason).getEpisodes().size();
        }
        create();
    }

    private void create() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = getInfoConstraints();

        c.weightx = 1.0;
        JLabel leftContent = cover();

        // Negate invisible borders on cover JLabel
        leftContent.setBorder(BorderFactory.createEmptyBorder(0,-40,0,0));
        add(leftContent, c);

        c.weightx = 3.0;
        add(info(), c);

        setBackground(backgroundColor);
    }

    private JPanel info() {
        // Set up info with title
        info = new JPanel(new BorderLayout());
        info.setBackground(backgroundColor);
        info.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel title = new JLabel(media.getTitle());
        title.setFont(new Font("Sans Serif", Font.PLAIN, 30));
        title.setForeground(Color.white);
        info.add(title, BorderLayout.PAGE_START);

        // Content of main info
        JPanel mainInfo = new JPanel(new BorderLayout());
        mainInfo.setBackground(backgroundColor);

        // Info about media
        JPanel mediaInfo = new JPanel();
        mediaInfo.setLayout(new BoxLayout(mediaInfo, BoxLayout.Y_AXIS));
        mediaInfo.setBackground(backgroundColor);

        String categoriesFormatted = media.getCategories().toString().replaceAll("[^a-zA-Z, ]", "");
        JLabel categories = new JLabel("Categories: " + categoriesFormatted);
        categories.setForeground(Color.white);

        JLabel rating = new JLabel("Rating: " + media.getRating());
        rating.setForeground(Color.white);

        JLabel year = new JLabel("Released: " + media.getYear());
        year.setForeground(Color.white);

        JLabel endYear = new JLabel();
        endYear.setForeground(Color.white);
        if (media instanceof Series) endYear.setText("End year: " +
                (((Series) media).getEndYear() == 0 ? "still going!" :
                ((Series) media).getEndYear())
        );

        // Adding elements
        mediaInfo.add(Box.createRigidArea(new Dimension(0, 20)));
        mediaInfo.add(categories);
        mediaInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        mediaInfo.add(rating);
        mediaInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        mediaInfo.add(year);
        mediaInfo.add(Box.createRigidArea(new Dimension(0, 5)));


        if (media instanceof Series) {
            mediaInfo.add(endYear);
            mainInfo.add(seriesInfo(), BorderLayout.PAGE_END);
        }

        mainInfo.add(mediaInfo);
        info.add(mainInfo, BorderLayout.CENTER);

        JPanel actions = new JPanel();
        actions.setBackground(backgroundColor);

        JButton play = new JButton("Play");
        play.addActionListener(e -> JOptionPane.showConfirmDialog(
                MainFrame.frame, "This feature is yet to be implemented", "Not playable", JOptionPane.DEFAULT_OPTION
        ));
        actions.add(play);

        JButton favorite = new JButton(
                media.isFavorite() ? "Un-favorite" : "Favorite"
        );
        favorite.addActionListener(e -> favoriteMedia());
        actions.add(favorite);

        info.add(actions, BorderLayout.PAGE_END);

        return info;
    }

    private void populateSeasons() {
        seasons = new JComboBox<>();
        for (int i = 0; i < ((Series) media).getSeasons().size(); i++) {
            seasons.addItem("Season " + (i + 1));
        }
        seasons.setSelectedIndex(selectedSeason);
        seasons.addActionListener(e -> selectSeason());
    }

    private void populateEpisodes() {
        episodes = new JComboBox<>();
        for (int i = 0; i < episodeAmount; i++) {
            episodes.addItem("Episode " + (i+1));
        }
        episodes.setSelectedIndex(selectedEpisode);
        episodes.addActionListener(e -> selectEpisode());
    }

    private JLabel cover() {
        return new JLabel(
                // Image sized up to 1.5x size
                new ImageIcon(media.getCoverImage().getScaledInstance(210, 314, Image.SCALE_SMOOTH))
        );
    }


    private JPanel seriesInfo() {
        JPanel series = new JPanel();
        series.setBackground(backgroundColor);

        populateSeasons();
        populateEpisodes();
        series.add(seasons, BorderLayout.PAGE_START);

        series.add(episodes, BorderLayout.PAGE_END);

        return series;
    }

    private GridBagConstraints getInfoConstraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 3.0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        return c;
    }

    private void selectSeason() {
        selectedSeason = seasons.getSelectedIndex();
        selectedEpisode = 0;
        episodeAmount = ((Series) media).getSeasons().get(selectedSeason).getEpisodes().size();

        update();
    }

    private void selectEpisode() {
        selectedEpisode = episodes.getSelectedIndex();
    }

    private void favoriteMedia() {
        mediaRegistry.setFavorite(media, !media.isFavorite());
        update();
    }

    private void update() {
        remove(info);
        info = info();

        add(info, getInfoConstraints(),1);

        revalidate();
        repaint();
    }
}
