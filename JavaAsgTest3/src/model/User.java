package model;

import enums.UserRole;

/**
 * Abstract base class for every user of the system.
 *
 * Demonstrates:
 *  - Abstraction  : cannot be instantiated directly; abstract showDashboard().
 *  - Encapsulation: fields are protected with public getters/setters.
 *  - Inheritance  : Passenger and Admin extend this class.
 *  - Comparable   : lets a list of users be sorted by name.
 */
public abstract class User implements Comparable<User> {

    protected String userId;
    protected String name;
    protected String email;
    protected String password;
    protected UserRole role;

    public User() {
    }

    public User(String userId, String name, String email, String password, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Simple registration validation. Returns true when the credentials
     * are usable, false otherwise. UserService decides what to do next.
     */
    public boolean register(String email, String password) {
        if (email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return false;
        }
        this.email = email;
        this.password = password;
        return true;
    }

    /** Prints the user's profile information to the console. */
    public void viewProfile() {
    	System.out.println();
        System.out.println("--------- Profile ---------");
        System.out.println("User ID : " + userId);
        System.out.println("Name    : " + name);
        System.out.println("Email   : " + email);
        System.out.println("Role    : " + role);
        System.out.println("---------------------------");
    }

    /** Updates editable profile details. */
    public void editProfile(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * Abstract method overridden by each subclass.
     * This is the polymorphism hook: the same call shows a different
     * dashboard depending on the actual object type (Passenger / Admin).
     */
    public abstract void showDashboard();

    /** Sort users alphabetically by name (used by viewAllUsers). */
    @Override
    public int compareTo(User other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    
    // ---------- Getters and Setters (encapsulation) ----------
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
