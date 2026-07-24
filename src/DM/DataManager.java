package DM;

import PD.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class DataManager {

    public static void loadStoreData(Store store, String filePath) {
        System.out.println("Loading data from: " + filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                String dataType = fields[0].trim();
                System.out.println("Processing data type: " + dataType);


                switch (dataType) {
                	case "Store":
                		System.out.println("Loading store: " + fields[1]);
                		store.setName(fields[1].trim());
                		store.setNumber(fields.length > 2 && !fields[2].trim().isEmpty() ? fields[2].trim() : "00000");
                    break;

                    case "TaxCategory":
                        addTaxCategory(store, fields);
                        break;
                    case "Cashier":
                        addCashier(store, fields);
                        break;
                    case "Register":
                        addRegister(store, fields);
                        break;
                    case "Item":
                        addItem(store, fields);
                        break;
                    case "Session":
                        addSession(store, fields);
                        break;
                    case "Sale":
                        addSale(store, fields);
                        break;
                    case "SaleLineItem":
                        addSaleLineItem(store, fields);
                        break;
                    case "Payment":
                        addPayment(store, fields);
                        break;
                    default:
                        System.out.println("Skipping unrecognized data type: " + dataType);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private static void addTaxCategory(Store store, String[] fields) {
        try {
            String categoryName = fields[1].trim();
            BigDecimal taxRate = new BigDecimal(fields[2].trim());
            LocalDate effectiveDate = parseDate(fields[3].trim(), "M/d/yy");
            TaxCategory taxCategory = new TaxCategory(categoryName);
            taxCategory.addTaxRate(new TaxRate(effectiveDate, taxRate));
            store.addTaxCategory(taxCategory);
        } catch (Exception e) {
            System.out.println("Skipping invalid TaxCategory entry: " + e.getMessage());
        }
    }

    private static void addCashier(Store store, String[] fields) {
        try {
            String number = fields[1].trim();
            String name = fields[2].trim();
            String ssn = fields[3].trim();
            String address = fields[4].trim();
            String city = fields[5].trim();
            String state = fields[6].trim();
            String zip = fields[7].trim();
            String phone = fields[8].trim();
            String password = fields[9].trim();
            Person person = new Person(name, ssn, address, city, state, zip, phone);
            Cashier cashier = new Cashier(number, person, password);
            store.addCashier(cashier);
        } catch (Exception e) {
            System.out.println("Skipping invalid Cashier entry: " + e.getMessage());
        }
    }

    private static void addRegister(Store store, String[] fields) {
        try {
            String number = fields[1].trim();
            Register register = new Register(number);
            store.addRegister(register);
        } catch (Exception e) {
            System.out.println("Skipping invalid Register entry: " + e.getMessage());
        }
    }

    private static void addItem(Store store, String[] fields) {
        try {
            String itemNumber = fields[1].trim();
            String upcCode = fields[2].trim();
            String description = fields[3].trim();
            String taxCategoryName = fields[4].trim();
            TaxCategory taxCategory = store.findTaxCategory(taxCategoryName);
            if (taxCategory == null) throw new IllegalArgumentException("TaxCategory not found: " + taxCategoryName);

            Item item = new Item(itemNumber, description);
            item.setTaxCategory(taxCategory);

            BigDecimal price = new BigDecimal(fields[5].trim());
            LocalDate effectiveDate = parseDate(fields[6].trim(), "M/d/yy");
            item.addPrice(new Price(price, effectiveDate));

            if (fields.length > 9 && !fields[7].trim().isEmpty()) {
                BigDecimal promoPrice = new BigDecimal(fields[7].trim());
                LocalDate promoStart = parseDate(fields[8].trim(), "M/d/yy");
                LocalDate promoEnd = parseDate(fields[9].trim(), "M/d/yy");
                item.addPrice(new PromoPrice(promoPrice, promoStart, promoEnd));
            }

            if (!upcCode.isEmpty()) {
                UPC upc = new UPC(upcCode);
                item.addUpc(upc);
                store.addUpc(upc);
            }

            store.addItem(item);
        } catch (Exception e) {
            System.out.println("Skipping invalid Item entry: " + e.getMessage());
        }
    }

    private static void addSession(Store store, String[] fields) {
        try {
            Cashier cashier = store.findCashier(fields[1].trim());
            Register register = store.findRegister(fields[2].trim());
            if (cashier == null || register == null) {
                throw new IllegalArgumentException("Cashier or Register not found.");
            }
            Session session = new Session(cashier, register);
            store.addSession(session);
        } catch (Exception e) {
            System.out.println("Skipping invalid Session entry: " + e.getMessage());
        }
    }

    private static void addSale(Store store, String[] fields) {
        try {
            Session session = store.getLastSession();
            if (session == null) {
                throw new IllegalStateException("No session available for sale.");
            }
            boolean taxFree = fields[1].trim().equalsIgnoreCase("Y");
            Sale sale = new Sale(taxFree);
            session.addSale(sale);
        } catch (Exception e) {
            System.out.println("Skipping invalid Sale entry: " + e.getMessage());
        }
    }

    private static void addSaleLineItem(Store store, String[] fields) {
        try {
            Session session = store.getLastSession();
            if (session == null) {
                throw new IllegalStateException("No session available for SaleLineItem.");
            }
            Sale sale = session.getLastSale();
            if (sale == null) {
                throw new IllegalStateException("No sale available for SaleLineItem.");
            }

            String itemNumber = fields[1].trim();
            Item item = store.findItem(itemNumber);
            if (item == null) {
                throw new IllegalArgumentException("Item not found for SaleLineItem: " + itemNumber);
            }

            int quantity = Integer.parseInt(fields[2].trim());
            SaleLineItem sli = new SaleLineItem(sale, item, String.valueOf(quantity));
            sale.addSaleLineItem(sli);
        } catch (Exception e) {
            System.out.println("Skipping invalid SaleLineItem entry: " + e.getMessage());
        }
    }

    private static void addPayment(Store store, String[] fields) {
        try {
            Session session = store.getLastSession();
            if (session == null) {
                throw new IllegalStateException("No session available for Payment.");
            }
            Sale sale = session.getLastSale();
            if (sale == null) {
                throw new IllegalStateException("No sale available for Payment.");
            }

            String paymentType = fields[1].trim();
            BigDecimal amount = new BigDecimal(fields[2].trim());

            switch (paymentType) {
                case "Cash":
                    BigDecimal tendered = new BigDecimal(fields[3].trim());
                    sale.addPayment(new Cash(amount, tendered));
                    break;
                case "Credit":
                    String cardType = fields[4].trim();
                    String accountNumber = fields[5].trim();
                    LocalDate expiryDate = parseDate(fields[6].trim(), "M/d/yyyy");
                    sale.addPayment(new Credit(amount, cardType, accountNumber, expiryDate));
                    break;
                case "Check":
                    String routingNumber = fields[4].trim();
                    String accountNumberCheck = fields[5].trim();
                    String checkNumber = fields[6].trim();
                    sale.addPayment(new Check(amount, routingNumber, accountNumberCheck, checkNumber));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown payment type: " + paymentType);
            }
        } catch (Exception e) {
            System.out.println("Skipping invalid Payment entry: " + e.getMessage());
        }
    }
    
    private static boolean dataLoaded = false;

    public static void loadStoreDataOnce(Store store, String filePath) {
        if (!dataLoaded) {
            loadStoreData(store, filePath);
            dataLoaded = true;
        }
    }

    private static LocalDate parseDate(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(date, formatter);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z yyyy");
        return dateTime.atZone(java.time.ZoneId.systemDefault()).format(formatter);
    }
    
    public static String formatDateRange(LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");
        ZoneId zone = ZoneId.systemDefault();
        return String.format("%s-%s",
                startDate.atStartOfDay(zone).format(formatter),
                endDate.atStartOfDay(zone).format(formatter));
    }

    
    // New methods for 2.3 Requirements
 // Store Management
    public static void updateStoreDetails(Store store, String name, String number) {
        store.setName(name);
        store.setNumber(number);
    }

    // Cashier Management
    public static Collection<Cashier> getCashiers(Store store) {
        return store.getCashiers();
    }

    public static void addCashier(Store store, Cashier cashier) {
        store.addCashier(cashier);
    }

    public static void updateCashier(Store store, String number, Cashier updatedCashier) {
        Cashier cashier = store.findCashier(number);
        if (cashier != null) {
            cashier.setPerson(updatedCashier.getPerson());
            cashier.setPassword(updatedCashier.getPassword());
        }
    }

    public static void deleteCashier(Store store, String number) {
        Cashier cashier = store.findCashier(number);
        if (cashier != null) {
            store.getCashiers().remove(cashier);
        }
    }

    // Register Management
    public static Collection<Register> getRegisters(Store store) {
        return store.getRegisters();
    }

    public static void addRegister(Store store, Register register) {
        store.addRegister(register);
    }

    public static void updateRegister(Store store, String number, Register updatedRegister) {
        Register register = store.findRegister(number);
        if (register != null) {
            register.setNumber(updatedRegister.getNumber());
            register.setCashDrawer(updatedRegister.getCashDrawer());
        }
    }

    public static void deleteRegister(Store store, String number) {
        Register register = store.findRegister(number);
        if (register != null) {
            store.getRegisters().remove(register);
        }
    }

    // Tax Category Management
    public static Collection<TaxCategory> getTaxCategories(Store store) {
        return store.getTaxCategories();
    }

    public static void addTaxCategory(Store store, TaxCategory taxCategory) {
        store.addTaxCategory(taxCategory);
    }

    public static void updateTaxCategory(Store store, String category, TaxCategory updatedTaxCategory) {
        TaxCategory taxCategory = store.findTaxCategory(category);
        if (taxCategory != null) {
            taxCategory.setCategory(updatedTaxCategory.getCategory());
            taxCategory.setTaxRates(updatedTaxCategory.getTaxRates());
        }
    }

    public static void deleteTaxCategory(Store store, String category) {
        TaxCategory taxCategory = store.findTaxCategory(category);
        if (taxCategory != null) {
            store.getTaxCategories().remove(taxCategory);
        }
    }

    // Tax Rate Management
    public static void addTaxRateToCategory(Store store, String category, TaxRate taxRate) {
        TaxCategory taxCategory = store.findTaxCategory(category);
        if (taxCategory != null) {
            taxCategory.addTaxRate(taxRate);
        }
    }

    public static void updateTaxRate(Store store, String category, LocalDate effectiveDate, TaxRate updatedTaxRate) {
        TaxCategory taxCategory = store.findTaxCategory(category);
        if (taxCategory != null) {
            taxCategory.updateTaxRate(effectiveDate, updatedTaxRate);
        }
    }

    public static void deleteTaxRate(Store store, String category, LocalDate effectiveDate) {
        TaxCategory taxCategory = store.findTaxCategory(category);
        if (taxCategory != null) {
            taxCategory.removeTaxRate(effectiveDate);
        }
    }

    // Item Management
    public static Collection<Item> getItems(Store store) {
        return store.getItems();
    }

    public static void addItem(Store store, Item item) {
        store.addItem(item);
    }

    public static void updateItem(Store store, String number, Item updatedItem) {
        Item item = store.findItem(number);
        if (item != null) {
            item.setDescription(updatedItem.getDescription());
            item.setTaxCategory(updatedItem.getTaxCategory());
            item.setPrices(updatedItem.getPrices());
            item.setUpcs(updatedItem.getUpcs());
        }
    }

    public static void deleteItem(Store store, String number) {
        Item item = store.findItem(number);
        if (item != null) {
            store.getItems().remove(item);
        }
    }

    // UPC Management
    public static void addUPCToItem(Store store, String itemNumber, UPC upc) {
        Item item = store.findItem(itemNumber);
        if (item != null) {
            item.addUpc(upc);
        }
    }

    public static void deleteUPCFromItem(Store store, String itemNumber, String upcCode) {
        Item item = store.findItem(itemNumber);
        if (item != null) {
            UPC upc = item.findUpc(upcCode);
            if (upc != null) {
                item.removeUpc(upc);
            }
        }
    }

    // Price Management
    public static void addPriceToItem(Store store, String itemNumber, Price price) {
        Item item = store.findItem(itemNumber);
        if (item != null) {
            item.addPrice(price);
        }
    }

    public static void deletePriceFromItem(Store store, String itemNumber, LocalDate effectiveDate) {
        Item item = store.findItem(itemNumber);
        if (item != null) {
            item.removePrice(effectiveDate);
        }
    }




}