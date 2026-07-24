package Test;

import PD.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import DM.DataManager;

public class Project_2_2_Setup {

    public static void run() {
        try {
            System.out.println("Ready to open Store");
            Store store = Store.getInstance();
            store.setName("David's Quick Mart");
            System.out.println(store.getName());

            String filePath = "src/data/StoreData_v2024FALL.csv";
            DataManager.loadStoreData(store, filePath);

            displayStoreData(store);
            System.out.println("Store Open: " + store.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            Price price = item.getPriceForDate(LocalDate.now());
            BigDecimal taxRate = item.getTaxCategory().getTaxRateForDate(LocalDate.now());
            String upcCode = item.getUpcs().isEmpty() ? "" : item.getUpcs().iterator().next().getUPC();
            System.out.println(item.getNumber() + " " + item.getDescription() +
                    " Price: " + price.getPrice() + " Tax Rate: " + taxRate + " " + upcCode);
        }

        System.out.println("==============");
        System.out.println("Sessions");
        System.out.println("==============");
        for (Session session : store.getSessions()) {
            session.displaySessionDetails();
        }
    }
}