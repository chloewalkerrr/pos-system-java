package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import PD.Store;

public class StoreEditScreen extends JFrame {

    private static final long serialVersionUID = 1L; // Added serialVersionUID
    private JTextField nameField;
    private JTextField numberField;
    private JButton saveButton;
    private JButton cancelButton;

    private MainMenu mainMenu;
	private Store store;

    public StoreEditScreen(Store store, MainMenu mainMenu) {
    	store = Store.getInstance();
        // Initialize frame
        setTitle("Edit Store");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        this.store = store;
        this.mainMenu = mainMenu;

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel nameLabel = new JLabel("Store Name:");
        JLabel numberLabel = new JLabel("Store Number:");

        nameField = new JTextField();
        numberField = new JTextField();

        // Populate fields with store data
        if (store != null) {
            nameField.setText(store.getName());
            numberField.setText(store.getNumber());
        }

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(numberLabel);
        formPanel.add(numberField);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStore();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelEdit();
            }
        });

        // Add components to frame
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Save the store data and return to the main menu.
     */
    private void saveStore() {
        String name = nameField.getText().trim();
        String number = numberField.getText().trim();

        if (name.isEmpty() || number.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Store name and number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        store.setName(name);
        store.setNumber(number);

        JOptionPane.showMessageDialog(this, "Store updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        mainMenu.setVisible(true);
        dispose();
    }

    /**
     * Cancel the edit and return to the main menu.
     */
    private void cancelEdit() {
        mainMenu.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        // Run the screen independently for testing purposes
        SwingUtilities.invokeLater(() -> {
            Store dummyStore = Store.getInstance();
            dummyStore.setName("Test Store");
            dummyStore.setNumber("001");
            MainMenu dummyMainMenu = new MainMenu();
            StoreEditScreen screen = new StoreEditScreen(dummyStore, dummyMainMenu);
            screen.setVisible(true);
        });
    }
}
