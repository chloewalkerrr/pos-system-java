package UI;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import PD.Check;
import PD.Sale;

public class CheckPaymentScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private Sale sale;
    private PaymentScreen parentScreen;
    private JTextField tenderedField;
    private JTextField routingField;
    private JTextField accountField;
    private JTextField checkNumberField;
    private JButton saveButton;
    private JButton cancelButton;

    public CheckPaymentScreen(Sale sale, PaymentScreen parentScreen) {
        this.sale = sale;
        this.parentScreen = parentScreen;

        setTitle("Check Payment");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Check Tendered:"));
        tenderedField = new JTextField();
        formPanel.add(tenderedField);

        formPanel.add(new JLabel("Routing Number:"));
        routingField = new JTextField();
        formPanel.add(routingField);

        formPanel.add(new JLabel("Account Number:"));
        accountField = new JTextField();
        formPanel.add(accountField);

        formPanel.add(new JLabel("Check Number:"));
        checkNumberField = new JTextField();
        formPanel.add(checkNumberField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> cancel());

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void save() {
        try {
            BigDecimal tendered = new BigDecimal(tenderedField.getText().trim());
            String routing = routingField.getText().trim();
            String account = accountField.getText().trim();
            String checkNumber = checkNumberField.getText().trim();

            if (routing.isEmpty() || account.isEmpty() || checkNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Check check = new Check(tendered, routing, account, checkNumber);
            sale.addPayment(check);
            parentScreen.refreshLabels();
            parentScreen.setVisible(true);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancel() {
        parentScreen.setVisible(true);
        dispose();
    }
}