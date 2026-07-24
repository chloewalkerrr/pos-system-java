package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import PD.Item;
import PD.Store;

public class ItemMaintenanceScreen extends JFrame {

    private static final long serialVersionUID = 1L; // Added serialVersionUID
    private DefaultListModel<String> itemListModel;
    private JList<String> itemList;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton cancelButton;
    private Store store;

    public ItemMaintenanceScreen() {
        // Set up the frame
        setTitle("Item Maintenance");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Retrieve the Store instance
        store = Store.getInstance(); // Replace with your singleton or global store instance retrieval method

        // Create the item list model and populate it
        itemListModel = new DefaultListModel<>();
        populateItemList();

        // Create UI components
        itemList = new JList<>(itemListModel);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(itemList);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        cancelButton = new JButton("Cancel");

        // Enable only the Add button by default
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        // Add buttons to the panel
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);

        // Add action listeners for buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditItemScreen(null);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = itemList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    String selectedItemNumber = itemListModel.getElementAt(selectedIndex).split(" - ")[0]; // Extract item number
                    Item item = store.findItem(selectedItemNumber);
                    openEditItemScreen(item);
                }
            }
        });
        
        cancelButton.addActionListener(e -> cancelChanges());

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedItem();
            }
        });

        // Add selection listener to enable/disable buttons
        itemList.addListSelectionListener(e -> {
            boolean isSelected = !itemList.isSelectionEmpty();
            updateButton.setEnabled(isSelected);
            deleteButton.setEnabled(isSelected);
        });

        // Add components to frame
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Populate the item list from the store.
     */
    private void populateItemList() {
        itemListModel.clear();
        List<Item> items = store.getItems().stream().toList(); // Convert Collection<Item> to List<Item>
        for (Item item : items) {
            itemListModel.addElement(item.getNumber() + " - " + item.getDescription());
        }
    }

    /**
     * Open the Edit Item screen.
     *
     * @param item The item to edit, or null for adding a new item.
     */
    private void openEditItemScreen(Item item) {
        ItemEditScreen itemEditScreen = new ItemEditScreen(item, store, this); // Updated reference
        itemEditScreen.setVisible(true);
        dispose();
    }

    /**
     * Delete the selected item from the list and the store.
     */
    private void deleteSelectedItem() {
        int selectedIndex = itemList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String selectedItemNumber = itemListModel.getElementAt(selectedIndex).split(" - ")[0]; // Extract the item number
            Item item = store.findItem(selectedItemNumber);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this item?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                store.getItems().remove(item); // Remove from store
                populateItemList(); // Refresh the list
                JOptionPane.showMessageDialog(this, "Item deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    /**
     * Cancel and return to the main menu.
     */
    private void cancelChanges() {
        new MainMenu().setVisible(true);
        dispose();
    }
   

    /**
     * Refresh the item list on the screen.
     */
    public void refreshItemList() {
        populateItemList();
    }

    public static void main(String[] args) {
        // Run the screen independently for testing purposes
        SwingUtilities.invokeLater(() -> {
            ItemMaintenanceScreen screen = new ItemMaintenanceScreen();
            screen.setVisible(true);
        });
    }
}