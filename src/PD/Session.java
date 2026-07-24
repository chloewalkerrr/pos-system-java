package PD;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Session {

    private Cashier cashier;
    private Register register;
    private ArrayList<Sale> sales;
    private LocalDateTime startTime;

    public Session(Cashier cashier, Register register) {
        this.cashier = cashier;
        this.register = register;
        this.sales = new ArrayList<>();
        this.startTime = LocalDateTime.now();
    }

    public void addSale(Sale sale) {
        sales.add(sale);
    }

    public ArrayList<Sale> getSales() {
        return sales;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public Register getRegister() {
        return register;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public BigDecimal calcTotal() {
        BigDecimal sessionTotal = BigDecimal.ZERO;
        for (Sale sale : sales) {
            sessionTotal = sessionTotal.add(sale.calcTotal());
        }
        return sessionTotal.setScale(2, RoundingMode.HALF_UP);
    }

    
    public Sale getLastSale() {
        if (sales.isEmpty()) {
            return null;
        }
        return sales.get(sales.size() - 1);
    }
    
    public java.math.BigDecimal calcCashCountDiff(java.math.BigDecimal countedCash) {
        java.math.BigDecimal expectedCash = register.getCashDrawer().getCashAmount();
        for (Sale sale : sales) {
            for (Payment payment : sale.getPayments()) {
                if (payment.countsAsCash()) {
                    expectedCash = expectedCash.add(payment.getAmtTendered());
                }
            }
        }
        return countedCash.subtract(expectedCash);
    }
    
    public void displaySessionDetails() {
        System.out.printf("Session: Cashier: %s Register: %s Date: %s Total: %.2f%n",
                cashier.getPerson().getName(),
                register.getNumber(),
//                startTime.format(DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy")),
                startTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                calcTotal());

        for (Sale sale : sales) {
            sale.displaySaleDetails();
        }
    }


}
