package UI;

import PD.Store;
import PD.TaxCategory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TaxCategoryMaintenanceScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultListModel<String> listModel;
    private JList<String> categoryList;
    private JButton saveButton;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton cancelButton;
    private Store store;

    public TaxCategoryMaintenanceScreen() {
        setTitle("Tax Category Maintenance");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        store = Store.getInstance();

        JLabel label = new JLabel("Tax Category Maintenance", SwingConstants.CENTER);
        add(label, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        populateCategoryList();

        categoryList = new JList<>(listModel);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(categoryList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save");
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        cancelButton = new JButton("Cancel");

        saveButton.setEnabled(false);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        buttonPanel.add(saveButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(this::handleAdd);
        updateButton.addActionListener(this::handleUpdate);
        deleteButton.addActionListener(this::handleDelete);
        saveButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "All changes to tax categories have been saved in memory.", "Success", JOptionPane.INFORMATION_MESSAGE);
            populateCategoryList();
        });
        cancelButton.addActionListener(this::handleCancel);

        categoryList.addListSelectionListener(e -> {
            boolean isSelected = !categoryList.isSelectionEmpty();
            updateButton.setEnabled(isSelected);
            deleteButton.setEnabled(isSelected);
        });
    }

    private void populateCategoryList() {
        listModel.clear();
        for (TaxCategory category : store.getTaxCategories()) {
            listModel.addElement(category.getCategory());
        }
    }

    private void handleAdd(ActionEvent e) {
        new TaxCategoryEditScreen(null, store, this).setVisible(true);
        dispose();
    }

    private void handleUpdate(ActionEvent e) {
        String selected = categoryList.getSelectedValue();
        if (selected != null) {
            TaxCategory category = store.findTaxCategory(selected);
            new TaxCategoryEditScreen(category, store, this).setVisible(true);
            dispose();
        }
    }

    private void handleDelete(ActionEvent e) {
        String selected = categoryList.getSelectedValue();
        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this tax category?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                store.removeTaxCategory(selected);
                populateCategoryList();
                JOptionPane.showMessageDialog(this, "Tax Category deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void handleCancel(ActionEvent e) {
        new MainMenu().setVisible(true);
        dispose();
    }

    public void refreshList() {
        populateCategoryList();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaxCategoryMaintenanceScreen screen = new TaxCategoryMaintenanceScreen();
            screen.setVisible(true);
        });
    }
}