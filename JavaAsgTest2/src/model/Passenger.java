package model;

//import java.util.InputMismatchException;
//import java.util.Scanner;

import app.UserModuleTest;
//import enums.TicketType;
import enums.UserRole;
//import exception.TicketNotFoundException;
import service.RouteService;
import service.TicketService;
import service.UserService;

/**
 * A Passenger is a User who can top up a wallet balance and buy tickets.
 * Inherits all profile behaviour from User and adds balance handling.
 */
public class Passenger extends User {
	
	private double balance;
	
//	private static final UserModuleTest userModuleTest = new UserModuleTest();
//	private static final RouteService routeService = new RouteService();
//	private static final TicketService ticketService = new TicketService(null);
//	private static final UserService userService = new UserService();
	
	
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
        System.out.println("===== Passenger Dashboard =====");
        System.out.println("Welcome " + name + " !");
        System.out.printf ("Wallet balance: RM%.2f%n", balance);
        System.out.println();
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    
    
//  //***************************Passenger Menu***************************
//      public void passengerMenu(Passenger passenger) {
//        boolean loggedIn = true;
//        System.out.println("Login successful!");
//        showDashboard();
//        while (loggedIn) {
//            printPassengerMenu();
//            int psgChoice = userModuleTest.readInt("Enter choice: ");
//            switch (psgChoice) {
//                case 1 -> viewProfile();
//                case 2 -> userModuleTest.topUp();
//                case 3 -> {
//                    // Need to get route and ticket type from user
//                    Route route = routeService.selectRoute();
//                    TicketType type = ticketService.selectTicketType();
//                    ticketService.buyTicket(passenger, route, type);
//                }
//                case 4 -> {
//                    String ticketId = userModuleTest.readString("Enter ticket ID to cancel: ");
//                    try {
//                        ticketService.cancelTicket(ticketId);
//                    } catch (TicketNotFoundException e) {
//                        System.out.println(e.getMessage());
//                    }
//                }
//                case 5 -> ticketService.viewTickets(passenger);
//                case 0 -> {
//                    System.out.println("Passenger logged out.");
//                    loggedIn = false;
//                }
//                default -> System.out.println("Invalid choice. Please enter number 0 - 5.");
//            }
//            System.out.println();
//        }
//    }
//          
//      private static void printPassengerMenu() {
//          System.out.println("========= PASSENGER MODULE MENU =========");
//          System.out.println("1. View Profile");
//          System.out.println("2. Top Up Balance");
//          System.out.println("3. Buy Ticket");
//          System.out.println("4. Cancel Ticket");
//          System.out.println("5. View Ticket");
//          System.out.println("0. Logout");
//          System.out.println("=========================================");
//      }        
     
      
}
