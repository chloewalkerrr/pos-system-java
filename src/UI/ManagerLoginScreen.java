package UI;

import javax.swing.*;
import java.awt.*;

public class ManagerLoginScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String MANAGER_PASSWORD = "manager123";

    private JPasswordField passwordField;
    private JButton loginButton;

    public ManagerLoginScreen() {
        setTitle("Manager Login");
        setSize(350, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        formPanel.add(new JLabel("Manager Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);
        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        loginButton = new JButton("Login");
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> attemptLogin());
    }

    private void attemptLogin() {
        String entered = new String(passwordField.getPassword());
        if (entered.equals(MANAGER_PASSWORD)) {
            new MainMenu().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManagerLoginScreen().setVisible(true));
    }
}