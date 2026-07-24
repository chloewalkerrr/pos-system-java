package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import PD.Cashier;
import PD.Store;

public class CashierMaintenanceScreen extends JFrame {

    private static final long serialVersionUID = 1L; // Added serialVersionUID
    private DefaultListModel<String> cashierListModel;
    private JList<String> cashierList;
    private JButton saveButton;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton cancelButton;
    private Store store;

    public CashierMaintenanceScreen() {
        // Set up the frame
        setTitle("Cashier Maintenance");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Retrieve the Store instance
        store = Store.getInstance();

        // Create the cashier list model and populate it
        cashierListModel = new DefaultListModel<>();
        populateCashierList();

        // Create UI components
        cashierList = new JList<>(cashierListModel);
        cashierList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(cashierList);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save");
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        cancelButton = new JButton("Cancel");

        // Enable only the Add button by default
        saveButton.setEnabled(false);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        // Add buttons to the panel
        buttonPanel.add(saveButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        

        // Add action listeners for buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCashierEditScreen(null);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = cashierList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    String selectedCashierId = cashierListModel.getElementAt(selectedIndex).split(" ")[0];
                    Cashier selectedCashier = store.findCashier(selectedCashierId);
                    openCashierEditScreen(selectedCashier);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedCashier();
            }
        });
        
        cancelButton.addActionListener(e -> cancelChanges());
        saveButton.addActionListener(e -> saveCashierChanges());

        // Add selection listener to enable/disable buttons
        cashierList.addListSelectionListener(e -> {
            boolean isSelected = !cashierList.isSelectionEmpty();
            updateButton.setEnabled(isSelected);
            deleteButton.setEnabled(isSelected);
        });

        // Add components to frame
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Populate the cashier list from the store.
     */
    private void populateCashierList() {
        cashierListModel.clear();
        List<Cashier> cashiers = store.getCashiers().stream().toList();
        for (Cashier cashier : cashiers) {
            cashierListModel.addElement(cashier.getNumber() + " - " + cashier.getPerson().getName());
        }
    }

    /**
     * Open the Edit Cashier screen.
     *
     * @param cashier The cashier to edit, or null for adding a new cashier.
     */
    private void openCashierEditScreen(Cashier cashier) {
        CashierEditScreen editCashierScreen = new CashierEditScreen(cashier, store, this);
        editCashierScreen.setVisible(true); // Corrected variable name
        dispose();
    }

    /**
     * Delete the selected cashier from the list and the store.
     */
    private void deleteSelectedCashier() {
        int selectedIndex = cashierList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String selectedCashierId = cashierListModel.getElementAt(selectedIndex).split(" ")[0];
            Cashier cashier = store.findCashier(selectedCashierId);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this cashier?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                store.getCashiers().remove(cashier); // Remove from store
                populateCashierList(); // Refresh the list
                JOptionPane.showMessageDialog(this, "Cashier deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Save the changes made in the current screen.
     */
    private void saveCashierChanges() {
        JOptionPane.showMessageDialog(this, "All changes to cashiers have been saved in memory.", "Success", JOptionPane.INFORMATION_MESSAGE);
        // Refresh the list to reflect changes in memory
        populateCashierList();
    }

    /**
     * Cancel the changes and return to the main menu.
     */
    private void cancelChanges() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel? Unsaved changes will be lost.",
                "Confirm Cancel",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Dispose the current screen and navigate back to the MainMenu
            new MainMenu().setVisible(true); 
            dispose();
        }
    }


    /**
     * Refresh the cashier list on the screen.
     */
    public void refreshCashierList() {
        populateCashierList();
    }

    public static void main(String[] args) {
        // Run the screen independently for testing purposes
        SwingUtilities.invokeLater(() -> {
            CashierMaintenanceScreen screen = new CashierMaintenanceScreen();
            screen.setVisible(true);
        });
    }
}