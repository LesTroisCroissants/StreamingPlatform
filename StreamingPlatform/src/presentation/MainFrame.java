package presentation;

// Importing required classes
import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
        static JFrame frame;

        public static void main(String[] args) {
            frame = new JFrame("Pastryeam");

            // MenuBar
            MenuBarUI menuBar = new MenuBarUI();
            frame.setJMenuBar(menuBar.createMenuBar());

            // create main panel
            ContentUI contentUI = new ContentUI();

            JPanel panel = contentUI.createMainPanel();
            //JPanel panelMedia = contentUI.getMoviePanel();
            frame.add(panel);



            // Frame settings
            frame.setSize(1280, 720);
            frame.setVisible(true);
        }

        public static void search(String input) {

        }
    }
