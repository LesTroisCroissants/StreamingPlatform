package presentation;

import domain.Media;
import domain.MediaRegistry;
import domain.Season;
import domain.Series;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;

public class PopUp {

    static JFrame frame;
    static JPanel content;
    static JComboBox<String> seasons;
    static Media media;

    public static void main(String[] args) throws FileNotFoundException {
        frame = new JFrame();
        frame.setSize(1280, 720);
        frame.setMaximumSize(new Dimension(1280, 720));

        MediaRegistry mediaRegistry = new MediaRegistry();
        media = mediaRegistry.getSeries().get(3);
        if (media instanceof Series) populateSeasons();
        content = create();
        frame.add(content);

        frame.setVisible(true);
    }

    public static JPanel create() {
        JPanel container = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.weightx = 1.0;
        container.add(cover(), c);

        c.weightx = 3.0;
        container.add(info(), c);

        return container;
    }

    public static void populateSeasons() {
        seasons = new JComboBox<>();
        for (int i = 0; i < ((Series) media).getSeasons().size(); i++) {
            seasons.addItem("Season " + (i + 1));
        }
        seasons.addActionListener(PopUp::selectSeason);
    }

    public static JLabel cover() {
        return new JLabel(
                new ImageIcon(media.getCoverImage().getScaledInstance(210, 314, Image.SCALE_SMOOTH))
        );
    }

    public static JPanel info() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel mainInfo = new JPanel();
        JLabel categories = new JLabel(media.getCategories().toString());
        mainInfo.add(categories);
        mainInfo.setBackground(Color.cyan);

        panel.add(mainInfo, BorderLayout.PAGE_START);

        if (media instanceof Series) {
            panel.add(seriesInfo());
        }

        return panel;
    }

    public static JPanel seriesInfo() {
        JPanel info = new JPanel();
        info.add(seasons, BorderLayout.LINE_START);

        JPanel episodes = new JPanel();

        int episodeCount = ((Series) media).getSeasons().get(seasons.getSelectedIndex()).getEpisodes().size();
        for (int i = 0; i < episodeCount; i++) {
            episodes.add(new JLabel("Episode " + (i+1)));
        }

        JScrollPane episodeView = new JScrollPane(episodes);
        info.add(episodeView, BorderLayout.PAGE_END);

        return info;
    }

    public static void selectSeason(ActionEvent e) {
        frame.remove(content);
        content = create();
        frame.add(content);
        frame.revalidate();
    }
}
