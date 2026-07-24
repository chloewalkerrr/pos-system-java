package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import PD.Cashier;
import PD.Person;
import PD.Store;

public class CashierEditScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtCashierNumber;
    private JTextField txtName;
    private JTextField txtSSN;
    private JTextField txtAddress;
    private JTextField txtCity;
    private JTextField txtState;
    private JTextField txtZip;
    private JTextField txtPhone;
    private JTextField txtPassword;
    private JButton saveButton;
    private JButton cancelButton;
    private Store store;
    private Cashier cashier;
    private CashierMaintenanceScreen parentScreen;

    public CashierEditScreen(Cashier cashier, Store store, CashierMaintenanceScreen parentScreen) {
        // Initialize the frame
        setTitle("Cashier Edit");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        this.cashier = cashier;
        this.store = store;
        this.parentScreen = parentScreen;

        // Create input fields and labels
        JPanel inputPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Cashier Number:"));
        txtCashierNumber = new JTextField();
        inputPanel.add(txtCashierNumber);

        inputPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        inputPanel.add(txtName);

        inputPanel.add(new JLabel("SSN:"));
        txtSSN = new JTextField();
        inputPanel.add(txtSSN);

        inputPanel.add(new JLabel("Address:"));
        txtAddress = new JTextField();
        inputPanel.add(txtAddress);

        inputPanel.add(new JLabel("City:"));
        txtCity = new JTextField();
        inputPanel.add(txtCity);

        inputPanel.add(new JLabel("State:"));
        txtState = new JTextField();
        inputPanel.add(txtState);

        inputPanel.add(new JLabel("Zip:"));
        txtZip = new JTextField();
        inputPanel.add(txtZip);

        inputPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField();
        inputPanel.add(txtPhone);

        inputPanel.add(new JLabel("Password:"));
        txtPassword = new JTextField();
        inputPanel.add(txtPassword);

        // Populate fields if editing an existing cashier
        if (cashier != null) {
            txtCashierNumber.setText(cashier.getNumber());
            txtCashierNumber.setEnabled(false); // Prevent changing the number
            txtName.setText(cashier.getPerson().getName());
            txtSSN.setText(cashier.getPerson().getSsn());
            txtAddress.setText(cashier.getPerson().getAddress());
            txtCity.setText(cashier.getPerson().getCity());
            txtState.setText(cashier.getPerson().getState());
            txtZip.setText(cashier.getPerson().getZip());
            txtPhone.setText(cashier.getPerson().getPhone());
            txtPassword.setText(""); // Avoid showing the password for security reasons
        }

        // Create buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add action listeners
        saveButton.addActionListener(e -> saveCashier());
        cancelButton.addActionListener(e -> {
            if (parentScreen != null) {
                parentScreen.setVisible(true); // Return to parent screen
            }
            dispose();
        });

        // Add components to the frame
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Save the cashier data and update the store.
     */
    private void saveCashier() {
        try {
            String number = txtCashierNumber.getText().trim();
            String name = txtName.getText().trim();
            String ssn = txtSSN.getText().trim();
            String address = txtAddress.getText().trim();
            String city = txtCity.getText().trim();
            String state = txtState.getText().trim();
            String zip = txtZip.getText().trim();
            String phone = txtPhone.getText().trim();
            String password = txtPassword.getText().trim();

            if (number.isEmpty() || name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cashier Number, Name, and Password are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Person person = new Person(name, ssn, address, city, state, zip, phone);

            if (cashier == null) {
                // Adding a new cashier
                cashier = new Cashier(number, person, password);
                store.addCashier(cashier);
            } else {
                // Updating an existing cashier
                cashier.getPerson().setName(name);
                cashier.getPerson().setSsn(ssn);
                cashier.getPerson().setAddress(address);
                cashier.getPerson().setCity(city);
                cashier.getPerson().setState(state);
                cashier.getPerson().setZip(zip);
                cashier.getPerson().setPhone(phone);
            }

            JOptionPane.showMessageDialog(this, "Cashier saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            if (parentScreen != null) {
                parentScreen.refreshCashierList();
                parentScreen.setVisible(true);
            }
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving cashier: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    //public static void main(String[] args) {
      //  SwingUtilities.invokeLater(() -> {
        //    Store store = Store.getInstance(); // Pass a manually created store
          //  CashierEditScreen screen = new CashierEditScreen(null, store, null);
            //screen.setVisible(true);
        //});
    //}
}
