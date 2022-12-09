package presentation;

// Importing required classes
import domain.Media;
import domain.MediaRegistry;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.util.Arrays;
import java.util.List;

public class MainFrame extends JFrame {
        static JFrame frame;
        static MediaRegistry mediaRegistry;
        static List<Media> media;
        static JPanel mainPanel;

        static JTextField searchBar;
        static String lastSearch;


        public static void init(){
            try {
                mediaRegistry = new MediaRegistry();
            } catch (FileNotFoundException e){ // TODO handle this with a popup
                System.out.println("Hej, you error! :)");
            }

            frame = new JFrame("Pastryeam");
        }

        public static void main(String[] args) {
            init();

            frame.setJMenuBar(createMenuBar());
            
            frame.add(createMainPanel());


            // Frame settings
            frame.setSize(1280, 720);
            frame.setVisible(true);
            frame.requestFocusInWindow();
        }
        
        public static JMenuBar createMenuBar(){
            JMenuBar menuBar = new JMenuBar();

            JPanel topPanel = createTopPanel();
            JPanel bottomPanel = createBottomPanel();

            menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.Y_AXIS));


            createTopPanel();
            createBottomPanel();

            // Debugging colors
            topPanel.setBackground(Color.cyan);
            bottomPanel.setBackground(Color.green);

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

            return panel;
        }

        public static JPanel createBottomPanel(){
            JPanel panel = new JPanel();

            // Creating buttons
            JButton buttonMovies = new JButton("Movies");
            JButton buttonSeries = new JButton("Series");
            JButton buttonFavorites = new JButton("Favorites");

            // Adding buttons
            panel.add(buttonMovies);
            panel.add(buttonSeries);
            panel.add(buttonFavorites);

            // add spacing
            panel.add(new JToolBar.Separator(new Dimension(50, 0)));

            // Adding label
            JLabel flopLabel = new JLabel("Flop");
            panel.add(flopLabel);

            return panel;
        }

        public static JPanel createMainPanel(){
            JPanel panel = new JPanel();

            // TODO Load media into this view

            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // TODO layout needs changing
            panel.setBackground(Color.darkGray);

            return panel;
        }

        static void search () {
            media = mediaRegistry.search(searchBar.getText().trim());
            System.out.println("Works!");
        }
    }
