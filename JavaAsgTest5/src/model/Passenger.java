package model;

import enums.UserRole;

/**
 * A Passenger is a User who can top up a wallet balance and buy tickets.
 * Inherits all profile behaviour from User and adds balance handling.
 */
public class Passenger extends User {
	
	private double balance;

    public Passenger() {
        super();
        this.role = UserRole.PASSENGER;
    }

    public Passenger(String userId, String name, String email, String password) {
        super(userId, name, email, password, UserRole.PASSENGER);
        this.balance = 0.0;
    }

    public Passenger(String userId, String name, String email, String password, double balance) {
        super(userId, name, email, password, UserRole.PASSENGER);
        this.balance = balance;
    }

    /** Adds money to the wallet. Rejects non-positive amounts. */
    public void topUp(double amount) {
        if (amount <= 0) {
            System.out.println("Top-up amount must be greater than zero.");
            return;
        }
        balance += amount;
        System.out.printf("Top-up successful. New balance: RM%.2f%n", balance);
    }

    /**
     * Deducts a fare from the wallet. Returns false if there are
     * insufficient funds so the Ticket module can react accordingly.
     * (Integration point for the teammate handling the ticket module.)
     */
    public boolean deduct(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    @Override
    public void showDashboard() {
    	System.out.println();
    	System.out.println("Login successful!");
        System.out.println("Welcome " + name + "!");
        System.out.printf ("Wallet balance: RM%.2f%n", balance);
        System.out.println();
    }
    
    @Override
    public void viewProfile() {
        super.viewProfile();
        System.out.printf("Wallet balance: RM%.2f%n", balance);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public void addBalance(double amount)
    {
        if (amount > 0)
        {
            balance += amount;
        }
    }
    
}
