package UI;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import PD.Sale;
import PD.Store;

public class DailySalesReportScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private Store store;
    private JTextField dateField;
    private JButton generateButton;
    private JLabel salesCountLabel;
    private JLabel totalSalesLabel;

    public DailySalesReportScreen(Store store) {
        this.store = store;

        setTitle("Daily Sales Report");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(new JLabel("Date (MM/dd/yyyy):"));
        dateField = new JTextField(10);
        topPanel.add(dateField);
        generateButton = new JButton("Generate");
        topPanel.add(generateButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel resultsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        salesCountLabel = new JLabel("Number of Sales: 0");
        totalSalesLabel = new JLabel("Total Sales: $0.00");
        resultsPanel.add(salesCountLabel);
        resultsPanel.add(totalSalesLabel);
        add(resultsPanel, BorderLayout.CENTER);

        generateButton.addActionListener(e -> generateReport());
    }

    private void generateReport() {
        try {
            LocalDate date = LocalDate.parse(dateField.getText().trim(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));

            java.util.List<Sale> sales = store.getSalesForDate(date);
            BigDecimal total = BigDecimal.ZERO;
            for (Sale sale : sales) {
                total = total.add(sale.calcTotal());
            }

            salesCountLabel.setText("Number of Sales: " + sales.size());
            totalSalesLabel.setText("Total Sales: $" + total);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date. Use MM/dd/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}