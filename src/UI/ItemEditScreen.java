package UI;

import javax.swing.*;
import java.awt.*;
import PD.Item;
import PD.Store;
import PD.TaxCategory;

public class ItemEditScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtItemNumber;
    private JTextField txtDescription;
    private JComboBox<String> cmbTaxCategory;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton manageUpcsButton;
    private JButton managePricesButton;

    private Store store;
    private Item item;
    private ItemMaintenanceScreen parentScreen;
    private boolean isNew;

    public ItemEditScreen(Item item, Store store, ItemMaintenanceScreen parentScreen) {
        setTitle("Item Edit");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        this.item = item;
        this.store = store;
        this.parentScreen = parentScreen;
        this.isNew = (item == null);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Item Number:"));
        txtItemNumber = new JTextField();
        inputPanel.add(txtItemNumber);

        inputPanel.add(new JLabel("Description:"));
        txtDescription = new JTextField();
        inputPanel.add(txtDescription);

        inputPanel.add(new JLabel("Tax Category:"));
        cmbTaxCategory = new JComboBox<>();
        for (TaxCategory category : store.getTaxCategories()) {
            cmbTaxCategory.addItem(category.getCategory());
        }
        inputPanel.add(cmbTaxCategory);

        if (item != null) {
            txtItemNumber.setText(item.getNumber());
            txtItemNumber.setEnabled(false);
            txtDescription.setText(item.getDescription());
            if (item.getTaxCategory() != null) {
                cmbTaxCategory.setSelectedItem(item.getTaxCategory().getCategory());
            }
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        manageUpcsButton = new JButton("Manage UPCs");
        managePricesButton = new JButton("Manage Prices");
        manageUpcsButton.setEnabled(!isNew);
        managePricesButton.setEnabled(!isNew);

        buttonPanel.add(saveButton);
        buttonPanel.add(manageUpcsButton);
        buttonPanel.add(managePricesButton);
        buttonPanel.add(cancelButton);

        saveButton.addActionListener(e -> saveItem());
        cancelButton.addActionListener(e -> {
            dispose();
            if (parentScreen != null) {
                parentScreen.setVisible(true);
            }
        });
        manageUpcsButton.addActionListener(e -> openUpcMaintenance());
        managePricesButton.addActionListener(e -> openPriceMaintenance());

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveItem() {
        try {
            String number = txtItemNumber.getText().trim();
            String description = txtDescription.getText().trim();
            String taxCategoryName = (String) cmbTaxCategory.getSelectedItem();

            if (number.isEmpty() || description.isEmpty() || taxCategoryName == null || taxCategoryName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TaxCategory taxCategory = store.findTaxCategory(taxCategoryName);
            if (taxCategory == null) {
                JOptionPane.showMessageDialog(this, "Invalid Tax Category.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (item == null) {
                item = new Item(number, description);
                store.addItem(item);
            }

            item.setDescription(description);
            item.setTaxCategory(taxCategory);

            JOptionPane.showMessageDialog(this, "Item saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            if (parentScreen != null) {
                parentScreen.refreshItemList();
                parentScreen.setVisible(true);
            }
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving item: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openUpcMaintenance() {
        new UPCSelectionScreen(item, this).setVisible(true);
        setVisible(false);
    }

    private void openPriceMaintenance() {
        new PriceSelectionScreen(item, this).setVisible(true);
        setVisible(false);
    }

    /**
     * Kept so PriceSelectionScreen's callback still compiles; prices are
     * now managed entirely within PriceSelectionScreen itself.
     */
    public void refreshPriceList() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Store store = Store.getInstance();
            ItemEditScreen screen = new ItemEditScreen(null, store, null);
            screen.setVisible(true);
        });
    }
}