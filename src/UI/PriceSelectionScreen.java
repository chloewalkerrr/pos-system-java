package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import PD.Price;
import PD.Item;
import java.util.List;

public class PriceSelectionScreen extends JFrame {

    private static final long serialVersionUID = 1L; // Added serialVersionUID
    private Item item;
    private DefaultListModel<Price> priceListModel;
    private JList<Price> priceList;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;
    //private JFrame parentScreen; // Reference to the parent screen (ItemEditScreen or another JFrame)

    public PriceSelectionScreen(Item item, JFrame parentScreen) {
        // Initialize frame
        setTitle("Price Selection");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        this.item = item;
        //this.parentScreen = parentScreen;

        // Create price list
        priceListModel = new DefaultListModel<>();
        priceList = new JList<>(priceListModel);
        refreshPriceList();

        JScrollPane scrollPane = new JScrollPane(priceList);

        // Create buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        backButton = new JButton("Back");
        
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPriceEditScreen(null);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Price selectedPrice = priceList.getSelectedValue();
                if (selectedPrice != null) {
                    openPriceEditScreen(selectedPrice);
                } else {
                    JOptionPane.showMessageDialog(PriceSelectionScreen.this, "Please select a price to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Price selectedPrice = priceList.getSelectedValue();
                if (selectedPrice != null) {
                    deletePrice(selectedPrice);
                } else {
                    JOptionPane.showMessageDialog(PriceSelectionScreen.this, "Please select a price to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (parentScreen != null) {
                    parentScreen.setVisible(true); // Make parent screen visible again
                    if (parentScreen instanceof ItemEditScreen) {
                        ((ItemEditScreen) parentScreen).refreshPriceList(); // Refresh parent price list
                    }
                }
            }
        });
        
        priceList.addListSelectionListener(e -> {
            boolean isSelected = !priceList.isSelectionEmpty();
            editButton.setEnabled(isSelected);
            deleteButton.setEnabled(isSelected);
        });

        // Add components to frame
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Refresh the price list displayed on the screen.
     */
    public void refreshPriceList() {
        priceListModel.clear();
        List<Price> prices = item.getPrices().stream().toList();
        for (Price price : prices) {
            priceListModel.addElement(price);
        }
    }

    /**
     * Open the PriceEditScreen for adding or editing a price.
     *
     * @param price The price to edit, or null to add a new price.
     */
    private void openPriceEditScreen(Price price) {
        PriceEditScreen priceEditScreen = new PriceEditScreen(item, price, this);
        priceEditScreen.setVisible(true);
        dispose();
    }

    /**
     * Delete the selected price from the item.
     *
     * @param price The price to delete.
     */
    private void deletePrice(Price price) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this price?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            item.removePrice(price.getEffectiveDate());
            refreshPriceList();
            JOptionPane.showMessageDialog(this, "Price deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Run the screen independently for testing purposes
        SwingUtilities.invokeLater(() -> {
            Item dummyItem = new Item("1234", "Dummy Item");
            PriceSelectionScreen screen = new PriceSelectionScreen(dummyItem, null);
            screen.setVisible(true);
        });
    }
}
