package PD;

import java.time.LocalDate;
import java.util.Collection;
import java.util.TreeMap;

public class Item {

    private String number;
    private String description;
    private TreeMap<LocalDate, Price> prices;
    private TaxCategory taxCategory;
    private TreeMap<String, UPC> upcs; // Map to store UPCs

    public Item(String number, String description) {
        this.number = number;
        this.description = description;
        this.prices = new TreeMap<>();
        this.upcs = new TreeMap<>(); // Initialize UPC storage
    }

    public void addPrice(Price price) {
        this.prices.put(price.getEffectiveDate(), price);
    }

    public Price getPriceForDate(LocalDate date) {
        Price regularMatch = null;
        Price promoMatch = null;
        for (Price price : prices.values()) {
            if (price.isEffective(date)) {
                if (price instanceof PromoPrice) {
                    promoMatch = price;
                } else if (regularMatch == null || price.getEffectiveDate().isAfter(regularMatch.getEffectiveDate())) {
                    regularMatch = price;
                }
            }
        }
        return promoMatch != null ? promoMatch : regularMatch;
    }
    
    public Collection<Price> getPrices() {
        return prices.values(); // Returns all prices
    }

    public void setTaxCategory(TaxCategory taxCategory) {
        this.taxCategory = taxCategory;
    }

    public TaxCategory getTaxCategory() {
        return taxCategory;
    }

    public String getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public void addUpc(UPC upc) {
        this.upcs.put(upc.getUPC(), upc);
    }

    public Collection<UPC> getUpcs() {
        return upcs.values();
    }

	public LocalDate getEndDateForPrice(LocalDate startDate) {
    return prices.higherEntry(startDate) != null 
            ? prices.higherEntry(startDate).getValue().getEffectiveDate().minusDays(1) 
            : LocalDate.MAX; // If no later date exists, assume it lasts indefinitely
}

	public void setDescription(String description) {
		this.description = description;
		
	}

	public void setPrices(Collection<Price> newPrices) {
		this.prices.clear();
	    for (Price price : newPrices) {
	        this.prices.put(price.getEffectiveDate(), price);
	    }
		
	}

	public void setUpcs(Collection<UPC> upcs) {
		this.upcs.clear();
	    for (UPC upc : upcs) {
	        this.upcs.put(upc.getUPC(), upc);
	    }
		
	}

	public UPC findUpc(String upcCode) {
		return upcs.get(upcCode);
	}
	
	
	public void removeUpc(UPC upc) {
	    upcs.remove(upc.getUPC());
	}

	public void removePrice(LocalDate effectiveDate) {
	    prices.remove(effectiveDate);
	}
	//public LocalDate getEndDateForPrice(LocalDate startDate) {
	    //return prices.higherKey(startDate) != null ? prices.higherKey(startDate) : LocalDate.MAX;
	//}


}
