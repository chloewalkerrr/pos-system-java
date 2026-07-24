package PD;

public class AuthorizedPayment extends Payment {
    private String authorizationCode;

    public AuthorizedPayment() {
        super();
        this.authorizationCode = "DEFAULT-AUTH-CODE";
    }

    public AuthorizedPayment(String amount, String authorizationCode) {
        super(amount);
        this.authorizationCode = authorizationCode == null || authorizationCode.trim().isEmpty()
                ? "DEFAULT-AUTH-CODE" : authorizationCode;
    }

    @Override
    public Boolean isAuthorized() {
        return authorizationCode != null && !authorizationCode.trim().isEmpty();
    }

    @Override
    public Boolean countsAsCash() {
        return false;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    @Override
    public String toString() {
        return "AuthorizedPayment [Amount=" + getAmount() + ", Authorization Code=" + authorizationCode + "]";
    }
}
