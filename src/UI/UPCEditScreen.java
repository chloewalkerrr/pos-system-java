package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import PD.UPC;
import PD.Item;

public class UPCEditScreen extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField upcCodeField;
    private JButton saveButton;
    private JButton cancelButton;
    private Item item;
    private UPC upc;
    private UPCSelectionScreen parentScreen;

    public UPCEditScreen(UPC upc, Item item, UPCSelectionScreen parentScreen) {
        this.upc = upc;
        this.item = item;
        this.parentScreen = parentScreen;

        setTitle(upc == null ? "Add UPC" : "Edit UPC");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(1, 2));
        inputPanel.add(new JLabel("UPC Code:"));
        upcCodeField = new JTextField(upc == null ? "" : upc.getUPC());
        inputPanel.add(upcCodeField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUPC();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelEdit();
            }
        });
    }

    /**
     * Saves the UPC to the item.
     */
    private void saveUPC() {
        String upcCode = upcCodeField.getText().trim();
        if (upcCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "UPC Code cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (upc == null) {
            // Adding a new UPC
            UPC newUpc = new UPC(upcCode);
            item.addUpc(newUpc);
        } else {
            // Editing an existing UPC
        	item.removeUpc(upc);
            upc.setUPC(upcCode);
            item.addUpc(upc);
        }

        parentScreen.updateUPCTable();
        parentScreen.setVisible(true);
        dispose();
    }

    /**
     * Cancels the editing process and returns to the UPCSelectionScreen.
     */
    private void cancelEdit() {
        parentScreen.setVisible(true);
        dispose();
    }
}
