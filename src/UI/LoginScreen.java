package UI;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import PD.Cashier;
import PD.Register;
import PD.Session;
import PD.Store;
import DM.DataManager;

public class LoginScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField cashierNumberField;
    private JTextField registerNumberField;
    private JPasswordField passwordField;
    private JTextField startingCashField;
    private JButton loginButton;
    private Store store;

    public LoginScreen() {
        String filePath = "src/data/StoreData_v2024FALL.csv";
        DataManager.loadStoreDataOnce(Store.getInstance(), filePath);
        store = Store.getInstance();

        setTitle("Login");
        setSize(350, 260);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Cashier Number:"));
        cashierNumberField = new JTextField();
        formPanel.add(cashierNumberField);

        formPanel.add(new JLabel("Register Number:"));
        registerNumberField = new JTextField();
        formPanel.add(registerNumberField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Starting Cash:"));
        startingCashField = new JTextField();
        formPanel.add(startingCashField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        loginButton = new JButton("Login");
        buttonPanel.add(loginButton);

        loginButton.addActionListener(e -> attemptLogin());

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void attemptLogin() {
        String cashierNumber = cashierNumberField.getText().trim();
        String registerNumber = registerNumberField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String startingCashText = startingCashField.getText().trim();

        Cashier cashier = store.findCashier(cashierNumber);
        Register register = store.findRegister(registerNumber);

        if (cashier == null || register == null || !cashier.isAuthorized(password)) {
            JOptionPane.showMessageDialog(this, "Invalid cashier number, register number, or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal startingCash;
        try {
            startingCash = new BigDecimal(startingCashText.isEmpty() ? "0" : startingCashText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Starting Cash must be a valid amount.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        register.getCashDrawer().addCash(startingCash);

        Session session = new Session(cashier, register);
        store.addSession(session);
        cashier.addSession(session);
        register.addSession(session);

        new POSScreen(session, store).setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginScreen screen = new LoginScreen();
            screen.setVisible(true);
        });
    }
}