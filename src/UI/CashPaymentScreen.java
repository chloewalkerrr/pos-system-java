package UI;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import PD.Cash;
import PD.Sale;

public class CashPaymentScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private Sale sale;
    private PaymentScreen parentScreen;
    private JTextField tenderedField;
    private JButton saveButton;
    private JButton cancelButton;

    public CashPaymentScreen(Sale sale, PaymentScreen parentScreen) {
        this.sale = sale;
        this.parentScreen = parentScreen;

        setTitle("Cash Payment");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Cash Tendered:"));
        tenderedField = new JTextField();
        formPanel.add(tenderedField);

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
            Cash cash = new Cash(tendered, tendered);
            sale.addPayment(cash);
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