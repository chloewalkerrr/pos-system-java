package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import PD.Item;
import PD.UPC;

public class UPCSelectionScreen extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable upcTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;
    private Item item;
    private JFrame parentScreen;

    public UPCSelectionScreen(Item item, JFrame parentScreen) {
        this.item = item;
        this.parentScreen = parentScreen;

        setTitle("UPC Selection for Item: " + item.getDescription());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"UPC Code"}, 0);
        upcTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(upcTable);

        updateUPCTable();

        JPanel buttonPanel = new JPanel(new FlowLayout());
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

        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addUPC());
        editButton.addActionListener(e -> editUPC());
        deleteButton.addActionListener(e -> deleteUPC());
        backButton.addActionListener(e -> {
            dispose();
            if (parentScreen != null) {
                parentScreen.setVisible(true);
            }
        });

        upcTable.getSelectionModel().addListSelectionListener(e -> {
            boolean isSelected = upcTable.getSelectedRow() != -1;
            editButton.setEnabled(isSelected);
            deleteButton.setEnabled(isSelected);
        });
    }

    public void updateUPCTable() {
        if (item != null && item.getUpcs() != null) {
            tableModel.setRowCount(0);
            for (UPC upc : item.getUpcs()) {
                tableModel.addRow(new Object[]{upc.getUPC()});
            }
        }
    }

    private void addUPC() {
        UPCEditScreen upcEditScreen = new UPCEditScreen(null, item, this);
        upcEditScreen.setVisible(true);
        setVisible(false);
    }

    private void editUPC() {
        int selectedRow = upcTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a UPC to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String upcCode = (String) tableModel.getValueAt(selectedRow, 0);
        UPC selectedUPC = item.findUpc(upcCode);

        if (selectedUPC != null) {
            UPCEditScreen upcEditScreen = new UPCEditScreen(selectedUPC, item, this);
            upcEditScreen.setVisible(true);
            setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "Unable to find selected UPC.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUPC() {
        int selectedRow = upcTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a UPC to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String upcCode = (String) tableModel.getValueAt(selectedRow, 0);
        UPC selectedUPC = item.findUpc(upcCode);

        if (selectedUPC != null) {
            int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected UPC?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                item.removeUpc(selectedUPC);
                updateUPCTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Unable to find selected UPC.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}