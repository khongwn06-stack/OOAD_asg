package service;

import java.util.ArrayList;

import enums.TicketStatus;
import enums.TicketType;
import fare.FareCalculator;
import model.Passenger;
import model.Route;
import model.Station;
import model.Ticket;
import exception.TicketNotFoundException;
import repository.TXTFileManager;

public class TicketService
{
    private ArrayList<Ticket> tickets;
    private FareCalculator fareCalculator;
    private UserService userService;

    private int nextTicketId = 1001;

    public TicketService(
            FareCalculator fareCalculator,
            UserService userService)
    {
        this.tickets = new ArrayList<Ticket>();
        this.fareCalculator = fareCalculator;
        this.userService = userService;
    }

    
    public Ticket buyTicket(Passenger passenger, Route route, TicketType type)
    {
    	if (passenger == null || route == null || type == null)
        {
            System.out.println("\nInvalid ticket information.");
            return null;
        }
    	
        double fare = fareCalculator.calculateFare(route, type);
        
        if (!passenger.deduct(fare))
        {
            System.out.println("\nInsufficient balance.");
            return null;
        }

        String ticketId = generateTicketId();

        Ticket ticket = new Ticket(
        		ticketId, 
        		passenger, 
        		route.getSource(), 
        		route.getDestination(), 
        		type, 
        		fare
        		);
        tickets.add(ticket);

        System.out.println("\nTicket purchased successfully!");
        ticket.printTicket();
        
        // Save Ticket
        TXTFileManager.saveTicket(ticket);
        // Save updated passenger balance
        userService.saveUsers();
        
        return ticket;
    }
    
    // Generate Ticket ID
    private String generateTicketId()
    {
        String ticketId;

        do
        {
            ticketId = String.format("T%04d", nextTicketId++);
        }
        while (ticketIdExists(ticketId));

        return ticketId;
    }
    
    // Check whether Ticket ID already exists
    private boolean ticketIdExists(String ticketId)
    {
        for (Ticket ticket : tickets)
        {
            if (ticket.getTicketId().equalsIgnoreCase(ticketId))
            {
                return true;
            }
        }

        return false;
    }
    
    
    public void cancelTicket(String ticketId) throws TicketNotFoundException
    {
        if (ticketId == null || ticketId.trim().isEmpty())
        {
            throw new TicketNotFoundException("Ticket ID cannot be empty.");
        }
        
        ticketId = ticketId.trim();

        for (Ticket ticket : tickets)
        {
            if (ticket.getTicketId().equalsIgnoreCase(ticketId.trim()))
            {
            	// Prevent cancelling an already cancelled ticket
                if (ticket.getStatus() == TicketStatus.CANCELLED)
                {
                    System.out.println("This ticket has already been cancelled.");
                    return;
                }
                
                // Refund fare
                Passenger passenger = ticket.getPassenger();

                passenger.addBalance(ticket.getFare());

                // Change status
                ticket.cancelTicket();
                System.out.println();

                System.out.printf(
                        "RM %.2f has been refunded to your wallet.%n",
                        ticket.getFare()
                );

                System.out.printf(
                        "Current balance: RM %.2f%n\n",
                        passenger.getBalance()
                );
                
                // Update ticket file
                TXTFileManager.saveTicket(ticket);

                // Save updated passenger balance
                userService.saveUsers();

                return;
            }
        }

        throw new TicketNotFoundException("Ticket with ID '" + ticketId + "' was not found.");
    }

    public void viewTickets(Passenger passenger)
    {
    	System.out.println("\n========== METRO TICKET ==========");
        if (passenger == null)
        {
            System.out.println("Passenger not found.");
            return;
        }

        boolean found = false;

        for (Ticket ticket : tickets)
        {
            if (ticket.getPassenger().getUserId().equals(passenger.getUserId()))
            {
                ticket.printTicket();
                System.out.println("==================================");
                found = true;
            }
        }

        if (!found)
        {
            System.out.println("You have not bought any tickets yet.");
        }
    }
    
