package service;

import java.util.ArrayList;

import enums.TicketType;
import fare.FareCalculator;
import model.Passenger;
import model.Route;
import model.Ticket;
import exception.TicketNotFoundException;
import repository.TxtFileManager;

public class TicketService
{
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
        double fare = fareCalculator.calculateFare(route, type);

        String ticketId = String.format("P%04d", nextTicketId++);

        Ticket ticket = new Ticket(ticketId, passenger, route.getSource(), route.getDestination(), type, fare);

        tickets.add(ticket);

        System.out.println("\nTicket purchased successfully!");
        ticket.printTicket();

        TxtFileManager.saveTicket(ticket);
        
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
}
