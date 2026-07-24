package UI;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import PD.Credit;
import PD.Sale;

public class CreditPaymentScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private Sale sale;
    private PaymentScreen parentScreen;
    private JTextField tenderedField;
    private JTextField cardTypeField;
    private JTextField accountField;
    private JTextField expireDateField;
    private JButton saveButton;
    private JButton cancelButton;

    public CreditPaymentScreen(Sale sale, PaymentScreen parentScreen) {
        this.sale = sale;
        this.parentScreen = parentScreen;

        setTitle("Credit Payment");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Credit Tendered:"));
        tenderedField = new JTextField();
        formPanel.add(tenderedField);

        formPanel.add(new JLabel("Card Type:"));
        cardTypeField = new JTextField();
        formPanel.add(cardTypeField);

        formPanel.add(new JLabel("Account Number:"));
        accountField = new JTextField();
        formPanel.add(accountField);

        formPanel.add(new JLabel("Expire Date (MM/dd/yyyy):"));
        expireDateField = new JTextField();
        formPanel.add(expireDateField);

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
            String cardType = cardTypeField.getText().trim();
            String account = accountField.getText().trim();
            String expireDateText = expireDateField.getText().trim();

            if (cardType.isEmpty() || account.isEmpty() || expireDateText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate expireDate = LocalDate.parse(expireDateText, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            Credit credit = new Credit(tendered, cardType, account, expireDate);
            sale.addPayment(credit);
            parentScreen.refreshLabels();
            parentScreen.setVisible(true);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use MM/dd/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancel() {
        parentScreen.setVisible(true);
        dispose();
    }
}