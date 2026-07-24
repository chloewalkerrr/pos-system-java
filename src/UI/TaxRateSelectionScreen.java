package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

import PD.TaxCategory;
import PD.TaxRate;

public class TaxRateSelectionScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private TaxCategory taxCategory;
    private JTable taxRateTable;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;
    private TaxCategoryEditScreen taxCategoryEditScreen;

    public TaxRateSelectionScreen(TaxCategory taxCategory, TaxCategoryEditScreen parentScreen) {
        this.taxCategory = taxCategory;
        this.taxCategoryEditScreen = parentScreen;

        setTitle("Tax Rate Selection");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        taxRateTable = new JTable();
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(taxRateTable);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        backButton = new JButton("Back");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(this::openAddTaxRate);
        editButton.addActionListener(this::openEditTaxRate);
        deleteButton.addActionListener(this::deleteTaxRate);
        backButton.addActionListener(this::goBack);
    }

    public void refreshTable() {
        String[] columnNames = {"Effective Date", "Tax Rate"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (TaxRate taxRate : taxCategory.getTaxRates()) {
            tableModel.addRow(new Object[]{
                taxRate.getEffectiveDate().toString(),
                taxRate.getTaxRate().toString()
            });
        }

        taxRateTable.setModel(tableModel);
    }

    private void openAddTaxRate(ActionEvent e) {
        TaxRateEditScreen addScreen = new TaxRateEditScreen(null, taxCategory, this);
        addScreen.setVisible(true);
        setVisible(false);
    }

    private void openEditTaxRate(ActionEvent e) {
        int selectedRow = taxRateTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a Tax Rate to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String effectiveDate = taxRateTable.getValueAt(selectedRow, 0).toString();
        TaxRate selectedTaxRate = taxCategory.findTaxRateByEffectiveDate(LocalDate.parse(effectiveDate));

        if (selectedTaxRate != null) {
            TaxRateEditScreen editScreen = new TaxRateEditScreen(selectedTaxRate, taxCategory, this);
            editScreen.setVisible(true);
            setVisible(false);
        }
    }

    private void deleteTaxRate(ActionEvent e) {
        int selectedRow = taxRateTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a Tax Rate to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String effectiveDate = taxRateTable.getValueAt(selectedRow, 0).toString();
        TaxRate selectedTaxRate = taxCategory.findTaxRateByEffectiveDate(LocalDate.parse(effectiveDate));

        if (selectedTaxRate != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this Tax Rate?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                taxCategory.removeTaxRate(selectedTaxRate.getEffectiveDate());
                refreshTable();
                JOptionPane.showMessageDialog(this, "Tax Rate deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void goBack(ActionEvent e) {
        taxCategoryEditScreen.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaxCategory dummyCategory = new TaxCategory("Food");
            TaxCategoryEditScreen dummyEditScreen = new TaxCategoryEditScreen(dummyCategory, null, null);
            TaxRateSelectionScreen screen = new TaxRateSelectionScreen(dummyCategory, dummyEditScreen);
            screen.setVisible(true);
        });
    }
}
