package model;

import enums.UserRole;

/**
 * An Admin is a User who manages the system: stations, trains, routes,
 * users and reports. The management methods themselves live in the
 * teammates' service classes; this class provides the admin identity
 * and its own dashboard view (polymorphism).
 */
public class Admin extends User {

    public Admin() {
        super();
        this.role = UserRole.ADMIN;
    }

    public Admin(String userId, String name, String email, String password) {
        super(userId, name, email, password, UserRole.ADMIN);
    }

    @Override
    public void showDashboard() {
        System.out.println("======= Admin Dashboard =======");
        System.out.println("Welcome, " + name + "!");
        System.out.println("Options: manage stations, trains, routes, users and reports.");
    }
}
