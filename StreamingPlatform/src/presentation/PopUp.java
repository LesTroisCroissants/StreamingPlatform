package presentation;

import domain.Media;
import domain.MediaRegistry;
import domain.Season;
import domain.Series;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class PopUp {

    public static void main(String[] args) throws FileNotFoundException {
        JFrame frame = new JFrame();
        frame.setSize(1280, 720);
        frame.setMaximumSize(new Dimension(1280, 720));

        MediaRegistry mediaRegistry = new MediaRegistry();
        Media m = mediaRegistry.getSeries().get(0);
        frame.add(create(mediaRegistry, m));

        frame.setVisible(true);
    }

    public static JPanel create(MediaRegistry mediaRegistry, Media media) {
        JPanel container = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.weightx = 1.0;
        JLabel cover = new JLabel(
                new ImageIcon(media.getCoverImage().getScaledInstance(210, 314, Image.SCALE_SMOOTH))
        );
        container.add(cover, c);

        c.weightx = 2.0;
        JPanel info = new JPanel(new BorderLayout());

        JPanel topInfo = new JPanel();
        JLabel categories = new JLabel(media.getCategories().toString());
        topInfo.add(categories);
        info.add(topInfo, BorderLayout.PAGE_START);
        container.add(info, c);

        if (media instanceof Series) {
            JPanel bottomInfo = new JPanel();
            JComboBox<String> seasons = new JComboBox<>();
            for (int i = 0; i < ((Series) media).getSeasons().size(); i++) {
                seasons.addItem("Season " + (i + 1));
            }
            bottomInfo.add(seasons);

            info.add(bottomInfo);
        }

        container.setVisible(true);

        return container;
    }
}
