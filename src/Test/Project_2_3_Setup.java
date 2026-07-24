package Test;

import javax.swing.*;
import PD.*;
import DM.*;
import UI.*;

public class Project_2_3_Setup {

    public static void run() {
        // Initialize Store Singleton Instance
        Store store = Store.getInstance();

        // Load Data
        String filePath = "src/data/StoreDAta_v2024FALL.csv"; // Adjust the path if needed
        DataManager.loadStoreData(store, filePath);

        SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        });
        
        // Set up Main Frame
       JFrame mainFrame = new JFrame("Store Maintenance Application");
       mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       mainFrame.setSize(800, 600);

        // Display Main Menu Screen
       //MainMenu mainMenu = new MainMenu(); // Use default constructor
       //mainFrame.setContentPane(mainMenu.getContentPane()); // Properly set the content pane
      // mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        run(); // Allow this class to run independently
    }
}


