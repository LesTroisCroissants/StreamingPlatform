package presentation;

import javax.swing.*;
import java.awt.*;

public class MenuBarUI extends JFrame {
    JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.Y_AXIS));

        // topPanel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // TODO should be replaced with logo
        JLabel nameLabel = new JLabel("Pastryeam");

        topPanel.add(nameLabel, BorderLayout.LINE_START);

        // search bar
        JTextField searchBar = new JTextField();
        searchBar.setPreferredSize(new Dimension(120, 20));
        topPanel.add(searchBar, BorderLayout.LINE_END);


        // --------------------- Shift to other panel --------------------------

        // bottomPanel
        JPanel bottomPanel = new JPanel();

        // Adding label
        JLabel flopLabel = new JLabel("Flop");
        bottomPanel.add(flopLabel);

        // Creating buttons
        JButton buttonMovies = new JButton("Movies");
        JButton buttonSeries = new JButton("Series");
        JButton buttonFavorites = new JButton("Favorites");

        // Adding buttons
        bottomPanel.add(buttonMovies);
        bottomPanel.add(buttonSeries);
        bottomPanel.add(buttonFavorites);

        // add spacing
        bottomPanel.add(new JToolBar.Separator(new Dimension(50, 0)));

        // Debugging colors
        topPanel.setBackground(Color.cyan);
        bottomPanel.setBackground(Color.green);

        menuBar.add(topPanel);
        menuBar.add(bottomPanel);

        return menuBar;
    }
}
