package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import PD.Item;
import PD.Sale;
import PD.SaleLineItem;
import PD.Session;
import PD.Store;

public class POSScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private Session session;
    private Store store;
    private Sale currentSale;

    private JTextField upcField;
    private JTextField quantityField;
    private JButton addItemButton;
    private JCheckBox taxFreeCheckBox;
    private JButton cancelButton;
    private JButton paymentButton;
    private JButton completeSaleButton;
    private JButton endSessionButton;

    private DefaultTableModel saleTableModel;
    private JTable saleTable;

    private JLabel subtotalLabel;
    private JLabel taxLabel;
    private JLabel totalLabel;
    private JLabel tenderedLabel;
    private JLabel changeLabel;

    public POSScreen(Session session, Store store) {
        this.session = session;
        this.store = store;

        setTitle("Point of Sale");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        startNewSale();

        String info = "Cashier: " + session.getCashier().getPerson().getName()
                + "   Register: " + session.getRegister().getNumber();
        JLabel infoLabel = new JLabel(info, SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(infoLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel entryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        entryPanel.add(new JLabel("UPC:"));
        upcField = new JTextField(12);
        entryPanel.add(upcField);
        entryPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField("1", 4);
        entryPanel.add(quantityField);
        addItemButton = new JButton("Add Item");
        entryPanel.add(addItemButton);
        taxFreeCheckBox = new JCheckBox("Tax Free");
        entryPanel.add(taxFreeCheckBox);

        centerPanel.add(entryPanel, BorderLayout.NORTH);

        saleTableModel = new DefaultTableModel(new Object[]{"Number", "Description", "Qty", "Subtotal"}, 0);
        saleTable = new JTable(saleTableModel);
        centerPanel.add(new JScrollPane(saleTable), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel totalsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        subtotalLabel = new JLabel("Subtotal: $0.00");
        taxLabel = new JLabel("Tax: $0.00");
        totalLabel = new JLabel("Total: $0.00");
        tenderedLabel = new JLabel("Amount Tendered: $0.00");
        changeLabel = new JLabel("Change: $0.00");
        totalsPanel.add(subtotalLabel);
        totalsPanel.add(taxLabel);
        totalsPanel.add(totalLabel);
        totalsPanel.add(tenderedLabel);
        totalsPanel.add(changeLabel);
        bottomPanel.add(totalsPanel, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cancelButton = new JButton("Cancel");
        paymentButton = new JButton("Payment");
        completeSaleButton = new JButton("Complete Sale");
        completeSaleButton.setEnabled(false);
        endSessionButton = new JButton("End Session");
        endSessionButton.setEnabled(false);
        actionPanel.add(cancelButton);
        actionPanel.add(paymentButton);
        actionPanel.add(completeSaleButton);
        actionPanel.add(endSessionButton);
        bottomPanel.add(actionPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        addItemButton.addActionListener(e -> addItemByUpc());
        taxFreeCheckBox.addActionListener(e -> {
            currentSale.setTaxFree(taxFreeCheckBox.isSelected());
            refreshTotals();
        });
        cancelButton.addActionListener(e -> cancelSale());
        paymentButton.addActionListener(e -> openPayment());
        completeSaleButton.addActionListener(e -> completeSale());
        endSessionButton.addActionListener(e -> openSessionEnd());
    }

    private void startNewSale() {
        currentSale = new Sale(false);
    }

    private void addItemByUpc() {
        String upcCode = upcField.getText().trim();
        String quantityText = quantityField.getText().trim();

        if (upcCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a UPC.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Item item = store.findItemByUpc(upcCode);
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Invalid UPC: " + upcCode, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityText.isEmpty() ? "1" : quantityText);
            if (quantity <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity must be a positive whole number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SaleLineItem sli = new SaleLineItem(currentSale, item, String.valueOf(quantity));
        currentSale.addSaleLineItem(sli);

        saleTableModel.addRow(new Object[]{
                item.getNumber(),
                item.getDescription(),
                quantity,
                sli.calcSubTotal()
        });

        upcField.setText("");
        quantityField.setText("1");
        upcField.requestFocus();

        refreshTotals();
    }

    private void refreshTotals() {
        subtotalLabel.setText("Subtotal: $" + currentSale.calcSubTotal());
        taxLabel.setText("Tax: $" + currentSale.calcTax());
        totalLabel.setText("Total: $" + currentSale.calcTotal());
    }

    private void cancelSale() {
        int confirm = JOptionPane.showConfirmDialog(this, "Cancel this sale? All scanned items will be cleared.",
                "Confirm Cancel", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            saleTableModel.setRowCount(0);
            taxFreeCheckBox.setSelected(false);
            tenderedLabel.setText("Amount Tendered: $0.00");
            changeLabel.setText("Change: $0.00");
            completeSaleButton.setEnabled(false);
            startNewSale();
            refreshTotals();
        }
    }

    private void openPayment() {
        if (currentSale.getSaleLineItemsCollection().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add at least one item before payment.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new PaymentScreen(currentSale, this).setVisible(true);
        setVisible(false);
    }

    public void onPaymentStepComplete() {
        tenderedLabel.setText("Amount Tendered: $" + currentSale.getTotalTendered());
        changeLabel.setText("Change: $" + currentSale.calcChange());
        completeSaleButton.setEnabled(currentSale.isPaymentEnough());
        setVisible(true);
    }

    private void completeSale() {
        session.addSale(currentSale);
        JOptionPane.showMessageDialog(this, "Sale completed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        saleTableModel.setRowCount(0);
        taxFreeCheckBox.setSelected(false);
        tenderedLabel.setText("Amount Tendered: $0.00");
        changeLabel.setText("Change: $0.00");
        completeSaleButton.setEnabled(false);
        endSessionButton.setEnabled(true);
        startNewSale();
        refreshTotals();
    }

    private void openSessionEnd() {
        new SessionEndScreen(session).setVisible(true);
        dispose();
    }
}