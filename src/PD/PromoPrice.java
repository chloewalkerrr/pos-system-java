package PD;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PromoPrice extends Price {

    private LocalDate endDate;

    public PromoPrice(BigDecimal price, LocalDate effectiveDate, LocalDate endDate) {
        super(price, effectiveDate);
        if (endDate == null || endDate.isBefore(effectiveDate)) {
            throw new IllegalArgumentException("End date must be on or after the effective date.");
        }
        this.endDate = endDate;
    }

    @Override
    public boolean isEffective(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        return !date.isBefore(getEffectiveDate()) && !date.isAfter(endDate);
    }


    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "PromoPrice [Price=" + getPrice() + ", Effective Date=" + getEffectiveDate() + ", End Date=" + endDate + "]";
    }
}
