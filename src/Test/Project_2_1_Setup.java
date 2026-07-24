package Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import PD.*;

public class Project_2_1_Setup {

    public static void run() {
        // Create store
        Store store = Store.getInstance();
        store.setName("David's Quick Mart");
        store.setNumber("001");

        // Create and add Cashiers
        Cashier cashier1 = new Cashier("1", new Person("David", "1 Main St", "Edmond", "OK", "73034", "4053481111"), "password1");
        Cashier cashier2 = new Cashier("2", new Person("Sally", "2 Main St", "Edmond", "OK", "73034", "4053482222"), "password2");
        store.addCashier(cashier1);
        store.addCashier(cashier2);

        // Create and add Registers
        Register register1 = new Register("1");
        Register register2 = new Register("2");
        store.addRegister(register1);
        store.addRegister(register2);

        // Create and add Tax Categories
        TaxCategory foodTax = new TaxCategory("Food");
        foodTax.addTaxRate(new TaxRate(LocalDate.of(2023, 1, 1), BigDecimal.valueOf(0.07)));

        TaxCategory beverageTax = new TaxCategory("Beverage");
        beverageTax.addTaxRate(new TaxRate(LocalDate.of(2023, 1, 1), BigDecimal.ZERO));

        store.addTaxCategory(foodTax);
        store.addTaxCategory(beverageTax);

        // Create and add Items
        Item turkeySandwich = new Item("1001", "Turkey Sandwich");
        turkeySandwich.setTaxCategory(foodTax);
        turkeySandwich.addPrice(new Price(BigDecimal.valueOf(2.29), LocalDate.of(2023, 1, 1)));
        store.addItem(turkeySandwich);

        Item hamSandwich = new Item("1002", "Ham Sandwich");
        hamSandwich.setTaxCategory(foodTax);
        hamSandwich.addPrice(new Price(BigDecimal.valueOf(2.59), LocalDate.of(2023, 1, 1)));
        store.addItem(hamSandwich);

        Item coke = new Item("1003", "Coke");
        coke.setTaxCategory(beverageTax);
        coke.addPrice(new Price(BigDecimal.valueOf(0.97), LocalDate.of(2023, 1, 1)));
        store.addItem(coke);

        Item drPepper = new Item("1004", "Dr. Pepper");
        drPepper.setTaxCategory(beverageTax);
        drPepper.addPrice(new Price(BigDecimal.valueOf(0.79), LocalDate.of(2023, 1, 1)));
        store.addItem(drPepper);

        // Create a Session
        Session session1 = new Session(cashier1, register2);
        store.addSession(session1);

        // Create a Sale
        Sale sale1 = new Sale(false);
        session1.addSale(sale1);

        // Add Sale Line Items
        sale1.addSaleLineItem(new SaleLineItem(sale1, turkeySandwich, "1"));
        sale1.addSaleLineItem(new SaleLineItem(sale1, hamSandwich, "2"));

        // Display Store Data
        displayStoreData(store);
    }

    private static void displayStoreData(Store store) {
        System.out.println("==============");
        System.out.println("Cashiers");
        System.out.println("==============");
        for (Cashier cashier : store.getCashiers()) {
            System.out.println(cashier.getPerson().getName());
        }

        System.out.println("==============");
        System.out.println("Registers");
        System.out.println("==============");
        for (Register register : store.getRegisters()) {
            System.out.println(register.getNumber());
        }

        System.out.println("==============");
        System.out.println("Items");
        System.out.println("==============");
        for (Item item : store.getItems()) {
            Price price = item.getPrices().iterator().next();
            BigDecimal taxRate = item.getTaxCategory().getTaxRateForDate(LocalDate.now());
            System.out.printf("%s %s %.2f %.2f%n", item.getNumber(), item.getDescription(),
                    price.getPrice(), taxRate);
        }

        System.out.println("==============");
        System.out.println("Sessions");
        System.out.println("==============");
        for (Session session : store.getSessions()) {
            System.out.printf("Session: Cashier: %s Register: %s Total: %.2f%n",
                    session.getCashier().getPerson().getName(),
                    session.getRegister().getNumber(),
                    session.calcTotal());

            for (Sale sale : session.getSales()) {
                System.out.printf("  Sale: Subtotal = %.2f Tax = %.2f Total = %.2f%n",
                        sale.calcSubTotal(),
                        sale.calcTax(),
                        sale.calcTotal());

                for (SaleLineItem sli : sale.getSaleLineItemsCollection()) {
                    Price price = sli.getItem().getPriceForDate(sale.getDateTime().toLocalDate());
                    BigDecimal tax = sli.calcTax();
                    System.out.printf("     %s %s %d %.2f %.2f%n",
                            sli.getItem().getNumber(),
                            sli.getItem().getDescription(),
                            sli.getQuantity(),
                            price.getPrice(),
                            tax);
                }
            }
        }
    }
}
