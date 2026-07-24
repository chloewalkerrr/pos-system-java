package PD;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

public class Sale {

    private Collection<Payment> payments;
    private Collection<SaleLineItem> saleLineItems;
    private LocalDateTime dateTime;
    private Boolean taxFree; // Indicates if the sale is tax-free

    // Default Constructor
    public Sale() {
        this(false); // Default to not tax-free
    }

    // Constructor to initialize tax-free property
    public Sale(Boolean taxFree) {
        this.payments = new ArrayList<>();
        this.saleLineItems = new ArrayList<>();
        this.dateTime = LocalDateTime.now();
        this.taxFree = taxFree;
    }

    // Adds a payment to the sale
    public void addPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null.");
        }
        payments.add(payment);
    }

    // Adds a SaleLineItem to the sale
    public void addSaleLineItem(SaleLineItem sli) {
        if (sli == null) {
            throw new IllegalArgumentException("SaleLineItem cannot be null.");
        }
        saleLineItems.add(sli);
    }

    // Calculates the subtotal of the sale
    public BigDecimal calcSubTotal() {
        BigDecimal subTotal = BigDecimal.ZERO;
        for (SaleLineItem sli : saleLineItems) {
            subTotal = subTotal.add(sli.calcSubTotal());
        }
        return subTotal.setScale(2, RoundingMode.HALF_UP);
    }

    // Calculates the total tax for the sale
    public BigDecimal calcTax() {
        if (taxFree) {
            return BigDecimal.ZERO; // No tax if the sale is tax-free
        }
        BigDecimal totalTax = BigDecimal.ZERO;
        for (SaleLineItem sli : saleLineItems) {
            totalTax = totalTax.add(sli.calcTax());
        }
        return totalTax.setScale(2, RoundingMode.HALF_UP);
    }

    // Calculates the total amount for the sale
    public BigDecimal calcTotal() {
        return calcSubTotal().add(calcTax().setScale(2, RoundingMode.HALF_UP));
    }

    // Gets the total payments made for the sale
    public BigDecimal getTotalPayments() {
        BigDecimal totalPayments = BigDecimal.ZERO;
        for (Payment payment : payments) {
            totalPayments = totalPayments.add(payment.getAmount());
        }
        return totalPayments.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalTendered() {
        BigDecimal totalTendered = BigDecimal.ZERO;
        for (Payment payment : payments) {
            totalTendered = totalTendered.add(payment.getAmtTendered());
        }
        return totalTendered.setScale(2, RoundingMode.HALF_UP);
    }

    // Checks if the total payments are sufficient to cover the sale
    public Boolean isPaymentEnough() {
        return getTotalPayments().compareTo(calcTotal()) >= 0;
    }

    public BigDecimal calcChange() {
        BigDecimal change = getTotalTendered().subtract(calcTotal());
        return change.compareTo(BigDecimal.ZERO) >= 0 ? change : BigDecimal.ZERO;
    }

    // Get the date and time of the sale
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    
    public java.util.Collection<Payment> getPayments() {
        return payments;
    }
    
    public Boolean getTaxFree() {
        return taxFree;
    }

    public void setTaxFree(Boolean taxFree) {
        this.taxFree = taxFree;
    }

    // Override toString for better debugging
    @Override
    public String toString() {
    	//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");
        return "Sale [DateTime=" + dateTime + ", SubTotal=" + calcSubTotal() + ", Tax=" + calcTax() 
                + ", Total=" + calcTotal() + ", Total Payments=" + getTotalPayments() + "]";
    }

    // Getter for sale line items
    public Collection<SaleLineItem> getSaleLineItemsCollection() {
        return saleLineItems;
    }
    
    public void displaySaleDetails() {
        System.out.printf("  Sale: Subtotal = %.2f Tax = %.2f Total = %.2f Payment = %.2f Change = %.2f%n",
                calcSubTotal(), calcTax(), calcTotal(), getTotalPayments(), calcChange());

        for (SaleLineItem sli : saleLineItems) {
            sli.displayLineItemDetails();
        }
    }

}
