package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import PD.Store;
import DM.DataManager;

public class StoreMaintenanceScreen extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField nameField;
    private JTextField numberField;
    private JButton saveButton;
    private JButton cancelButton;
    private Store store;

    public StoreMaintenanceScreen() {
        store = Store.getInstance();

        setTitle("Store Maintenance");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Store Name:");
        nameField = new JTextField(store.getName() != null ? store.getName() : "");
        JLabel numberLabel = new JLabel("Store Number:");
        numberField = new JTextField(store.getNumber() != null ? store.getNumber() : "");

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(numberLabel);
        formPanel.add(numberField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> saveStoreDetails());
        cancelButton.addActionListener(e -> cancelChanges());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveStoreDetails() {
        String newName = nameField.getText().trim();
        String newNumber = numberField.getText().trim();

        if (newName.isEmpty() || newNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update in-memory Store object
        store.setName(newName);
        store.setNumber(newNumber);

        JOptionPane.showMessageDialog(this, "Store details saved successfully!");
        new MainMenu().setVisible(true);
        dispose();
    }


    private void cancelChanges() {
        new MainMenu().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StoreMaintenanceScreen screen = new StoreMaintenanceScreen();
            screen.setVisible(true);
        });
    }
}
