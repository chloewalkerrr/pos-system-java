package UI;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import PD.Session;

public class SessionEndScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private Session session;
    private JTextField cashCountField;
    private JButton calculateButton;
    private JLabel differenceLabel;
    private JButton doneButton;

    public SessionEndScreen(Session session) {
        this.session = session;

        setTitle("End of Session");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Cash Count:"));
        cashCountField = new JTextField();
        formPanel.add(cashCountField);

        formPanel.add(new JLabel("Difference:"));
        differenceLabel = new JLabel("$0.00");
        formPanel.add(differenceLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        calculateButton = new JButton("Calculate");
        doneButton = new JButton("Done");
        buttonPanel.add(calculateButton);
        buttonPanel.add(doneButton);

        calculateButton.addActionListener(e -> calculateDifference());
        doneButton.addActionListener(e -> {
            new LoginScreen().setVisible(true);
            dispose();
        });

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void calculateDifference() {
        try {
            BigDecimal countedCash = new BigDecimal(cashCountField.getText().trim());
            BigDecimal diff = session.calcCashCountDiff(countedCash);
            differenceLabel.setText("$" + diff);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid cash amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}