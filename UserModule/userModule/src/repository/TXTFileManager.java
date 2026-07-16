package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import enums.UserRole;
import exception.FileProcessingException;
import model.Admin;
import model.Passenger;
import model.User;

/**
 * TXT implementation of FileManager for the user module.
 *
 * File format (one user per line), comma separated:
 *   userId,name,email,password,role,balance
 *
 * Example:
 *   U0001,Alice Tan,alice@mail.com,pass123,PASSENGER,50.0
 *   U0002,System Admin,admin@metro.com,admin123,ADMIN,0.0
 */
public class TXTFileManager implements FileManager {

    private static final String DELIMITER = ",";

    /**
     * Expects {@code data} to be a Collection of User objects and writes
     * each one as a line in the target file.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void saveData(Object data, String fileName) throws FileProcessingException {
        Collection<User> users;
        try {
            users = (Collection<User>) data;
        } catch (ClassCastException e) {
            throw new FileProcessingException("Expected a collection of users for file: " + fileName);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (User user : users) {
                writer.write(toLine(user));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new FileProcessingException("Could not write to file: " + fileName, e);
        }
    }

    /**
     * Reads the file and rebuilds a list of User objects. Returns an empty
     * list if the file does not exist yet (first run of the program).
     */
    @Override
    public Object loadData(String fileName) throws FileProcessingException {
        List<User> users = new ArrayList<>();
        File file = new File(fileName);

        if (!file.exists()) {
            return users; // nothing saved yet
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNo = 0;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                User user = fromLine(line, lineNo);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            throw new FileProcessingException("Could not read file: " + fileName, e);
        }
        return users;
    }

    /** Converts a User object into a single text line. */
    private String toLine(User user) {
        double balance = 0.0;
        if (user instanceof Passenger) {
            balance = ((Passenger) user).getBalance();
        }
        return String.join(DELIMITER,
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole().name(),
                String.valueOf(balance));
    }

    /** Rebuilds a User (Passenger or Admin) from one text line. */
    private User fromLine(String line, int lineNo) throws FileProcessingException {
        String[] parts = line.split(DELIMITER);
        if (parts.length < 6) {
            // skip malformed line but keep the program running
            System.out.println("Warning: skipping malformed line " + lineNo + " in users file.");
            return null;
        }
        try {
            String userId = parts[0].trim();
            String name = parts[1].trim();
            String email = parts[2].trim();
            String password = parts[3].trim();
            UserRole role = UserRole.valueOf(parts[4].trim().toUpperCase());
            double balance = Double.parseDouble(parts[5].trim());

            if (role == UserRole.ADMIN) {
                return new Admin(userId, name, email, password);
            } else {
                return new Passenger(userId, name, email, password, balance);
            }
        } catch (IllegalArgumentException e) {
            throw new FileProcessingException("Corrupted user record at line " + lineNo + ": " + line);
        }
    }
}
