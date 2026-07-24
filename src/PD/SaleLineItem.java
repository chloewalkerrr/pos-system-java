package PD;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SaleLineItem {

    private Item item;    // The item being sold
    private int quantity; // The quantity of the item sold
    private Sale sale;    // The sale this line item belongs to

    // Constructor
    public SaleLineItem(Sale sale, Item item, String quantity) {
        this.sale = sale;
        this.item = item;
        this.quantity = Integer.parseInt(quantity);
    }

    // Calculate the subtotal for this line item
    public BigDecimal calcSubTotal() {
        Price price = item.getPriceForDate(sale.getDateTime().toLocalDate());
        return price.getPrice().multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP)
;
    }

    // Calculate the tax for this line item
    public BigDecimal calcTax() {
        return calcSubTotal()
                .multiply(item.getTaxCategory().getTaxRateForDate(sale.getDateTime().toLocalDate()))
                .setScale(2, RoundingMode.HALF_UP);

    }

    // Custom string representation for display
    @Override
    public String toString() {
        Price price = item.getPriceForDate(sale.getDateTime().toLocalDate());
        LocalDate startDate = price.getEffectiveDate();
        LocalDate endDate = item.getEndDateForPrice(startDate); // Ensure method exists in the Item class

        return String.format("%s %s %d@$%.2f %s-%s $%.2f",
                item.getNumber(),
                item.getDescription(),
                quantity,
                price.getPrice(),
                startDate.format(DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy")),
                endDate.format(DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy")),
                calcSubTotal());
    }

    // Getters
    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public String displayLineItemDetails() {
        Price price = item.getPriceForDate(sale.getDateTime().toLocalDate());
        LocalDate startDate = price.getEffectiveDate();
        LocalDate endDate = item.getEndDateForPrice(startDate); // Ensure `getEndDateForPrice` exists in `Item`

        return String.format("%s %s %d@$%.2f %s-%s $%.2f",
                item.getNumber(),
                item.getDescription(),
                quantity,
                price.getPrice(),
//                startDate.format(DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy")),
                startDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
//                endDate.format(DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy")),
                endDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                calcSubTotal());
    }

}
