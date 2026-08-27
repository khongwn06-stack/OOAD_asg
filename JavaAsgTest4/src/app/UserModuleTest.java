package app;

import java.util.InputMismatchException;
import java.util.Scanner;

//import app.SmartMetroTicketingSystem;
import enums.UserRole;
import exception.InvalidLoginException;
import model.Admin;
import model.Passenger;
import model.User;
import service.UserService;

public class UserModuleTest {

	private final SmartMetroTicketingSystem smartMetroTicketingSystem;
	private final UserService userService;
	
	public UserModuleTest(
            SmartMetroTicketingSystem smartMetroTicketingSystem,
            UserService userService) {

        this.smartMetroTicketingSystem = smartMetroTicketingSystem;
        this.userService = userService;
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
    	String email = readString("Passenger email: ");
        User user = userService.findByEmail(email);
        
        if (user == null) {
            System.out.println("No user found with that email.");
            return;
        }
        if (!(user instanceof Passenger)) {
            System.out.println("Only passengers have a wallet balance.");
            return;
        }
        
        double amount = readDouble("Amount to top up: RM");
        ((Passenger) user).topUp(amount);
        userService.saveUsers();
    }

    
    // ---------- input helpers with validation ----------
    private static final Scanner scanner = new Scanner(System.in);
    public int readInt(String prompt) {
        System.out.print(prompt);
        try {
            int value = scanner.nextInt();
            scanner.nextLine(); // consume newline
            return value;
        } 
        catch (InputMismatchException e) {
            scanner.nextLine(); // clear bad token
            return -1;          // treated as invalid choice
        }
    }
    
    public double readDouble(String prompt) {
        System.out.print(prompt);
        try {
            double value = scanner.nextDouble();
            scanner.nextLine();
            return value;
        } 
        catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }
    
    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

}
