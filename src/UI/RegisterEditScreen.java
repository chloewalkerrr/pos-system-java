package UI;

import javax.swing.*;
import PD.Register;
import PD.Store;

public class RegisterEditScreen extends JFrame {
    private static final long serialVersionUID = 1L;
    private Register register;
    private JTextField registerNumberField;
    private JButton saveButton;
    private JButton cancelButton;
    private RegisterMaintenanceScreen parentScreen;

    public RegisterEditScreen(Register register, RegisterMaintenanceScreen parentScreen) {
        this.register = register;
        this.parentScreen = parentScreen;

        initUI();
    }

    private void initUI() {
        setTitle("Edit Register");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Register Number
        JLabel registerNumberLabel = new JLabel("Register Number:");
        registerNumberField = new JTextField(register != null ? register.getNumber() : "");
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        registerPanel.add(registerNumberLabel);
        registerPanel.add(registerNumberField);

        // Buttons
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> saveRegister());
        cancelButton.addActionListener(e -> cancelChanges());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add components to frame
        add(registerPanel);
        add(buttonPanel);
    }

    private void saveRegister() {
        try {
            String registerNumber = registerNumberField.getText().trim();
            if (registerNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Register number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (register == null) {
                // Create new register
                register = new Register(registerNumber);
                Store.getInstance().addRegister(register);
            } else {
                // Update existing register
                String oldNumber = register.getNumber();
                register.setNumber(registerNumber);
                if (!oldNumber.equals(registerNumber)) {
                    Store.getInstance().removeRegister(oldNumber);
                    Store.getInstance().addRegister(register);
                }
            }

            JOptionPane.showMessageDialog(this, "Register saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Refresh the parent screen and close this screen
            parentScreen.refreshRegisterList();
            parentScreen.setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving register: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelChanges() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel? Unsaved changes will be lost.",
                "Confirm Cancel",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            parentScreen.setVisible(true);
            dispose();
        }
    }
}
