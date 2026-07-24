package UI;

import javax.swing.*;

import DM.DataManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import PD.Item;
import PD.Store;

public class MainMenu extends JFrame {
    private static final long serialVersionUID = 1L;
    private static boolean dataLoaded = false;
    private Store store;

    public MainMenu() {
        String filePath = "src/data/StoreData_v2024FALL.csv";
        DataManager.loadStoreDataOnce(Store.getInstance(), filePath);
        store = Store.getInstance();

        setTitle("Store Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String storeName = store.getName() != null && !store.getName().isEmpty() ? store.getName() : "No Name Set";
        JLabel storeNameLabel = new JLabel("Store: " + storeName, SwingConstants.CENTER);
        storeNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(storeNameLabel, BorderLayout.NORTH);

        JLabel welcomeLabel = new JLabel("Welcome to Store Management System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(welcomeLabel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu maintenanceMenu = new JMenu("Maintenance");

        JMenuItem storeMaintenanceMenuItem = new JMenuItem("Store Maintenance");
        JMenuItem cashierMaintenanceMenuItem = new JMenuItem("Cashier Maintenance");
        JMenuItem registerMaintenanceMenuItem = new JMenuItem("Register Maintenance");
        JMenuItem itemMaintenanceMenuItem = new JMenuItem("Item Maintenance");
        JMenuItem taxCategoryMaintenanceMenuItem = new JMenuItem("Tax Category Maintenance");
        JMenuItem UPCMaintenanceMenuItem = new JMenuItem("UPC Maintenance");
        JMenuItem priceMaintenanceMenuItem = new JMenuItem("Price Maintenance");
        JMenuItem taxRateMaintenanceMenuItem = new JMenuItem("Tax Rate Maintenance");

        storeMaintenanceMenuItem.addActionListener(e -> openStoreMaintenance());
        cashierMaintenanceMenuItem.addActionListener(e -> openCashierMaintenance());
        registerMaintenanceMenuItem.addActionListener(e -> openRegisterMaintenance());
        itemMaintenanceMenuItem.addActionListener(e -> openItemMaintenance());
        taxCategoryMaintenanceMenuItem.addActionListener(e -> openTaxCategoryMaintenance());
        UPCMaintenanceMenuItem.addActionListener(e -> selectItemAndOpenUPCMaintenance());
        priceMaintenanceMenuItem.addActionListener(e -> selectItemAndOpenPriceMaintenance());


        maintenanceMenu.add(storeMaintenanceMenuItem);
        maintenanceMenu.add(cashierMaintenanceMenuItem);
        maintenanceMenu.add(registerMaintenanceMenuItem);
        maintenanceMenu.add(itemMaintenanceMenuItem);
        maintenanceMenu.add(taxCategoryMaintenanceMenuItem);
        maintenanceMenu.add(UPCMaintenanceMenuItem);
        maintenanceMenu.add(priceMaintenanceMenuItem);
        menuBar.add(maintenanceMenu);

        JMenu reportsMenu = new JMenu("Reports");
        JMenuItem itemReportMenuItem = new JMenuItem("Item Report");
        JMenuItem cashierReportMenuItem = new JMenuItem("Cashier Report");
        JMenuItem dailySalesReportMenuItem = new JMenuItem("Daily Sales Report");

        itemReportMenuItem.addActionListener(e -> new ItemReportScreen(store).setVisible(true));
        cashierReportMenuItem.addActionListener(e -> new CashierReportScreen(store).setVisible(true));
        dailySalesReportMenuItem.addActionListener(e -> new DailySalesReportScreen(store).setVisible(true));

        reportsMenu.add(itemReportMenuItem);
        reportsMenu.add(cashierReportMenuItem);
        reportsMenu.add(dailySalesReportMenuItem);
        menuBar.add(reportsMenu);

        JMenu cashierMenu = new JMenu("Cashier");
        JMenuItem openLoginMenuItem = new JMenuItem("Open Login Screen");
        openLoginMenuItem.addActionListener(e -> new LoginScreen().setVisible(true));
        cashierMenu.add(openLoginMenuItem);
        menuBar.add(cashierMenu);

        setJMenuBar(menuBar);
    }

    

	private void openStoreMaintenance() {
        StoreMaintenanceScreen storeScreen = new StoreMaintenanceScreen();
        storeScreen.setVisible(true);
        dispose();
    }

    private void openCashierMaintenance() {
        CashierMaintenanceScreen cashierScreen = new CashierMaintenanceScreen();
        cashierScreen.setVisible(true);
        dispose();
    }

    private void openRegisterMaintenance() {
        RegisterMaintenanceScreen registerScreen = new RegisterMaintenanceScreen();
        registerScreen.setVisible(true);
        dispose();
    }

    private void openItemMaintenance() {
        ItemMaintenanceScreen itemScreen = new ItemMaintenanceScreen();
        itemScreen.setVisible(true);
        dispose();
    }

    private void openTaxCategoryMaintenance() {
        TaxCategoryMaintenanceScreen taxCategoryScreen = new TaxCategoryMaintenanceScreen();
        taxCategoryScreen.setVisible(true);
        dispose();
    }

    private void selectItemAndOpenUPCMaintenance() {
        Item selectedItem = selectItem();
        if (selectedItem != null) {
            UPCSelectionScreen upcScreen = new UPCSelectionScreen(selectedItem, this);
            upcScreen.setVisible(true);
            dispose();
        }
    }

    private void selectItemAndOpenPriceMaintenance() {
        Item selectedItem = selectItem();
        if (selectedItem != null) {
            PriceSelectionScreen priceScreen = new PriceSelectionScreen(selectedItem, this);
            priceScreen.setVisible(true);
            dispose();
        }
    }

    private void openUPCMaintenance() {
        if (!store.getItems().isEmpty()) {
            Item firstItem = store.getItems().iterator().next(); 
            UPCSelectionScreen upcScreen = new UPCSelectionScreen(firstItem, this);
            upcScreen.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No items available for UPC maintenance.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openPriceMaintenance() {
        if (!store.getItems().isEmpty()) {
            Item firstItem = store.getItems().iterator().next(); // Example: Retrieve first item
            PriceSelectionScreen priceScreen = new PriceSelectionScreen(firstItem, this);
            priceScreen.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No items available for Price maintenance.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    /**
     * Displays a dialog for selecting an item.
     *
     * @return The selected Item, or null if no selection is made.
     */
    private Item selectItem() {
        Object[] items = store.getItems().toArray();
        Item selectedItem = (Item) JOptionPane.showInputDialog(
                this,
                "Select an Item:",
                "Item Selection",
                JOptionPane.PLAIN_MESSAGE,
                null,
                items,
                items.length > 0 ? items[0] : null
        );

        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "No item selected.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }

        return selectedItem;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        });
    }
}