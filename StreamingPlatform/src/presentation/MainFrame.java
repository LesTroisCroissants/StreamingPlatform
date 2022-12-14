package presentation;

// Importing required classes
import domain.Media;
import domain.MediaInfo;
import domain.MediaRegistry;

import java.awt.*;
import java.awt.event.*;
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

        static String selectedView = "all";
        static String selectedCategory = "All categories";

        // Popup show movie
        static JPanel popup;

        // Panels
        static JMenuBar menuBar;
        static JLayeredPane mainPanel;

        // Top panel content
        static JTextField searchBar;
        static String aboutPageText =
                """ 
                <html>
                Pastryeam made with Love & French Pastry by Les Trois Croissants <br>
                Version 1.0.0 <br>
                Icons: Flaticon.com <br>
                Images provided by ITU
                </html>
                """;

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
            } catch (FileNotFoundException e){
                JLabel errorMessage = new JLabel("The program could not find the media files.");
                JOptionPane.showConfirmDialog(frame, errorMessage, "File not found!", JOptionPane.DEFAULT_OPTION);
                System.exit(0);
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
            frame.setResizable(false);
            frame.setVisible(true);
            frame.requestFocusInWindow();
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

            // Handling closing the application
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    trySaveAndClose();
                }
            });
        }

        public static void trySaveAndClose(){
            try {
                mediaRegistry.saveFavorites();
                System.exit(0);
            } catch (FileNotFoundException saveFavoritesException) {
                JLabel errorMessage = new JLabel("Could not save favorites.");

                // Makes the popup
                int returnedOption = JOptionPane.showConfirmDialog(frame,errorMessage, "Save Error!", JOptionPane.OK_CANCEL_OPTION);

                int okButton = 0;
                if (returnedOption == okButton){
                    System.exit(0);
                }
            }
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

            // Left side of the topPanel
            JPanel leftSidePanel = new JPanel();
            leftSidePanel.setLayout(new BorderLayout());
            leftSidePanel.setBackground(backgroundColor);

            // Adding pastryeam logo
            try {
                BufferedImage logoImage = ImageIO.read(new File("StreamingPlatform/src/data/Data/presentation_images/pastryeam_logo.png"));
                JLabel logoLabel = new JLabel(new ImageIcon(logoImage.getScaledInstance(329,56, Image.SCALE_SMOOTH)));
                leftSidePanel.add(logoLabel, BorderLayout.LINE_START);
            } catch (IOException e){
                System.out.println("Could not load logo!");
            }

            // Adding "info i" icon
            try {
                BufferedImage logoImage = ImageIO.read(new File("StreamingPlatform/src/data/Data/presentation_images/information_i.png"));
                JLabel logoLabel = new JLabel(new ImageIcon(logoImage.getScaledInstance(25,25, Image.SCALE_SMOOTH)));
                logoLabel.setBorder(BorderFactory.createEmptyBorder(0,25,0,0));

                logoLabel.setToolTipText(aboutPageText);

                leftSidePanel.add(logoLabel, BorderLayout.LINE_END);
            } catch (IOException e){
                System.out.println("Could not load logo!");
            }

            panel.add(leftSidePanel, BorderLayout.LINE_START);


            // Right side of the topPanel
            JPanel searchPanel = new JPanel(new BorderLayout());

            // search bar
            searchBar = new JTextField();
            searchBar.setBorder(new EmptyBorder(0,12,0,12));
            searchBar.setPreferredSize(new Dimension(300, 35));
            searchBar.setFont(new Font("Sans-Serif", Font.PLAIN,16));
            searchPanel.add(searchBar, BorderLayout.CENTER);
            searchPanel.setBorder(new EmptyBorder(14,0,14,0));
            searchPanel.setBackground(backgroundColor);

            // Adding the search icon
            try {
                BufferedImage searchIcon = ImageIO.read(new File("StreamingPlatform/src/data/Data/presentation_images/search.png"));
                JLabel searchIconLabel = new JLabel(new ImageIcon(searchIcon.getScaledInstance(50,50, Image.SCALE_SMOOTH)));
                searchIconLabel.setBorder(BorderFactory.createEmptyBorder(0,0,0,15));
                searchPanel.add(searchIconLabel, BorderLayout.LINE_START);
            } catch (IOException e){
                System.out.println("Could not load search icon!");
            }

            searchBar.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { search(); }
                public void removeUpdate(DocumentEvent e) { search(); }
                public void changedUpdate(DocumentEvent e) {}
            });

            panel.add(searchPanel, BorderLayout.LINE_END);

            panel.setBackground(backgroundColor);

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
            buttonHome.addActionListener((e) -> {showMedia(mediaRegistry.getAllMedia(), "all"); dropDownCategories.setSelectedIndex(0); searchBar.setText("");});
            buttonMovies.addActionListener((e) -> showMedia(mediaRegistry.getMovies(), "movies"));
            buttonSeries.addActionListener((e) -> showMedia(mediaRegistry.getSeries(), "series"));
            buttonFavorites.addActionListener((e) -> showMedia(mediaRegistry.getFavorites(), "favorites"));

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


        public static JLayeredPane createMainPanel(){
            JPanel content = new JPanel();
            content.setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weighty = 1.0;
            constraints.weightx = 1.0;
            constraints.ipadx = 15;
            constraints.ipady = 15;
            constraints.anchor = GridBagConstraints.FIRST_LINE_START;

            JScrollPane scroll = new JScrollPane(content);

            if (popup != null) scroll.getVerticalScrollBar().setUnitIncrement(0);
            else scroll.getVerticalScrollBar().setUnitIncrement(10);
            scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            content.setBackground(backgroundColor);

            int counter = 0;
            for (Media m : displayedMedia) {
                constraints.gridy = counter / 8;

                JLabel coverImg = new JLabel(new ImageIcon(m.getCoverImage()));
                // coverImg.setToolTipText(m.getTitle());
                coverImg.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        mediaPopup(m);
                    }
                });
                content.add(coverImg, constraints);
                counter++;
            }

             // TODO find prettier way to add empty space in the grid
            int empty = 8 - (counter + 1 % 8) + 1;
            constraints.weightx = 3.0;
            for (int i = 0; i < empty; i++) {
                content.add(new JLabel(""), constraints);
            }

            JLayeredPane layered = new JLayeredPane();

            // Set size to fit rest of space
            scroll.setBounds(0,0,1265,550);

            layered.add(scroll, JLayeredPane.DEFAULT_LAYER);

            return layered;
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

        static void showMedia(List<Media> media, String view) {
            selectedView = view;
            setDisplayedMedia(media);
            setSelectedMedia(media);
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
            if (popup != null) mainPanel.add(popup);
            frame.add(mainPanel);
            frame.revalidate();
        }

        static void mediaPopup(Media media) {
            if (popup != null) return;
            popup = new PopUp(media, mediaRegistry);

            JLabel popupClose = new JLabel("✖");
            popupClose.setFont(new Font("Sans-Serif", Font.PLAIN,28));
            popupClose.setBorder(BorderFactory.createEmptyBorder(5, 5,5,10));
            popupClose.setForeground(new Color(255,255,255));
            popupClose.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    hidePopup();
                }
            });
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.PAGE_START;
            popup.add(popupClose, c);
            popup.setBounds(320,100,665, 314);

            mainPanel.add(popup, JLayeredPane.POPUP_LAYER);
        }

        static void hidePopup() {
            mainPanel.remove(popup);
            popup = null;
            frame.repaint();
            if (selectedView.equals("favorites")) {
                showMedia(mediaRegistry.getFavorites(), "favorites");
                updateContent();
            }
        }
    }
