package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

//import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import enums.UserRole;
import exception.FileProcessingException;
import model.Admin;
import model.Passenger;
import model.User;
import model.Ticket;
import model.Station;
import model.Train;
import payment.PaymentRecord;

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
    	
    	// -----------------------------------------------------
        // User
        if (fileName.equalsIgnoreCase("users.txt")) {

            Collection<User> users;

            try {
                users = (Collection<User>) data;
            } catch (ClassCastException e) {
                throw new FileProcessingException(
                        "Expected a collection of users for file: " + fileName);
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

                for (User user : users) {
                    writer.write(toUserLine(user));
                    writer.newLine();
                }

            } catch (IOException e) {
                throw new FileProcessingException(
                		"Could not write to file: " + fileName, e);
            }

            return;
        }
    	
        
        // -----------------------------------------------------
        // Station
        if (fileName.equalsIgnoreCase("stations.txt")) {

            try {
                ArrayList<Station> stations = (ArrayList<Station>) data;

                try (PrintWriter outputFile = new PrintWriter(fileName)) {

                    for (Station station : stations) {
                        outputFile.println(
                                station.getStationId() + "|" +
                                station.getStationName() + "|" +
                                station.getStationLocation()
                        );
                    }
                }

                System.out.println("Data saved successfully to " + fileName);

            } catch (IOException e) {
                throw new FileProcessingException(
                        "Unable to save data to " + fileName, e);

            } catch (ClassCastException e) {
                throw new FileProcessingException(
                        "Invalid station data for " + fileName, e);
            }

            return;
        }
        
        
        // -----------------------------------------------------
        // Train
        if (fileName.equalsIgnoreCase("trains.txt")) {

            try {
                ArrayList<Train> trains = (ArrayList<Train>) data;

                try (PrintWriter outputFile = new PrintWriter(fileName)) {

                    for (Train train : trains) {
                        outputFile.println(
                                train.getTrainId() + "|" +
                                train.getTrainName() + "|" +
                                train.getCapacity()
                        );
                    }
                }

                System.out.println("Data saved successfully to " + fileName);

            } catch (IOException e) {
                throw new FileProcessingException(
                        "Unable to save data to " + fileName, e);

            } catch (ClassCastException e) {
                throw new FileProcessingException(
                        "Invalid train data for " + fileName, e);
            }

            return;
        }
        
        
        //Unknown File
        throw new FileProcessingException("Unsupported file: " + fileName);
    }

    
    /**
     * Reads the file and rebuilds a list of User objects. Returns an empty
     * list if the file does not exist yet (first run of the program).
     */
    @Override
    public Object loadData(String fileName) throws FileProcessingException {
    	
    	// -----------------------------------------------------
        // User
    	if (fileName.equalsIgnoreCase("users.txt")) {

            List<User> users = new ArrayList<>();

            File file = new File(fileName);

            // First run - file does not exist yet
            if (!file.exists()) {
                return users;
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

                    User user = fromUserLine(line, lineNo);

                    if (user != null) {
                        users.add(user);
                    }
                }

            } catch (IOException e) {
                throw new FileProcessingException(
                        "Could not read file: " + fileName, e);
            }

            return users;
    	}
    	
    	
        // -----------------------------------------------------
        // Station
    	if (fileName.equalsIgnoreCase("stations.txt")) {

            ArrayList<Station> stations = new ArrayList<>();

            File file = new File(fileName);

            if (!file.exists()) {
                return stations;
            }

            try (Scanner inputFile = new Scanner(file)) {

                while (inputFile.hasNextLine()) {
                    String line = inputFile.nextLine().trim();

                    if (line.isEmpty()) {
                        continue;
                    }

                    String[] data = line.split("\\|");

                    if (data.length < 3) {
                        System.out.println("Warning: skipping malformed station record.");
                        continue;
                    }

                    Station station =
                            new Station(
                                    data[0],
                                    data[1],
                                    data[2]
                            );

                    stations.add(station);
                }

            } catch (IOException e) {
                throw new FileProcessingException(
                        "Unable to load data from " + fileName, e);
            }

            return stations;
        }
    	
    	
        // -----------------------------------------------------
        // Train
    	if (fileName.equalsIgnoreCase("trains.txt")) {

            ArrayList<Train> trains = new ArrayList<>();

            File file = new File(fileName);

            if (!file.exists()) {
                return trains;
            }

            try (Scanner inputFile = new Scanner(file)) {

                while (inputFile.hasNextLine()) {
                    String line = inputFile.nextLine().trim();

                    if (line.isEmpty()) {
                        continue;
                    }

                    String[] data = line.split("\\|");

                    if (data.length < 3) {
                        System.out.println("Warning: skipping malformed train record.");
                        continue;
                    }

                    try {
                        Train train =
                                new Train(
                                        data[0],
                                        data[1],
                                        Integer.parseInt(data[2])
                                );
                        trains.add(train);

                    } catch (NumberFormatException e) {
                        throw new FileProcessingException(
                                "Invalid train capacity: " + data[2], e);
                    }
                }

            } catch (IOException e) {
                throw new FileProcessingException(
                        "Unable to load data from " + fileName, e);
            }

            return trains;
        }
    	
    	
    	//Unknown File
    	throw new FileProcessingException("Unsupported file: " + fileName);
    }
    
    
    // -----------------------------------------------------
    //User Helper Methods
    /** Converts a User object into a single text line. 
     * Format:
     * userId,name,email,password,role,balance  */
    private String toUserLine(User user) {
    	
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
    private User fromUserLine(String line, int lineNo) throws FileProcessingException {
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
    
    // -----------------------------------------------------
    //Ticket
    public static void saveTicket(Ticket ticket)
    {
        try
        {
            FileWriter fw = new FileWriter("tickets.txt", true);
            PrintWriter outputFile = new PrintWriter(fw);

            outputFile.println(ticket.toFileString());

            outputFile.close();
        }
        catch (IOException e)
        {
            System.out.println("Error saving ticket.");
        }
    }

    public static ArrayList<String> loadTickets()
    {
        ArrayList<String> lines = new ArrayList<String>();

        try
        {
            Scanner inputFile = new Scanner(new File("tickets.txt"));

            while (inputFile.hasNextLine())
            {
                lines.add(inputFile.nextLine());
            }

            inputFile.close();
        }
        catch (IOException e)
        {
            System.out.println("Error loading tickets.");
        }

        return lines;
    }
    
      
    // -----------------------------------------------------
    //Payment
    public static void savePayment(PaymentRecord record)
    {
        try
        {
            FileWriter fw = new FileWriter("payments.txt", true);
            PrintWriter outputFile = new PrintWriter(fw);

            outputFile.println(record.toFileLine());

            outputFile.close();
        }
        catch (IOException e)
        {
            System.out.println("Error saving payment.");
        }
    }
    
}