package PD;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

public class Store {

    private String number;
    private String name;
    private TreeMap<String, Cashier> cashiers; // Stores cashiers by ID
    private TreeMap<String, Register> registers; // Stores registers by ID
    private TreeMap<String, Item> items; // Stores items by item number
    private ArrayList<Session> sessions; // List of sessions
    private TreeMap<String, UPC> upcs; // Stores UPC mappings
    private TreeMap<String, TaxCategory> taxCategories; // Stores TaxCategories by name

    // Singleton instance
    private static Store instance;

    // Constructor is private to enforce Singleton pattern
    public Store() {
        this.cashiers = new TreeMap<>();
        this.registers = new TreeMap<>();
        this.items = new TreeMap<>();
        this.sessions = new ArrayList<>();
        this.upcs = new TreeMap<>();
        this.taxCategories = new TreeMap<>();
    }

    // Singleton implementation
    public static Store getInstance() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
    }

    // Name and number setters and getters
    public void setNumber(String number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    // Methods to manage Cashiers
    public void addCashier(Cashier cashier) {
        cashiers.put(cashier.getNumber(), cashier);
    }

    public Collection<Cashier> getCashiers() {
        return cashiers.values();
    }

    public Cashier findCashier(String number) {
        return cashiers.get(number);
    }

    // Methods to manage Registers
    public void addRegister(Register register) {
        registers.put(register.getNumber(), register);
    }

    public Collection<Register> getRegisters() {
        return registers.values();
    }

    public Register findRegister(String number) {
        return registers.get(number);
    }
    
    public void removeRegister(String number) {
        registers.remove(number);
    }

    // Methods to manage Items
    public void addItem(Item item) {
        items.put(item.getNumber(), item);
    }

    public Collection<Item> getItems() {
        return items.values();
    }

    public Item findItem(String number) {
        return items.get(number);
    }
    
    public Item findItemByUpc(String upcCode) {
        for (Item item : items.values()) {
            if (item.findUpc(upcCode) != null) {
                return item;
            }
        }
        return null;
    }

    // Methods to manage Tax Categories
    public void addTaxCategory(TaxCategory taxCategory) {
        taxCategories.put(taxCategory.getCategory(), taxCategory);
    }

    public Collection<TaxCategory> getTaxCategories() {
        return taxCategories.values();
    }

    public TaxCategory findTaxCategory(String name) {
        return taxCategories.get(name);
    }
    
    public void removeTaxCategory(String category) {
        taxCategories.remove(category);
    }

    // Methods to manage Sessions
    public void addSession(Session session) {
        sessions.add(session);
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }
    
    public java.util.List<Sale> getSalesForDate(java.time.LocalDate date) {
        java.util.List<Sale> result = new java.util.ArrayList<>();
        for (Session session : sessions) {
            for (Sale sale : session.getSales()) {
                if (sale.getDateTime().toLocalDate().equals(date)) {
                    result.add(sale);
                }
            }
        }
        return result;
    }

    public Session getLastSession() {
        return sessions.isEmpty() ? null : sessions.get(sessions.size() - 1);
    }

    // Methods to manage UPCs
    public void addUpc(UPC upc) {
        upcs.put(upc.getUPC(), upc);
    }

    public UPC findUpc(String upcCode) {
        return upcs.get(upcCode);
    }
}