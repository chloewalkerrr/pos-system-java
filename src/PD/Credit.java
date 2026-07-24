package PD;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Credit extends Payment {

    private String cardType;
    private String accountNumber;
    private LocalDate expiryDate;

    public Credit(BigDecimal amount, String cardType, String accountNumber, LocalDate expiryDate) {
        super(amount);
        this.cardType = cardType;
        this.accountNumber = accountNumber;
        this.expiryDate = expiryDate;
    }

    public String getCardType() {
        return cardType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    
    @Override
    public Boolean countsAsCash() {
        return false;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    @Override
    public String toString() {
        return "Credit Payment [Amount=" + getAmount() + ", CardType=" + cardType +
                ", AccountNumber=" + accountNumber + ", ExpiryDate=" + expiryDate + "]";
    }
}
