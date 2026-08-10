package model;

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

    public Ticket(String ticketId, Passenger passenger, Station source, Station destination, TicketType ticketType, double fare) 
    {
        this.ticketId = ticketId;
        this.passenger = passenger;
        this.source = source;
        this.destination = destination;
        this.ticketType = ticketType;
        this.status = TicketStatus.ACTIVE;
        this.fare = fare;
    }

    public void printTicket() 
    {
        System.out.println("========== METRO TICKET ==========");
        System.out.println("Ticket ID    : " + ticketId);
        System.out.println("Passenger    : " + passenger);
        System.out.println("From         : " + source);
        System.out.println("To           : " + destination);
        System.out.println("Ticket Type  : " + ticketType);
        System.out.printf ("Fare         : RM %.2f%n", fare);
        System.out.println("Status       : " + status);
        System.out.println("==================================");
    }
