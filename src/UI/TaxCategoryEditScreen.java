package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import PD.TaxCategory;
import PD.Store;

public class TaxCategoryEditScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField categoryField;
    private JButton saveButton, cancelButton, manageTaxRatesButton;
    private TaxCategory taxCategory;
    private Store store;
    private TaxCategoryMaintenanceScreen parentScreen;
    private boolean isNew;

    public TaxCategoryEditScreen(TaxCategory taxCategory, Store store, TaxCategoryMaintenanceScreen parentScreen) {
        setTitle("Edit Tax Category");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        this.taxCategory = taxCategory;
        this.store = store;
        this.parentScreen = parentScreen;
        this.isNew = (taxCategory == null);

        JPanel formPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel categoryLabel = new JLabel("Category:");
        categoryField = new JTextField();

        if (taxCategory != null) {
            categoryField.setText(taxCategory.getCategory());
        }

        formPanel.add(categoryLabel);
        formPanel.add(categoryField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        manageTaxRatesButton = new JButton("Manage Tax Rates");
        manageTaxRatesButton.setEnabled(!isNew);

        buttonPanel.add(saveButton);
        buttonPanel.add(manageTaxRatesButton);
        buttonPanel.add(cancelButton);

        saveButton.addActionListener(this::saveTaxCategory);
        cancelButton.addActionListener(this::cancelEdit);
        manageTaxRatesButton.addActionListener(this::openTaxRates);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveTaxCategory(ActionEvent e) {
        String category = categoryField.getText().trim();

        if (category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (taxCategory == null) {
            taxCategory = new TaxCategory(category);
            store.addTaxCategory(taxCategory);
        } else {
            String oldCategory = taxCategory.getCategory();
            taxCategory.setCategory(category);
            if (!oldCategory.equals(category)) {
                store.removeTaxCategory(oldCategory);
                store.addTaxCategory(taxCategory);
            }
        }

        JOptionPane.showMessageDialog(this, "Tax Category saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        parentScreen.refreshList();
        parentScreen.setVisible(true);
        dispose();
    }

    private void cancelEdit(ActionEvent e) {
        parentScreen.setVisible(true);
        dispose();
    }

    private void openTaxRates(ActionEvent e) {
        new TaxRateSelectionScreen(taxCategory, this).setVisible(true);
        setVisible(false);
    }
}