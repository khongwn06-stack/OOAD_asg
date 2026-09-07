package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
//import java.util.Scanner;

import exception.FileProcessingException;
import exception.InvalidLoginException;
import model.User;
import model.Passenger;
import repository.FileManager;
import repository.TXTFileManager;

/**
 * Central service for all user-account operations.
 *
 * Demonstrates:
 *  - Collections : a HashMap keyed by email for O(1) lookup.
 *  - Polymorphism: talks to the FileManager interface, not a concrete class.
 *  - Exception handling: login throws InvalidLoginException; file errors caught.
 *  - Comparable  : viewAllUsers sorts users by name.
 */
public class UserService {
    
    // Key = email (unique), Value = the User object.
    //private HashMap<String, User> users;
    private HashMap<String, User> users = new HashMap<>();

    // Programmed against the interface so the storage type can change.
    private FileManager fileManager;

    private static final String USER_FILE = "users.txt";

    // Used to generate sequential user IDs like U0001, U0002 ...
    private int idCounter = 1;

    public UserService() {
        this.users = new HashMap<>();
        this.fileManager = new TXTFileManager();
    }

    /**
     * Registers a new user. Rejects duplicate email.
     * @return true if registration succeeded.
     */
    public boolean register(User user) {
        if (user == null) {
            System.out.println("Registration Failed: invalid user details.");
            return false;
        }
        
        // Register validation
        boolean valid = true;
        if (!nameValidation(user)) {
        	valid = false;
        }
        if (!emailValidation(user)) {
        	valid = false;
        }
        if (!passwordValidation(user)) {
        	valid = false;
        }      
        // Stop registration if any validation failed
        if (!valid) {
            System.out.println("Registration Failed. Please correct the above errors.");
            return false;
        }
        
        String email = user.getEmail().trim();
        
        // Check if email already exists
        if (users.containsKey(email)) {
            System.out.println("Registration Failed: email '" + email + "' already exists.");
            return false;
        }
        
        // All validation passed
        users.put(email, user);
        System.out.println();
        System.out.println("Registration successful! Welcome, " + user.getName() + " (" + user.getUserId() + ").");
		saveUsers();
        return true;
    }
    
    //Name validation
    public boolean nameValidation(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return false;
        }
        if (user.getName().matches(".*\\d.*")) {
            System.out.println("Name cannot contain numbers.");
            return false;
        }
        return true;
    }
    
    //Email validation
    public boolean emailValidation(User user) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            System.out.println("Email cannot be empty.");
            return false;
        }
        
        //check email format
        if(!user.getEmail().contains("@") || !user.getEmail().contains(".")) {
 			System.out.println("Email format is not accepted. Please include '@' and '.' symbols in the email.");
 			return false;
        }
        return true;
    }
    
    //Password validation
    public boolean passwordValidation(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            System.out.println("Password cannot be empty.");
            return false;
        }
        if (user.getPassword().length() < 5) {
            System.out.println("Password must be at least 5 characters.");
            return false;
        }
        return true;
    }
    
    
    

    /**
     * Authenticates a user by email and password.
     * @return the matching User on success.
     * @throws InvalidLoginException if the email is unknown or password wrong.
     */
    public User login(String email, String password) throws InvalidLoginException {
        User user = users.get(email);
        if (user == null) {
            throw new InvalidLoginException("No account found for email: " + email);
        }
        if (!user.getPassword().equals(password)) {
            throw new InvalidLoginException("Incorrect password for email: " + email);
        }
        return user;
    }


    /** Prints every registered user, sorted alphabetically by name. */
    public void viewAllUsers() {
        if (users.isEmpty()) {
            System.out.println("No users registered yet.");
            return;
        }
        List<User> list = new ArrayList<>(users.values());
        Collections.sort(list); // uses User.compareTo (Comparable)

        System.out.println("========================= Registered Users ===========================");
        System.out.printf("%-8s %-20s %-28s %-10s%n", "ID", "Name", "Email", "Role");
        System.out.println("----------------------------------------------------------------------");
        for (User u : list) {
            System.out.printf("%-8s %-20s %-28s %-10s%n",
                    u.getUserId(), u.getName(), u.getEmail(), u.getRole());
        }
        System.out.println("======================================================================");
    }

    /** Generates the next unique user ID, e.g. "U0003". */
    public String generateUserId() {
        return String.format("U%04d", idCounter++);
    }

    /** Looks up a single user by email (or null if none). */
    public User findByEmail(String email) {
        return users.get(email);
    }

    /** Saves all users to users.txt, handling any file error gracefully. */
    public void saveUsers() {
        try {
            fileManager.saveData(users.values(), USER_FILE);
            System.out.println("User data saved to " + USER_FILE + ".");
        } 
        catch (FileProcessingException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    /** Loads users from users.txt into the HashMap on program start. */
    @SuppressWarnings("unchecked")
    public void loadUsers() {        
        try {
            Object data = fileManager.loadData(USER_FILE);
            List<User> loaded = (List<User>) data;
            users.clear();

            int maxId = 0;
            for (User u : loaded) {
                users.put(u.getEmail(), u);
                // keep the ID counter ahead of any loaded IDs
                String digits = u.getUserId().replaceAll("\\D", "");
                if (!digits.isEmpty()) {
                    maxId = Math.max(maxId, Integer.parseInt(digits));
                }
            }
            idCounter = maxId + 1;
            System.out.println("Loaded " + loaded.size() + " user(s) from " + USER_FILE + ".");
        } 
        catch (FileProcessingException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    public HashMap<String, User> getUsers() {
        return users;
    }
    
    
    public ArrayList<Passenger> getPassengers()
    {
        ArrayList<Passenger> passengers = new ArrayList<>();

        for (User user : users.values())
        {
            if (user instanceof Passenger)
            {
                passengers.add((Passenger) user);
            }
        }

        return passengers;
    }
}