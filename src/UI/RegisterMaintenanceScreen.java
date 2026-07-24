package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import PD.Register;
import PD.Store;

public class RegisterMaintenanceScreen extends JFrame {

    private static final long serialVersionUID = 1L; // Added serialVersionUID

    private DefaultListModel<String> registerListModel;
    private JList<String> registerList;
    private JButton saveButton;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton cancelButton;
    private Store store;

    public RegisterMaintenanceScreen() {
        // Set up the frame
        setTitle("Register Maintenance");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Retrieve the Store instance
        store = Store.getInstance(); // Replace with your singleton or global store instance retrieval method

        // Create the register list model and populate it
        registerListModel = new DefaultListModel<>();
        populateRegisterList();

        // Create UI components
        registerList = new JList<>(registerListModel);
        registerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(registerList);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save");
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        cancelButton = new JButton("Cancel");

        // Enable only the Add button by default
        saveButton.setEnabled(false);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        // Add buttons to the panel
        buttonPanel.add(saveButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);

        // Add action listeners for buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditRegisterScreen(null);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = registerList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    String selectedRegisterId = registerListModel.getElementAt(selectedIndex);
                    Register selectedRegister = store.findRegister(selectedRegisterId);
                    openEditRegisterScreen(selectedRegister);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRegister();
            }
        });
        
        saveButton.addActionListener(e -> {
            saveRegisterChanges();
        });

        cancelButton.addActionListener(e -> {
            cancelChanges();
        });

        // Add selection listener to enable/disable buttons
        registerList.addListSelectionListener(e -> {
            boolean isSelected = !registerList.isSelectionEmpty();
            updateButton.setEnabled(isSelected);
            deleteButton.setEnabled(isSelected);
        });

        // Add components to frame
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Populate the register list from the store.
     */
    private void populateRegisterList() {
        registerListModel.clear();
        List<Register> registers = store.getRegisters().stream().toList();
        for (Register register : registers) {
            registerListModel.addElement(register.getNumber());
        }
    }

    /**
     * Add a register to the list.
     *
     * @param register The register to add.
     */
    public void addRegister(Register register) {
        store.addRegister(register);
        populateRegisterList(); // Refresh the list after adding
    }

    /**
     * Open the Edit Register screen.
     *
     * @param register The register to edit, or null for adding a new register.
     */
    private void openEditRegisterScreen(Register register) {
        RegisterEditScreen editRegisterScreen = new RegisterEditScreen(register, this);
        editRegisterScreen.setVisible(true);
        dispose();
    }

    /**
     * Delete the selected register from the list and the store.
     */
    private void deleteSelectedRegister() {
        int selectedIndex = registerList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String selectedRegisterId = registerListModel.getElementAt(selectedIndex);
            Register register = store.findRegister(selectedRegisterId);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this register?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                store.getRegisters().remove(register); // Remove from store
                populateRegisterList(); // Refresh the list
                JOptionPane.showMessageDialog(this, "Register deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    
    /**
     * Mark the screen as modified and enable the Save button.
     */

   
    /**
     * Save the changes made in the current screen.
     */
    private void saveRegisterChanges() {
        JOptionPane.showMessageDialog(this, "All changes to registers have been saved in memory.", "Success", JOptionPane.INFORMATION_MESSAGE);
        // Refresh the list to reflect changes in memory
        populateRegisterList();
    }

    /**
     * Cancel the changes and return to the main menu.
     */
    private void cancelChanges() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel? Unsaved changes will be lost.",
                "Confirm Cancel",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Dispose the current screen and navigate back to the MainMenu
            new MainMenu().setVisible(true);
            dispose();
        }
    }

    
    /**
     * Refresh the register list on the screen.
     */
    public void refreshRegisterList() {
        populateRegisterList();
    }

    public static void main(String[] args) {
        // Run the screen independently for testing purposes
        SwingUtilities.invokeLater(() -> {
            RegisterMaintenanceScreen screen = new RegisterMaintenanceScreen();
            screen.setVisible(true);
        });
    }
}