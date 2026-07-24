package PD;

import java.math.BigDecimal;

public class Payment {

    private BigDecimal amount;
    private BigDecimal amtTendered;

    public Payment() {
        this.amount = BigDecimal.ZERO;
        this.amtTendered = BigDecimal.ZERO;
    }

    public Payment(String amount) {
        this.amount = new BigDecimal(amount);
        this.amtTendered = this.amount;
    }

    public Payment(BigDecimal amount) {
        this.amount = amount;
        this.amtTendered = amount;
    }

    public Payment(BigDecimal amount, BigDecimal amtTendered) {
        this(amount);
        this.amtTendered = amtTendered;
    }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getAmtTendered() { return amtTendered; }
    public void setAmtTendered(BigDecimal amtTendered) { this.amtTendered = amtTendered; }

    public Boolean countsAsCash() {
        throw new UnsupportedOperationException("countsAsCash is not implemented in base Payment class.");
    }

    public Boolean isAuthorized() {
        throw new UnsupportedOperationException("isAuthorized is not implemented in base Payment class.");
    }

    @Override
    public String toString() {
        return "Payment [Amount=" + amount + ", Tendered=" + amtTendered + "]";
    }
}