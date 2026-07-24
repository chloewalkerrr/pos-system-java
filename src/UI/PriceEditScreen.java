package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import PD.Price;
import PD.Item;

public class PriceEditScreen extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtPrice;
    private JTextField txtEffectiveDate;
    private JButton saveButton;
    private JButton cancelButton;

    private Item item;
    private Price price;
    private JFrame parentScreen; // Parent screen (PriceSelectionScreen or any other JFrame)

    public PriceEditScreen(Item item, Price price, JFrame parentScreen) {
        // Initialize frame
        setTitle("Price Edit");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        this.item = item;
        this.price = price;
        this.parentScreen = parentScreen;

        // Create input fields and labels
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Price:"));
        txtPrice = new JTextField();
        inputPanel.add(txtPrice);

        inputPanel.add(new JLabel("Effective Date (MM/dd/yyyy):"));
        txtEffectiveDate = new JTextField();
        inputPanel.add(txtEffectiveDate);

        // Populate fields if editing an existing price
        if (price != null) {
            txtPrice.setText(price.getPrice().toString());
            txtEffectiveDate.setText(formatDate(price.getEffectiveDate(), "MM/dd/yyyy"));
        }

        // Create buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePrice();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (parentScreen != null) {
                    parentScreen.setVisible(true); // Return to parent screen
                }
            }
        });

        // Add components to the frame
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Save the price data and update the item.
     */
    private void savePrice() {
        try {
            String priceText = txtPrice.getText().trim();
            String effectiveDate = txtEffectiveDate.getText().trim();

            if (priceText.isEmpty() || effectiveDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BigDecimal priceValue = new BigDecimal(priceText);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate parsedDate = parseDate(effectiveDate, formatter);

            if (price == null) {
                // Adding a new price
                price = new Price(priceValue, parsedDate);
                item.addPrice(price);
            } else {
                // Updating an existing price
            	item.removePrice(price.getEffectiveDate());
                price.setPrice(priceValue);
                price.setEffectiveDate(parsedDate);
                item.addPrice(price);
            }

            JOptionPane.showMessageDialog(this, "Price saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            if (parentScreen instanceof PriceSelectionScreen) {
                ((PriceSelectionScreen) parentScreen).refreshPriceList(); // Refresh the price list in the parent screen
            }
            if (parentScreen != null) {
                parentScreen.setVisible(true);
            }
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving price: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Parse a date string into a LocalDate object.
     */
    private LocalDate parseDate(String date, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use MM/dd/yyyy.");
        }
    }

    /**
     * Format a LocalDate object into a string.
     */
    private String formatDate(LocalDate date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    public static void main(String[] args) {
        // Run the screen independently for testing purposes
        SwingUtilities.invokeLater(() -> {
            Item dummyItem = new Item("1234", "Dummy Item");
            PriceEditScreen screen = new PriceEditScreen(dummyItem, null, null);
            screen.setVisible(true);
        });
    }
}
