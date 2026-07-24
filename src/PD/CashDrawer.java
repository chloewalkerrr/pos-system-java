package PD;

import java.math.BigDecimal;

public class CashDrawer {
    private BigDecimal cashAmount;

    public CashDrawer() {
        this.cashAmount = BigDecimal.ZERO;
    }

    public void removeCash(BigDecimal cash) {
        if (cash == null || cash.compareTo(BigDecimal.ZERO) < 0 || cash.compareTo(cashAmount) > 0) {
            throw new IllegalArgumentException("Invalid cash amount to remove.");
        }
        cashAmount = cashAmount.subtract(cash);
    }

    public void addCash(BigDecimal cash) {
        if (cash == null || cash.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid cash amount to add.");
        }
        cashAmount = cashAmount.add(cash);
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    @Override
    public String toString() {
        return "CashDrawer [Cash Amount=" + cashAmount + "]";
    }
}