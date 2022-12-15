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
        private static MediaInfo mediaRegistry;
        private static List<Media> selectedMedia; // Movies & Series || Movies || Series
        private static List<Media> categorizedMedia;
        private static List<Media> displayedMedia; // Shown on screen

        // Selected category or media
        private static String selectedView = "all";
        private static String selectedCategory = "All categories";

        // Popup show movie
        private static JPanel popup;

        // Panels
        private static JLayeredPane mainPanel;

        // Top panel content
        private static JTextField searchBar;

        // Sorting
        private static boolean sortByYear;
        private static boolean sortByRating;
        private static boolean order;

        // Loading image on startup
        private static ImageIcon favoriteImg;
        private static final Color backgroundColor = new Color(28,28,28);

        /**
         * Initializing method run when starting the program.
         */
        private static void init(){
            sortByRating = false;
            sortByYear = false;
            order = false;
            try {
                mediaRegistry = new MediaRegistry();
                setDisplayedMedia(mediaRegistry.getAllMedia());
                setSelectedMedia(mediaRegistry.getAllMedia());
                categorizedMedia = mediaRegistry.getAllMedia();

                BufferedImage favoriteImgBuffer = ImageIO.read(new File("StreamingPlatform/src/data/Data/presentation_images/favorite.png"));
                favoriteImg = new ImageIcon(favoriteImgBuffer.getScaledInstance(64,64, Image.SCALE_SMOOTH));
            } catch (FileNotFoundException e){
                JLabel errorMessage = new JLabel("The program could not find the media files.");
                JOptionPane.showConfirmDialog(frame, errorMessage, "File not found!", JOptionPane.DEFAULT_OPTION);
                System.exit(0);
            } catch (IOException e) {
                System.out.println("Could not find favorite icon!");
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

        /**
         * Tries to save favorites to file and exit the program.
         * Displays popup with error if exception is caught.
         */
        private static void trySaveAndClose(){
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

        /**
         * Creates the menubar for the program.
         */
        private static JMenuBar createMenuBar(){
            JMenuBar menuBar = new JMenuBar();

            JPanel topPanel = createTopPanel();
            JPanel bottomPanel = createBottomPanel();

            menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.Y_AXIS));

            menuBar.add(topPanel);
            menuBar.add(bottomPanel);

            return menuBar;
        }

        /**
         * Creates the top part of the menubar.
         */
        private static JPanel createTopPanel(){
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

                // About tooltip
                ToolTipManager.sharedInstance().setDismissDelay(20000);
                logoLabel.setToolTipText(
                    """ 
                    <html>
                    Pastryeam made with Love & French Pastry by Les Trois Croissants <br>
                    Version 1.0.0 <br>
                    Icons: Flaticon.com <br>
                    Images provided by ITU
                    </html>
                    """
                );

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

        // Creates the bottom part the menubar
        private static JPanel createBottomPanel(){
            JPanel panel = new JPanel();

            panel.setLayout(new BorderLayout());

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(backgroundColor);

            // Creating buttons
            JButton buttonHome = new JButton("Home");
            JButton buttonMovies = new JButton("Movies");
            JButton buttonSeries = new JButton("Series");
            JButton buttonFavorites = new JButton("Favorites");

            // Getting categories into dropdown
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


        /**
         * Creates the main panel which holds the content.
         */
        private static JLayeredPane createMainPanel(){
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
                // Add 8 movies per row - decision made to fit size
                constraints.gridy = counter / 8;
                constraints.fill = GridBagConstraints.BOTH;

                JLayeredPane coverView = new JLayeredPane();
                JLabel coverImg = new JLabel(new ImageIcon(m.getCoverImage()));
                coverImg.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        mediaPopup(m);
                    }
                });
                coverImg.setBounds(5, 5, 140, 209);
                coverView.add(coverImg, JLayeredPane.DEFAULT_LAYER);
                coverView.setPreferredSize(new Dimension(140, 209));

                if (m.isFavorite()) {
                    JLabel favoriteIcon = new JLabel(favoriteImg);
                    favoriteIcon.setBounds(75, 10, 64, 64);
                    coverView.add(favoriteIcon, JLayeredPane.PALETTE_LAYER);
                }

                content.add(coverView, constraints);

                counter++;
            }

            // Fill out empty space
            if (displayedMedia.size() > 0) {
                int empty = 8 - (counter + 1 % 8) + 1;
                constraints.weightx = 3.0;
                for (int i = 0; i < empty; i++) {
                    content.add(new JLabel(""), constraints);
                }
            }

            if (displayedMedia.size() == 0) {
                JLabel message = new JLabel(
                        (selectedView.equals("favorites") ? "No favorites have been added yet" : "No media matches the current selection")
                );
                message.setFont(new Font("Sans Serif", Font.PLAIN, 24));
                message.setForeground(Color.white);
                content.add(message);
            }

            JLayeredPane layered = new JLayeredPane();

            // Subtracting pixels from the main content width on the Windows-OS, to be able to see the scrollbar
            int scrollWidth = System.getProperty("os.name").contains("Windows") ? 1265 : 1280;

            // Set size to fit rest of space
            scroll.setBounds(0,0, scrollWidth,550);

            layered.add(scroll, JLayeredPane.DEFAULT_LAYER);

            return layered;
        }

        /**
         * Sets the global "displayMedia" variable to the sorted media.
         */
        private static void setDisplayedMedia(List<Media> media) {
            displayedMedia = sort(media);
        }

        /**
         * Sets the "selectedMedia" global variable to the given argument media,
         * and sets the "categorizedMedia" to the category filtered version of the given argument media.
         */
        private static void setSelectedMedia (List<Media> media) {
            selectedMedia = media;
            categorizedMedia = filterByCategory(media);
        }

        // Sorts media by year, and flips between ascending and descending, for every time it is called.
        private static void setSortByYear() {
            order = !order;
            sortByYear = true;
            sortByRating = false;
            setDisplayedMedia(displayedMedia);
            updateContent();
        }

        private static void setSortByRating() {
            order = !order;
            sortByRating = true;
            sortByYear = false;
            setDisplayedMedia(displayedMedia);
            updateContent();
        }

        private static void setCategory(String category){
            selectedCategory = category;
            categorizedMedia = filterByCategory(selectedMedia);
            search();
            updateContent();
        }

        /**
         * Sorts media given as parameter, depending on the global variables for sorting
         */
        private static List<Media> sort(List<Media> m) {
            if (sortByRating) return mediaRegistry.sortRating(m, order);
            if (sortByYear) return mediaRegistry.sortYear(m, order);
            return m;
        }

        private static void search () {
            setDisplayedMedia(mediaRegistry.search(searchBar.getText(), categorizedMedia));
            updateContent();
        }

        private static void showMedia(List<Media> media, String view) {
            selectedView = view;
            setDisplayedMedia(media);
            setSelectedMedia(media);
            search();
            updateContent();
        }

        private static List<Media> filterByCategory(List<Media> media){
            if (selectedCategory.equals("") || selectedCategory.equals("All categories")) return media;
            return new ArrayList<>(media.stream().filter(x -> x.getCategories().contains(selectedCategory.toLowerCase())).toList());
        }


        private static void updateContent() {
            frame.remove(mainPanel);
            mainPanel = createMainPanel();
            if (popup != null) mainPanel.add(popup);
            frame.add(mainPanel);
            frame.revalidate();
        }

        private static void mediaPopup(Media media) {
            if (popup != null) return;
            popup = new PopUp(media, mediaRegistry);

            JLabel popupClose = new JLabel("✖");
            popupClose.setFont(new Font("Sans-Serif", Font.PLAIN,28));
            popupClose.setBorder(BorderFactory.createEmptyBorder(5, 5,5,10));
            popupClose.setForeground(Color.white);
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

        private static void hidePopup() {
            mainPanel.remove(popup);
            popup = null;
            frame.repaint();
            if (selectedView.equals("favorites")) {
                showMedia(mediaRegistry.getFavorites(), "favorites");
                updateContent();
            }
        }
    }
