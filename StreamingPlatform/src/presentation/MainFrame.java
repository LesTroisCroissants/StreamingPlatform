package presentation;

// Importing required classes
import domain.Media;
import domain.MediaRegistry;

import java.awt.*;
import java.io.FileNotFoundException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.util.List;

public class MainFrame extends JFrame {
        static JFrame frame;
        static MediaRegistry mediaRegistry;
        static List<Media> media;
        static List<Media> selectedMedia;

        // Panels
        static JMenuBar menuBar;
        static JScrollPane mainPanel;

        // Top panel content
        static JTextField searchBar;

        // Bottom panel content
        static JButton buttonHome;
        static JButton buttonMovies;
        static JButton buttonSeries;
        static JButton buttonFavorites;

        // Sorting
        static boolean sortByYear;
        static boolean sortByRating;
        static boolean order;


        public static void init(){
            sortByRating = false;
            sortByYear = false;
            try {
                mediaRegistry = new MediaRegistry();
                setMedia(mediaRegistry.getAllMedia());
                setSelectedMedia(media);
            } catch (FileNotFoundException e){ // TODO handle this with a popup
                System.out.println("Hej, you error! :)");
            }

            frame = new JFrame("Pastryeam");
        }

        public static void main(String[] args) {
            init();

            frame.setJMenuBar(createMenuBar());

            mainPanel = createMainPanel();
            frame.add(mainPanel);

            // Frame settings
            frame.setSize(1280, 720);
            frame.setMaximumSize(new Dimension(1280, 720));
            frame.setVisible(true);
            frame.requestFocusInWindow();
        }
        
        public static JMenuBar createMenuBar(){
            menuBar = new JMenuBar();

            JPanel topPanel = createTopPanel();
            JPanel bottomPanel = createBottomPanel();

            menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.Y_AXIS));

            menuBar.add(topPanel);
            menuBar.add(bottomPanel);

            return menuBar;
        }

        public static JPanel createTopPanel(){
            JPanel panel = new JPanel();

            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

            // TODO should be replaced with logo
            JLabel nameLabel = new JLabel("Pastryeam");
            panel.add(nameLabel, BorderLayout.LINE_START);

            // search bar
            searchBar = new JTextField();
            searchBar.setPreferredSize(new Dimension(120, 20));
            panel.add(searchBar, BorderLayout.LINE_END);
            searchBar.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { search(); }
                public void removeUpdate(DocumentEvent e) { search(); }
                public void changedUpdate(DocumentEvent e) {}
            });

            // Debugging color
            panel.setBackground(Color.cyan);

            return panel;
        }

        public static JPanel createBottomPanel(){
            JPanel panel = new JPanel();

            // Creating buttons
            buttonHome = new JButton("Home");
            buttonMovies = new JButton("Movies");
            buttonSeries = new JButton("Series");
            buttonFavorites = new JButton("Favorites");

            // Add event listeners
            buttonHome.addActionListener((e) -> showAll());
            buttonMovies.addActionListener((e) -> showMovies());
            buttonSeries.addActionListener((e) -> showSeries());

            // Adding buttons
            panel.add(buttonHome);
            panel.add(buttonMovies);
            panel.add(buttonSeries);
            panel.add(buttonFavorites);

            // add spacing
            panel.add(new JToolBar.Separator(new Dimension(50, 0)));

            // Adding label
            JLabel year = new JLabel("By year");
            JButton yearAsc = new JButton("↑");
            JButton yearDes = new JButton("↓");

            yearAsc.addActionListener((e) -> setSortByYear(true));
            yearDes.addActionListener((e) -> setSortByYear(false));

            JLabel rating = new JLabel("By rating");
            JButton ratingAsc = new JButton("↑");
            JButton ratingDes = new JButton("↓");

            ratingAsc.addActionListener((e) -> setSortByRating(true));
            ratingDes.addActionListener((e) -> setSortByRating(false));

            panel.add(year);
            panel.add(yearAsc);
            panel.add(yearDes);

            panel.add(rating);
            panel.add(ratingAsc);
            panel.add(ratingDes);

            // Debugging color
            panel.setBackground(Color.green);

            return panel;
        }

        public static JScrollPane createMainPanel(){
            // TODO Load media into this view

            JPanel content = new JPanel();
            content.setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weighty = 1.0;
            constraints.weightx = 1.0;
            constraints.ipadx = 15;
            constraints.ipady = 15;
            constraints.anchor = media.size() < 17 ? GridBagConstraints.FIRST_LINE_START : GridBagConstraints.CENTER;

            JScrollPane panel = new JScrollPane(content);
            panel.getVerticalScrollBar().setUnitIncrement(10);
            panel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            content.setBackground(Color.darkGray);

            int counter = 0;
            for (Media m : media) {
                constraints.gridy = counter / 8;

                content.add(new JLabel(new ImageIcon(m.getCoverImage())), constraints);
                counter++;
            }

             // TODO find prettier way to add empty space in the grid
            int empty = 8 - (counter + 1 % 8) + 1;
            constraints.weightx = 3.0;
            for (int i = 0; i < empty; i++) {
                content.add(new JLabel(""), constraints);
            }

            return panel;
        }

        static void setMedia (List<Media> m) {
            media = sort(m);
        }

        static void setSelectedMedia (List<Media> m) {
            selectedMedia = sort(m);
        }

        static void setSortByYear(boolean o) {
            order = o;
            sortByYear = true;
            sortByRating = false;
            setMedia(media);
            updateContent();
        }

        static void setSortByRating(boolean o) {
            order = o;
            sortByRating = true;
            sortByYear = false;
            setMedia(media);
            updateContent();
        }

        static List<Media> sort(List<Media> m) {
            if (sortByRating) return mediaRegistry.sortRating(m, order);
            if (sortByYear) return mediaRegistry.sortYear(m, order);
            return m;
        }

        static void search () {
            setMedia(mediaRegistry.search(searchBar.getText(), selectedMedia));
            updateContent();
        }

        static void showAll() {
            setMedia(mediaRegistry.getAllMedia());
            setSelectedMedia(media);
            search();
            // TODO: Add styling to buttons on click
            // buttonHome.setBackground(Color.darkGray);
            // updateBar();
            updateContent();
        }

        static void showMovies () {
            setMedia(mediaRegistry.getMovies());
            setSelectedMedia(media);
            search();
            // buttonMovies.setBackground(Color.darkGray);
            // updateBar();
            updateContent();
        }

        static void showSeries () {
            setMedia(mediaRegistry.getSeries());
            setSelectedMedia(media);
            search();
            // buttonSeries.setBackground(Color.darkGray);
            // updateBar();
            updateContent();
        }

        static void updateContent() {
            frame.remove(mainPanel);
            mainPanel = createMainPanel();
            frame.add(mainPanel);
            frame.revalidate();
        }

        static void updateBar() {
            frame.remove(mainPanel);
            frame.remove(menuBar);
            menuBar = createMenuBar();
            frame.add(menuBar);
            updateContent();
        }
    }
