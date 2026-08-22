package service;

import java.util.ArrayList;

//import app.UserModuleTest;
import enums.TicketType;
import fare.FareCalculator;
import model.Passenger;
import model.Route;
import model.Ticket;
import exception.TicketNotFoundException;
import repository.TXTFileManager;

public class TicketService
{
	//private static final UserModuleTest userModuleTest = new UserModuleTest();
	
    private ArrayList<Ticket> tickets;
    private FareCalculator fareCalculator;

    private int nextTicketId = 1001;

    public TicketService(FareCalculator fareCalculator)
    {
        this.tickets = new ArrayList<>();
        this.fareCalculator = fareCalculator;
    }

    public Ticket buyTicket(Passenger passenger, Route route, TicketType type)
    {
    	if (passenger == null || route == null || type == null)
        {
            System.out.println("Invalid ticket information.");
            return null;
        }
    	
        double fare = fareCalculator.calculateFare(route, type);
        
        if (!passenger.deduct(fare))
        {
            System.out.println("Insufficient balance.");
            return null;
        }

        String ticketId = String.format("T%04d", nextTicketId++);

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

        TXTFileManager.saveTicket(ticket);
        
        return ticket;
    }
    
    public void cancelTicket(String ticketId) throws TicketNotFoundException
    {
        if (ticketId == null || ticketId.trim().isEmpty())
        {
            throw new TicketNotFoundException("Ticket ID cannot be empty.");
        }

        for (Ticket ticket : tickets)
        {
            if (ticket.getTicketId().equalsIgnoreCase(ticketId.trim()))
            {
                ticket.cancelTicket();
                ticket.printTicket();
                return;
            }
        }

        throw new TicketNotFoundException("Ticket with ID '" + ticketId + "' was not found.");
    }

    public void viewTickets(Passenger passenger)
    {
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
                found = true;
            }
        }

        if (!found)
        {
            System.out.println("You have not bought any tickets yet.");
        }
    }


    /*public TicketType selectTicketType() {
        System.out.println("\nSelect Ticket Type:");
        System.out.println("1. Single");
        System.out.println("2. Daily Pass");
        System.out.println("3. Monthly Pass");

        int choice = userModuleTest.readInt("Enter choice: ");

        switch (choice) {
            case 1:
                return TicketType.SINGLE;
            case 2:
                return TicketType.DAILY;
            case 3:
                return TicketType.MONTHLY;
            default:
                System.out.println("Invalid ticket type.");
                return null;
        }
    }*/

    public ArrayList<Ticket> getTickets()
    {
        return tickets;
    }
}