package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import exception.FileProcessingException;
import exception.InvalidLoginException;
import model.User;
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

    /** Key = email (unique), Value = the User object. */
    private HashMap<String, User> users;

    /** Programmed against the interface so the storage type can change. */
    private FileManager fileManager;

    private static final String USER_FILE = "users.txt";

    /** Used to generate sequential user IDs like U0001, U0002 ... */
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
        if (user == null || user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            System.out.println("Registration failed: invalid user details.");
            return false;
        }
        if (users.containsKey(user.getEmail())) {
            System.out.println("Registration failed: email '" + user.getEmail() + "' already exists.");
            return false;
        }
        users.put(user.getEmail(), user);
        System.out.println("Registration successful. Welcome, " + user.getName() + " (" + user.getUserId() + ").");
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

        System.out.println("=================== Registered Users ===================");
        System.out.printf("%-8s %-20s %-25s %-10s%n", "ID", "Name", "Email", "Role");
        System.out.println("--------------------------------------------------------");
        for (User u : list) {
            System.out.printf("%-8s %-20s %-25s %-10s%n",
                    u.getUserId(), u.getName(), u.getEmail(), u.getRole());
        }
        System.out.println("========================================================");
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
        } catch (FileProcessingException e) {
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
        } catch (FileProcessingException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    public HashMap<String, User> getUsers() {
        return users;
    }
}