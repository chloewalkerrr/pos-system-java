package PD;

import java.math.BigDecimal;

public class Check extends AuthorizedPayment {

    private String routingNumber;
    private String accountNumber;
    private String checkNumber;

    public Check(BigDecimal amount, String routingNumber, String accountNumber, String checkNumber) {
        super(amount.toString(), ""); // Pass amount and blank authorization code
        if (routingNumber == null || routingNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Routing number cannot be null or empty.");
        }
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty.");
        }
        if (checkNumber == null || checkNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Check number cannot be null or empty.");
        }
        this.routingNumber = routingNumber;
        this.accountNumber = accountNumber;
        this.checkNumber = checkNumber;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    @Override
    public String toString() {
        return "Check [Amount=" + getAmount() + ", Routing=" + routingNumber + ", Account=" + accountNumber + ", Check Number=" + checkNumber + "]";
    }
}
