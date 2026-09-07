package model;

import enums.UserRole;

public class Admin extends User{

    public Admin() {
        super();
        this.role = UserRole.ADMIN;
    }

    public Admin(String userId, String name, String email, String password) {
        super(userId, name, email, password, UserRole.ADMIN);
    }
 
    @Override
    public void showDashboard() {
    	System.out.println();
    	System.out.println("Login successful!");
        System.out.println("Welcome, " + name + "!");
        System.out.println();
    }
}
