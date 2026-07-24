package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import PD.Cashier;
import PD.Sale;
import PD.Session;
import PD.Store;

public class CashierReportScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private Store store;
    private JTextField dateField;
    private JButton generateButton;
    private DefaultTableModel tableModel;
    private JTable reportTable;

    public CashierReportScreen(Store store) {
        this.store = store;

        setTitle("Cashier Report");
        setSize(450, 400);
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

        tableModel = new DefaultTableModel(new Object[]{"Cashier", "Total Sales"}, 0);
        reportTable = new JTable(tableModel);
        add(new JScrollPane(reportTable), BorderLayout.CENTER);

        generateButton.addActionListener(e -> generateReport());
    }

    private void generateReport() {
        try {
            LocalDate date = LocalDate.parse(dateField.getText().trim(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            tableModel.setRowCount(0);

            Map<String, BigDecimal> cashierTotals = new LinkedHashMap<>();

            for (Session session : store.getSessions()) {
                Cashier cashier = session.getCashier();
                for (Sale sale : session.getSales()) {
                    if (sale.getDateTime().toLocalDate().equals(date)) {
                        String name = cashier.getPerson().getName();
                        BigDecimal existing = cashierTotals.getOrDefault(name, BigDecimal.ZERO);
                        cashierTotals.put(name, existing.add(sale.calcTotal()));
                    }
                }
            }

            for (Map.Entry<String, BigDecimal> entry : cashierTotals.entrySet()) {
                tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date. Use MM/dd/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}