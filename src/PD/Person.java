package PD;

public class Person {

    private String name;
    private String ssn; // Optional for 2.1
    private String address;
    private String city;
    private String state;
    private String zip;
    private String phone;

    // Constructor with SSN (used in 2.2)
    public Person(String name, String ssn, String address, String city, String state, String zip, String phone) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
        this.ssn = ssn;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
    }

    // Overloaded constructor without SSN (used in 2.1)
    public Person(String name, String address, String city, String state, String zip, String phone) {
        this(name, null, address, city, state, zip, phone); // Pass null for SSN
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getSsn() {
        return ssn;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getPhone() {
        return phone;
    }
    
 // Setters (new for 2.3 compatibility)
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

	public String getSSN() {
		return ssn;
	}
    
}
