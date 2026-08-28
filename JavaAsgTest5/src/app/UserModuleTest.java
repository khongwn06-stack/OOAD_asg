package app;

import java.util.Scanner;

import enums.UserRole;
import exception.InvalidLoginException;
import model.Admin;
import model.Passenger;
import model.User;
import payment.Payment;
import payment.CashPayment;
import payment.CardPayment;
import service.PaymentService;
import service.UserService;

public class UserModuleTest {

	private final SmartMetroTicketingSystem smartMetroTicketingSystem;
	private final PaymentService paymentService;
	private final UserService userService;
	
	public UserModuleTest(
	        SmartMetroTicketingSystem smartMetroTicketingSystem,
	        UserService userService) {

	    this.smartMetroTicketingSystem = smartMetroTicketingSystem;
	    this.userService = userService;
	    this.paymentService = new PaymentService();
	}

    public void registerPassenger() {
    	String name = readString("Name    : ");
        String email = readString("Email   : ");
        String password = readString("Password: ");
	
	    Passenger passenger = new Passenger(
	    		userService.generateUserId(), 
	        	name, email, password);

	    userService.register(passenger);
    }

    public void login() {
    	String email = readString("Email   : ");
        String password = readString("Password: ");

        try {
            User user = userService.login(email, password);

            if(user.getRole() == UserRole.ADMIN) {
                smartMetroTicketingSystem.adminMenu((Admin) user);
            }
            
            if(user.getRole() == UserRole.PASSENGER) {
            	smartMetroTicketingSystem.passengerMenu((Passenger) user);
            }
        } 
        catch (InvalidLoginException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    public void topUp(Passenger passenger) {       
    	if (passenger == null){
            System.out.println("Passenger not found.");
            return;
        }

        double amount = readDouble("Amount to top up: RM ");

        if (amount == 0){
            System.out.println("Top up failed. Amount cannot be empty.");
            return;
        }

    	if (amount <= 0){
    		System.out.println("Top up failed. Amount must be greater than 0.");
        	return;
    	}

        System.out.println();
        System.out.println("---- TOP UP PAYMENT ----");
        System.out.println("1. Cash");
        System.out.println("2. Card");
        System.out.println("0. Cancel");
        System.out.println("------------------------");

        int choice = readInt("Select payment method: ");

        Payment payment;

        switch (choice){
            case 1: payment = new CashPayment(); break;

            case 2:
                String cardNumber = readString("Enter card number: ");
                payment = new CardPayment(cardNumber);
                break;

            case 0:
                System.out.println("Top up cancelled.");
                return;

            default:
                System.out.println("Invalid payment method.");
                return;
        }

        boolean success = paymentService.processPayment(payment, amount);

        if (success){
            passenger.topUp(amount);
            userService.saveUsers();

            System.out.printf("Top up successful! Current balance: RM %.2f%n",
                passenger.getBalance());
        }
        else{
            System.out.println("Top up failed. Balance was not changed.");
        }
    }

    
    // ---------- input helpers with validation ----------
    private static final Scanner scanner = new Scanner(System.in);
    public int readInt(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        
        if (input.isEmpty()) {
            return 0;
        }
        
        try {
        	return Integer.parseInt(input);
        } 
        catch (NumberFormatException e) {
            return -2;
        }
    }
    
    public double readDouble(String prompt){
    	System.out.print(prompt);
        String input = scanner.nextLine().trim();

        if (input.isEmpty()){
            return 0;
        }

        try{
        	return Double.parseDouble(input);
        }
        catch (NumberFormatException e){
        	return -2;
    	}
    }
    
    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

}
