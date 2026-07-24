package PD;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Price {
    private BigDecimal price;
    private LocalDate effectiveDate;

    public Price(BigDecimal price, LocalDate effectiveDate) {
        this.price = price;
        this.effectiveDate = effectiveDate;
    }

    public boolean isEffective(LocalDate date) {
        return !date.isBefore(effectiveDate);
    }

    public BigDecimal getPrice() {
        return price;
    }
    
 
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

	public Object getAmount() {
		return getPrice();
	}
	
	@Override
	public String toString() {
	    return "Price [Price=" + price + ", Effective Date=" + effectiveDate + "]";
	}

    
}
