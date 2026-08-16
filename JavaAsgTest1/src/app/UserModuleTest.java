package app;

import java.util.InputMismatchException;
import java.util.Scanner;

import exception.InvalidLoginException;
import model.Admin;
import model.Passenger;
import model.User;
import service.UserService;

public class UserModuleTest {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserService();

    public static void main(String[] args) {
        // Load any previously saved users at startup.
        userService.loadUsers();

        // Seed a default admin the first time the program runs.
        if (userService.findByEmail("admin@metro.com") == null) {
            Admin admin = new Admin(userService.generateUserId(),
                    "System Admin", "admin@metro.com", "admin123");
            userService.register(admin);
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> registerPassenger();
                case 2 -> login();
                case 3 -> userService.viewAllUsers();
                case 4 -> topUp();
                case 5 -> userService.saveUsers();
                case 6 -> userService.loadUsers();
                case 0 -> {
                    userService.saveUsers(); // save before exit
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("========= USER MODULE MENU =========");
        System.out.println("1. Register passenger");
        System.out.println("2. Login");
        System.out.println("3. View all users");
        System.out.println("4. Top up passenger balance");
        System.out.println("5. Save data to file");
        System.out.println("6. Load data from file");
        System.out.println("0. Exit");
        System.out.println("====================================");
    }

    private static void registerPassenger() {
        System.out.print("Name    : ");
        String name = scanner.nextLine().trim();
        System.out.print("Email   : ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        Passenger passenger = new Passenger(userService.generateUserId(), name, email, password);
        // register(email, password) validates the credentials first
        if (!passenger.register(email, password)) {
            System.out.println("Registration failed: email and password cannot be empty.");
            return;
        }
        userService.register(passenger);
    }

    private static void login() {
        System.out.print("Email   : ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try {
            User user = userService.login(email, password);
            System.out.println("Login successful!");
            user.showDashboard();   // polymorphism: passenger vs admin view
            user.viewProfile();
        } catch (InvalidLoginException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private static void topUp() {
        System.out.print("Passenger email: ");
        String email = scanner.nextLine().trim();
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
    }

    // ---------- input helpers with validation ----------

    private static int readInt(String prompt) {
        System.out.print(prompt);
        try {
            int value = scanner.nextInt();
            scanner.nextLine(); // consume newline
            return value;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // clear bad token
            return -1;          // treated as invalid choice
        }
    }

    private static double readDouble(String prompt) {
        System.out.print(prompt);
        try {
            double value = scanner.nextDouble();
            scanner.nextLine();
            return value;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }
}
