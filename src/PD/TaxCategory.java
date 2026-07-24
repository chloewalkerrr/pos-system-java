package PD;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.TreeSet;

public class TaxCategory {
    private String category;
    private TreeSet<TaxRate> taxRates = new TreeSet<>((r1, r2) -> r1.getEffectiveDate().compareTo(r2.getEffectiveDate()));

    public TaxCategory(String category) {
        this.category = category;
    }

    public void addTaxRate(TaxRate rate) {
        taxRates.add(rate);
    }

    public BigDecimal getTaxRateForDate(LocalDate date) {
        return taxRates.stream()
                .filter(rate -> rate.isEffective(date))
                .map(TaxRate::getTaxRate)
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }

    public String getCategory() {
        return category;
    }
    
    public TreeSet<TaxRate> getTaxRate() {
    	return taxRates;
    }

	public void setCategory(String category) {
		this.category = category;
		
	}

	public void updateTaxRate(LocalDate effectiveDate, TaxRate updatedTaxRate) {
		taxRates.removeIf(rate -> rate.getEffectiveDate().equals(effectiveDate));
		taxRates.add(updatedTaxRate);
	}

	public void removeTaxRate(LocalDate effectiveDate) {
		taxRates.removeIf(rate -> rate.getEffectiveDate().equals(effectiveDate));
		
	}

	public Collection<TaxRate> getTaxRates() {
		return taxRates;
	}

	public void setTaxRates(Collection<TaxRate> taxRates2) {
		this.taxRates.clear();
		this.taxRates.addAll(taxRates);
		
	}

	public TaxRate findTaxRateByEffectiveDate(LocalDate effectiveDate) {
        for (TaxRate rate : taxRates) {
            if (rate.getEffectiveDate().equals(effectiveDate)) {
                return rate;
            }
        }
        return null; // Return null if no matching TaxRate is found
    }
}