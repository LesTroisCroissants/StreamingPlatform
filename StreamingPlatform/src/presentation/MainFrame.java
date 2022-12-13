package presentation;

// Importing required classes
import domain.Media;
import domain.MediaInfo;
import domain.MediaRegistry;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
        static JFrame frame;
        static MediaInfo mediaRegistry;
        static List<Media> selectedMedia; // Movies & Series || Movies || Series
        static List<Media> categorizedMedia;
        static List<Media> displayedMedia; // Shown on screen
        static String selectedCategory = "All categories";

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


        static Color backgroundColor = new Color(28,28,28);


        public static void init(){
            sortByRating = false;
            sortByYear = false;
            order = false;
            try {
                mediaRegistry = new MediaRegistry();
                setDisplayedMedia(mediaRegistry.getAllMedia());
                setSelectedMedia(mediaRegistry.getAllMedia());
                categorizedMedia = mediaRegistry.getAllMedia();
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


            try {
                BufferedImage logoImage = ImageIO.read(new File("StreamingPlatform/src/data/Data/pastryeam_logo.png"));
                JLabel logoLabel = new JLabel(new ImageIcon(logoImage.getScaledInstance(329,56, Image.SCALE_SMOOTH)));
                panel.add(logoLabel, BorderLayout.LINE_START);
            } catch (IOException e){
                System.out.println("Could not load logo!");
            }

            JPanel searchPanel = new JPanel(new BorderLayout());

            // search bar
            searchBar = new JTextField();
            searchBar.setBorder(new EmptyBorder(0,12,0,12));
            searchBar.setPreferredSize(new Dimension(300, 35));
            searchBar.setFont(new Font("Sans-Serif", Font.PLAIN,16));
            searchPanel.add(searchBar, BorderLayout.CENTER);
            searchPanel.setBorder(new EmptyBorder(14,0,14,0));
            searchPanel.setBackground(backgroundColor);

            searchBar.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { search(); }
                public void removeUpdate(DocumentEvent e) { search(); }
                public void changedUpdate(DocumentEvent e) {}
            });

            panel.add(searchPanel, BorderLayout.LINE_END);

            panel.setBackground(backgroundColor);
            // Debugging color
            // panel.setBackground(Color.cyan);

            return panel;
        }

        public static JPanel createBottomPanel(){
            JPanel panel = new JPanel();

            panel.setLayout(new BorderLayout());

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(backgroundColor);

            // Creating buttons
            buttonHome = new JButton("Home");
            buttonMovies = new JButton("Movies");
            buttonSeries = new JButton("Series");
            buttonFavorites = new JButton("Favorites");

            // Getting categories into dropdown
            List<String> categories = (mediaRegistry.getCategories().stream()).toList();
            JComboBox<String> dropDownCategories = new JComboBox<>();

            dropDownCategories.addItem("All categories");
            for (String category : mediaRegistry.getCategories()){
                dropDownCategories.addItem(category);
            }


            // Add event listeners
            buttonHome.addActionListener((e) -> showAll());
            buttonMovies.addActionListener((e) -> showMovies());
            buttonSeries.addActionListener((e) -> showSeries());

            dropDownCategories.addActionListener((e) -> setCategory(dropDownCategories.getSelectedItem().toString()));

            // Adding buttons to sub-panel
            buttonPanel.add(buttonHome);
            buttonPanel.add(buttonMovies);
            buttonPanel.add(buttonSeries);
            buttonPanel.add(buttonFavorites);

            // Adding the dropdown menu to sub-panel
            buttonPanel.add(dropDownCategories);


            // Sorting on the right side of the menubar
            JPanel sortPanel = new JPanel();
            sortPanel.setBackground(backgroundColor);
            // Adding label
            JLabel yearText = new JLabel("By year");
            yearText.setForeground(Color.white);
            JButton yearAsc = new JButton("-");

            JLabel ratingText = new JLabel("By rating");
            ratingText.setForeground(Color.white);
            JButton ratingAsc = new JButton("-");

            yearAsc.addActionListener((e) -> {
                setSortByYear();

                if(order){
                    yearAsc.setText("↑");
                } else {
                    yearAsc.setText("↓");
                }

                ratingAsc.setText("-");
            });

            ratingAsc.addActionListener((e) -> {
                setSortByRating();

                if(order){
                    ratingAsc.setText("↑");
                } else {
                    ratingAsc.setText("↓");
                }

                yearAsc.setText("-");
            });

            // Adding sorting visuals to the sub-panel
            sortPanel.add(yearText);
            sortPanel.add(yearAsc);

            sortPanel.add(ratingText);
            sortPanel.add(ratingAsc);


            // Add sub-panels and set the background color
            panel.add(buttonPanel, BorderLayout.LINE_START);
            panel.add(sortPanel, BorderLayout.LINE_END);
            panel.setBackground(backgroundColor);

            panel.setBorder(new EmptyBorder(0, 20, 0, 20));

            return panel;
        }


        public static JScrollPane createMainPanel(){
            JPanel content = new JPanel();
            content.setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weighty = 1.0;
            constraints.weightx = 1.0;
            constraints.ipadx = 15;
            constraints.ipady = 15;
            constraints.anchor = displayedMedia.size() < 17 ? GridBagConstraints.FIRST_LINE_START : GridBagConstraints.CENTER;

            JScrollPane panel = new JScrollPane(content);
            panel.getVerticalScrollBar().setUnitIncrement(10);
            panel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            content.setBackground(backgroundColor);

            int counter = 0;
            for (Media m : displayedMedia) {
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

        static void setDisplayedMedia(List<Media> media) {
            displayedMedia = sort(media);
        }

        static void setSelectedMedia (List<Media> media) {
            selectedMedia = media;
            categorizedMedia = filterByCategory(media);
        }

        static void setSortByYear() {
            order = !order;
            sortByYear = true;
            sortByRating = false;
            setDisplayedMedia(displayedMedia);
            updateContent();
        }

        static void setSortByRating() {
            order = !order;
            sortByRating = true;
            sortByYear = false;
            setDisplayedMedia(displayedMedia);
            updateContent();
        }

        static void setCategory(String category){
            selectedCategory = category;
            categorizedMedia = filterByCategory(selectedMedia);
            search();
            updateContent();
        }

        static List<Media> sort(List<Media> m) {
            if (sortByRating) return mediaRegistry.sortRating(m, order);
            if (sortByYear) return mediaRegistry.sortYear(m, order);
            return m;
        }

        static void search () {
            setDisplayedMedia(mediaRegistry.search(searchBar.getText(), categorizedMedia));
            updateContent();
        }

        static void showAll() {
            setDisplayedMedia(mediaRegistry.getAllMedia());
            setSelectedMedia(mediaRegistry.getAllMedia());
            search();
            updateContent();
        }

        static void showMovies () {
            setDisplayedMedia(mediaRegistry.getMovies());
            setSelectedMedia(mediaRegistry.getMovies());
            search();
            updateContent();
        }
        static void showSeries () {
            setDisplayedMedia(mediaRegistry.getSeries());
            setSelectedMedia(mediaRegistry.getSeries());
            search();
            updateContent();
        }

        static List<Media> filterByCategory(List<Media> media){
            if (selectedCategory.equals("") || selectedCategory.equals("All categories")) return media;
            return new ArrayList<>(media.stream().filter(x -> x.getCategories().contains(selectedCategory.toLowerCase())).toList());
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
