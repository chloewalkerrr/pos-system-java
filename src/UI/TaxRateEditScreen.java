package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import PD.TaxCategory;
import PD.TaxRate;

public class TaxRateEditScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField effectiveDateField;
    private JTextField taxRateField;
    private JButton saveButton;
    private JButton cancelButton;
    private TaxRate taxRate;
    private TaxCategory taxCategory;
    private TaxRateSelectionScreen taxRateSelectionScreen;

    public TaxRateEditScreen(TaxRate taxRate, TaxCategory taxCategory, TaxRateSelectionScreen selectionScreen) {
        setTitle("Edit Tax Rate");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        this.taxRate = taxRate;
        this.taxCategory = taxCategory;
        this.taxRateSelectionScreen = selectionScreen;

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel effectiveDateLabel = new JLabel("Effective Date (yyyy-MM-dd):");
        JLabel taxRateLabel = new JLabel("Tax Rate:");

        effectiveDateField = new JTextField();
        taxRateField = new JTextField();

        if (taxRate != null) {
            effectiveDateField.setText(taxRate.getEffectiveDate().toString());
            taxRateField.setText(taxRate.getTaxRate().toString());
        }

        formPanel.add(effectiveDateLabel);
        formPanel.add(effectiveDateField);
        formPanel.add(taxRateLabel);
        formPanel.add(taxRateField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        saveButton.addActionListener(this::saveTaxRate);
        cancelButton.addActionListener(this::cancelEdit);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveTaxRate(ActionEvent e) {
        String effectiveDateString = effectiveDateField.getText().trim();
        String taxRateString = taxRateField.getText().trim();

        if (effectiveDateString.isEmpty() || taxRateString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Effective Date and Tax Rate cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate effectiveDate = LocalDate.parse(effectiveDateString);
            BigDecimal taxRateValue = new BigDecimal(taxRateString);

            if (taxRate == null) {
                taxRate = new TaxRate(effectiveDate, taxRateValue);
                taxCategory.addTaxRate(taxRate);
            } else {
                taxCategory.removeTaxRate(taxRate.getEffectiveDate());
                taxRate = new TaxRate(effectiveDate, taxRateValue);
                taxCategory.addTaxRate(taxRate);
            }

            JOptionPane.showMessageDialog(this, "Tax Rate updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            taxRateSelectionScreen.refreshTable();
            taxRateSelectionScreen.setVisible(true);
            dispose();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid tax rate format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelEdit(ActionEvent e) {
        taxRateSelectionScreen.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaxCategory dummyCategory = new TaxCategory("Food");
            TaxRate dummyTaxRate = new TaxRate(LocalDate.of(2023, 1, 1), new BigDecimal("0.07"));
            TaxRateSelectionScreen dummySelectionScreen = new TaxRateSelectionScreen(dummyCategory, null);
            TaxRateEditScreen screen = new TaxRateEditScreen(dummyTaxRate, dummyCategory, dummySelectionScreen);
            screen.setVisible(true);
        });
    }
}