    public boolean viewActiveTickets(Passenger passenger)
    {
        System.out.println("\n========== ACTIVE METRO TICKET ==========");

        if (passenger == null)
        {
            System.out.println("Passenger not found.");
            return false;
        }

        boolean found = false;

        for (Ticket ticket : tickets)
        {
            if (ticket.getPassenger().getUserId().equals(passenger.getUserId())
                    && ticket.getStatus() == TicketStatus.ACTIVE)
            {
                ticket.printTicket();
                System.out.println("===========================================");
                found = true;
            }
        }

        if (!found)
        {
            System.out.println("You have no active tickets to cancel.");
        }
    
        return found;
    }

    public ArrayList<Ticket> getTickets()
    {
        return tickets;
    }
    
    public Ticket findTicketById(String ticketId)
            throws TicketNotFoundException
    {
        if (ticketId == null || ticketId.trim().isEmpty())
        {
            throw new TicketNotFoundException("Ticket ID not found.");
        }

        for (Ticket ticket : tickets)
        {
            if (ticket.getTicketId().equalsIgnoreCase(ticketId.trim()))
            {
                return ticket;
            }
        }

        throw new TicketNotFoundException(
                "Ticket with ID '" + ticketId + "' was not found."
        );
    }
    
    // Load Tickets From File
    public void loadTickets(
            ArrayList<Passenger> passengers,
            ArrayList<Station> stations)
    {
        tickets.clear();

        ArrayList<String> lines =
                TXTFileManager.loadTickets();

        int highestTicketNumber = 1000;

        for (String line : lines)
        {
            if (line == null || line.trim().isEmpty())
            {
                continue;
            }

            try
            {
                String[] data = line.trim().split("\\s+");

                // Expected:
                // ticketId passengerId sourceId destinationId
                // ticketType status fare

                if (data.length < 7)
                {
                    System.out.println(
                            "Warning: skipping malformed ticket record."
                    );
                    continue;
                }

                String ticketId = data[0];
                String passengerId = data[1];
                String sourceId = data[2];
                String destinationId = data[3];

                TicketType ticketType =
                        TicketType.valueOf(
                                data[4].toUpperCase()
                        );

                TicketStatus status =
                        TicketStatus.valueOf(
                                data[5].toUpperCase()
                        );

                double fare =
                        Double.parseDouble(data[6]);


                // Find Passenger
                Passenger passenger =
                        findPassenger(passengers, passengerId);

                if (passenger == null)
                {
                    System.out.println(
                            "Warning: passenger "
                            + passengerId
                            + " not found for ticket "
                            + ticketId
                    );
                    continue;
                }


                // Find Source Station
                Station source =
                        findStation(stations, sourceId);

                if (source == null)
                {
                    System.out.println(
                            "Warning: source station "
                            + sourceId
                            + " not found for ticket "
                            + ticketId
                    );
                    continue;
                }


                // Find Destination Station
                Station destination =
                        findStation(stations, destinationId);

                if (destination == null)
                {
                    System.out.println(
                            "Warning: destination station "
                            + destinationId
                            + " not found for ticket "
                            + ticketId
                    );
                    continue;
                }


                // Rebuild Ticket object
                Ticket ticket = new Ticket(
                        ticketId,
                        passenger,
                        source,
                        destination,
                        ticketType,
                        status,
                        fare
                );

                tickets.add(ticket);


                // Find highest ticket number
                if (ticketId.startsWith("T"))
                {
                    try
                    {
                        int number =
                                Integer.parseInt(
                                        ticketId.substring(1)
                                );

                        if (number > highestTicketNumber)
                        {
                            highestTicketNumber = number;
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        // Ignore invalid ticket number
                    }
                }

            }
            catch (IllegalArgumentException e)
            {
                System.out.println(
                        "Warning: invalid ticket record: "
                        + line
                );
            }
        }

        // Continue from highest existing Ticket ID
        nextTicketId = highestTicketNumber + 1;
    }
    
    // Find Passenger
    private Passenger findPassenger(
            ArrayList<Passenger> passengers,
            String passengerId)
    {
        for (Passenger passenger : passengers)
        {
            if (passenger.getUserId()
                    .equalsIgnoreCase(passengerId))
            {
                return passenger;
            }
        }

        return null;
    }

    // Find Station
    private Station findStation(
            ArrayList<Station> stations,
            String stationId)
    {
        for (Station station : stations)
        {
            if (station.getStationId()
                    .equalsIgnoreCase(stationId))
            {
                return station;
            }
        }

        return null;
    }
}
