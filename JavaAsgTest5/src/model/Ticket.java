package model;

import enums.TicketStatus;
import enums.TicketType;

public class Ticket 
{
    private String ticketId;
    private Passenger passenger;
    private Station source;
    private Station destination;
    private TicketType ticketType;
    private TicketStatus status;
    private double fare;

    public String getTicketId() 
    {
        return ticketId;
    }

    public Passenger getPassenger() 
    {
        return passenger;
    }

    public Station getSource() 
    {
        return source;
    }

    public Station getDestination() 
    {
        return destination;
    }

    public TicketType getTicketType() 
    {
        return ticketType;
    }

    public TicketStatus getStatus() 
    {
        return status;
    }

    public double getFare() 
    {
        return fare;
    }

    // Used when buying a NEW ticket
    public Ticket(
            String ticketId,
            Passenger passenger,
            Station source,
            Station destination,
            TicketType ticketType,
            double fare) 
    {
        this.ticketId = ticketId;
        this.passenger = passenger;
        this.source = source;
        this.destination = destination;
        this.ticketType = ticketType;
        this.status = TicketStatus.ACTIVE;
        this.fare = fare;
    }

    // Used when loading an EXISTING ticket from file
    public Ticket(
            String ticketId,
            Passenger passenger,
            Station source,
            Station destination,
            TicketType ticketType,
            TicketStatus status,
            double fare) 
    {
        this.ticketId = ticketId;
        this.passenger = passenger;
        this.source = source;
        this.destination = destination;
        this.ticketType = ticketType;
        this.status = status;
        this.fare = fare;
    }
    
    public void printTicket() 
    {
        System.out.println();
        System.out.println("Ticket ID   : " + ticketId);
        System.out.println("Passenger   : " + passenger.userId);
        System.out.println("From        : " + source.getStationName());
        System.out.println("To          : " + destination.getStationName());
        System.out.println("Ticket Type : " + ticketType);
        System.out.printf ("Fare        : RM %.2f%n", fare);
        System.out.println("Status      : " + status);
        System.out.println();
    }


    public void cancelTicket()
    {
        status = TicketStatus.CANCELLED;
        System.out.println("Ticket " + ticketId + " has been cancelled.");
    }

    public String toFileString()
    {
        return ticketId + " " + passenger.getUserId() + " " + source.getStationId() + " " + destination.getStationId() + " " + ticketType + " " + status + " " + fare;
    }
    
    
    @Override
    public String toString() 
    {
        return "Ticket ID: " + ticketId +
        			" | From: " + source +
        			" | To: " + destination +
        			" | Type: " + ticketType +
        			" | Fare: RM " + String.format("%.2f", fare) +
        			" | Status: " + status;
    }
}
