package PD;

import java.util.ArrayList;
import java.util.Collection;

public class Register {

    private String number;                   // Unique identifier for the register
    private CashDrawer cashDrawer;           // The cash drawer associated with this register
    private Collection<Session> sessions;    // Collection of sessions linked to this register

    /**
     * Default constructor initializes with default values.
     */
    public Register() {
        this.number = "";
        this.cashDrawer = new CashDrawer();  // Initialize a default cash drawer
        this.sessions = new ArrayList<>();  // Initialize an empty collection of sessions
    }

    /**
     * Constructor to initialize the register with a unique number.
     * 
     * @param number The unique identifier for the register.
     */
    public Register(String number) {
        this(); // Call the default constructor
        if (number == null || number.trim().isEmpty()) {
            throw new IllegalArgumentException("Register number cannot be null or empty.");
        }
        this.number = number;
    }

    /**
     * Returns a string representation of the register.
     * 
     * @return A string summarizing the register details.
     */
    @Override
    public String toString() {
        return "Register [Number=" + number + ", CashDrawer=" + cashDrawer + "]";
    }

    // Getters and Setters

    /**
     * Gets the register number.
     * 
     * @return The register number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the register number.
     * 
     * @param number The new register number.
     */
    public void setNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            throw new IllegalArgumentException("Register number cannot be null or empty.");
        }
        this.number = number;
    }

    /**
     * Gets the cash drawer associated with this register.
     * 
     * @return The cash drawer.
     */
    public CashDrawer getCashDrawer() {
        return cashDrawer;
    }

    /**
     * Sets the cash drawer for this register.
     * 
     * @param cashDrawer The new cash drawer.
     */
    public void setCashDrawer(CashDrawer cashDrawer) {
        if (cashDrawer == null) {
            throw new IllegalArgumentException("CashDrawer cannot be null.");
        }
        this.cashDrawer = cashDrawer;
    }

    /**
     * Gets the sessions linked to this register.
     * 
     * @return The collection of sessions.
     */
    public Collection<Session> getSessions() {
        return sessions;
    }

    /**
     * Adds a session to the register.
     * 
     * @param session The session to add.
     */
    public void addSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }
        sessions.add(session);
    }

    /**
     * Removes a session from the register.
     * 
     * @param session The session to remove.
     */
    public void removeSession(Session session) {
        sessions.remove(session);
    }
}