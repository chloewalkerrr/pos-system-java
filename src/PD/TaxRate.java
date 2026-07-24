package PD;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TaxRate {
    private BigDecimal taxRate;
    private LocalDate effectiveDate;

    public TaxRate(LocalDate effectiveDate, BigDecimal taxRate) {
        this.effectiveDate = effectiveDate;
        this.taxRate = taxRate;
    }

    public boolean isEffective(LocalDate date) {
        return !date.isBefore(effectiveDate);
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }
}