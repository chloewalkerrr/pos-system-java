package PD;

import java.math.BigDecimal;

public class Cash extends Payment {

    public Cash(BigDecimal amount, BigDecimal amtTendered) {
        super(amount, amtTendered);
    }

    @Override
    public Boolean countsAsCash() {
        return true;
    }

    @Override
    public String toString() {
        return "Cash Payment [Amount=" + getAmount() + ", Tendered=" + getAmtTendered() + "]";
    }
}