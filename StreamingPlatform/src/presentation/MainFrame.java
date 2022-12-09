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
        static JScrollPane mainPanel;

        static JTextField searchBar;
        static String lastSearch;


        public static void init(){
            try {
                mediaRegistry = new MediaRegistry();
                media = mediaRegistry.getAllMedia();
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
                public void insertUpdate(DocumentEvent e) { search(e); }
                public void removeUpdate(DocumentEvent e) { search(e); }
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

        public static JScrollPane createMainPanel(){
            // TODO Load media into this view

            JPanel content = new JPanel();
            content.setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weighty = 1.0;
            constraints.weightx = 1.0;
            constraints.ipadx = 15;
            constraints.ipady = 15;
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.fill = GridBagConstraints.BOTH;

            JScrollPane panel = new JScrollPane(content);
            panel.getVerticalScrollBar().setUnitIncrement(10);
            panel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            content.setBackground(Color.darkGray);
            content.getWidth();

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

        static void search (DocumentEvent e) {
            String searchText = null;
            try {
                searchText = e.getDocument().getText(0, e.getDocument().getLength()).trim();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            media = mediaRegistry.search(searchText);
            updateContent();
        }

        static void updateContent() {
            frame.remove(mainPanel);
            mainPanel = createMainPanel();
            frame.add(mainPanel);
            frame.revalidate();
        }
    }
