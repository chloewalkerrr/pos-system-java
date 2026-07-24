package UI;

import javax.swing.*;
import java.awt.*;

public class StartScreen extends JFrame {

    private static final long serialVersionUID = 1L;

    public StartScreen() {
        setTitle("Welcome");
        setSize(350, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Point of Sale System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton managerButton = new JButton("Manager Login");
        JButton cashierButton = new JButton("Cashier Login");
        buttonPanel.add(managerButton);
        buttonPanel.add(cashierButton);
        add(buttonPanel, BorderLayout.CENTER);

        managerButton.addActionListener(e -> {
            new ManagerLoginScreen().setVisible(true);
            dispose();
        });

        cashierButton.addActionListener(e -> {
            new LoginScreen().setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StartScreen().setVisible(true));
    }
}