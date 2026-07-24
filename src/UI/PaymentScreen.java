package UI;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import PD.Sale;

public class PaymentScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private Sale sale;
    private POSScreen posScreen;

    private JLabel amountDueLabel;
    private JLabel amountTenderedLabel;
    private JButton cashButton;
    private JButton checkButton;
    private JButton creditButton;
    private JButton paymentCompleteButton;

    public PaymentScreen(Sale sale, POSScreen posScreen) {
        this.sale = sale;
        this.posScreen = posScreen;

        setTitle("Payment");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        amountDueLabel = new JLabel();
        amountTenderedLabel = new JLabel();
        infoPanel.add(amountDueLabel);
        infoPanel.add(amountTenderedLabel);
        add(infoPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cashButton = new JButton("Cash");
        checkButton = new JButton("Check");
        creditButton = new JButton("Credit");
        paymentCompleteButton = new JButton("Payment Complete");

        buttonPanel.add(cashButton);
        buttonPanel.add(checkButton);
        buttonPanel.add(creditButton);
        buttonPanel.add(paymentCompleteButton);
        add(buttonPanel, BorderLayout.CENTER);

        cashButton.addActionListener(e -> openCashScreen());
        checkButton.addActionListener(e -> openCheckScreen());
        creditButton.addActionListener(e -> openCreditScreen());
        paymentCompleteButton.addActionListener(e -> completePaymentStep());

        refreshLabels();
    }

    public void refreshLabels() {
        BigDecimal due = sale.calcTotal().subtract(sale.getTotalTendered());
        if (due.compareTo(BigDecimal.ZERO) < 0) {
            due = BigDecimal.ZERO;
        }
        amountDueLabel.setText("Amount Due: $" + due);
        amountTenderedLabel.setText("Amount Tendered: $" + sale.getTotalTendered());
        paymentCompleteButton.setEnabled(sale.isPaymentEnough());
    }

    private void openCashScreen() {
        new CashPaymentScreen(sale, this).setVisible(true);
        setVisible(false);
    }

    private void openCheckScreen() {
        new CheckPaymentScreen(sale, this).setVisible(true);
        setVisible(false);
    }

    private void openCreditScreen() {
        new CreditPaymentScreen(sale, this).setVisible(true);
        setVisible(false);
    }

    private void completePaymentStep() {
        posScreen.onPaymentStepComplete();
        dispose();
    }
}