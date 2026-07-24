package UI;

import javax.swing.*;

public class UIUtils {
    /**
     * Show a confirmation dialog before canceling.
     *
     * @param parentComponent The parent JFrame where the dialog is displayed.
     * @return true if the user confirms the cancellation, false otherwise.
     */
    public static boolean confirmCancel(JFrame parentComponent) {
        int confirm = JOptionPane.showConfirmDialog(
            parentComponent,
            "Are you sure you want to cancel? Unsaved changes will be lost.",
            "Confirm Cancel",
            JOptionPane.YES_NO_OPTION
        );

        return confirm == JOptionPane.YES_OPTION; // Return true if "Yes" is selected
    }
}
