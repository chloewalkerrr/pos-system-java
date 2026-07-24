package PD;

public class UPC {
    private String upc;

    public UPC(String upc) {
        if (upc == null || upc.trim().isEmpty()) {
            throw new IllegalArgumentException("UPC cannot be null or empty.");
        }
        this.upc = upc;
    }

    public String getUPC() {
        return upc;
    }
    
    public void setUPC(String upc) {
    	this.upc = upc;
    }
}