package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import PD.Item;
import PD.Sale;
import PD.SaleLineItem;
import PD.Store;

public class ItemReportScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private Store store;
    private JTextField dateField;
    private JButton generateButton;
    private DefaultTableModel tableModel;
    private JTable reportTable;

    public ItemReportScreen(Store store) {
        this.store = store;

        setTitle("Item Report");
        setSize(500, 400);
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

        tableModel = new DefaultTableModel(new Object[]{"Item Number", "Description", "Quantity Sold", "Total Sales"}, 0);
        reportTable = new JTable(tableModel);
        add(new JScrollPane(reportTable), BorderLayout.CENTER);

        generateButton.addActionListener(e -> generateReport());
    }

    private void generateReport() {
        try {
            LocalDate date = LocalDate.parse(dateField.getText().trim(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            tableModel.setRowCount(0);

            Map<String, Object[]> itemTotals = new LinkedHashMap<>();

            for (Sale sale : store.getSalesForDate(date)) {
                for (SaleLineItem sli : sale.getSaleLineItemsCollection()) {
                    Item item = sli.getItem();
                    Object[] existing = itemTotals.get(item.getNumber());
                    int quantity = sli.getQuantity();
                    BigDecimal subtotal = sli.calcSubTotal();

                    if (existing == null) {
                        itemTotals.put(item.getNumber(), new Object[]{item.getDescription(), quantity, subtotal});
                    } else {
                        int existingQty = (int) existing[1];
                        BigDecimal existingTotal = (BigDecimal) existing[2];
                        itemTotals.put(item.getNumber(), new Object[]{item.getDescription(), existingQty + quantity, existingTotal.add(subtotal)});
                    }
                }
            }

            for (Map.Entry<String, Object[]> entry : itemTotals.entrySet()) {
                tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()[0], entry.getValue()[1], entry.getValue()[2]});
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date. Use MM/dd/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}