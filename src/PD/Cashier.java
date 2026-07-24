package PD;

import java.util.ArrayList;

public class Cashier {
    private String number;
    private Person person;
    private ArrayList<Session> sessions;
    private String password;

    public Cashier(String number, Person person, String password) {
        if (number == null || number.trim().isEmpty() || person == null || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid cashier details.");
        }
        this.number = number;
        this.person = person;
        this.sessions = new ArrayList<>();
        this.password = password;
    }

    public void addSession(Session session) {
        sessions.add(session);
    }

    public String getNumber() {
        return number;
    }

    public Person getPerson() {
        return person;
    }

    public Boolean isAuthorized(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "Cashier [Number=" + number + ", Person=" + person.getName() + "]";
    }
    
    // 2.3
    public void setPerson(Person person) {
        this.person = person;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}