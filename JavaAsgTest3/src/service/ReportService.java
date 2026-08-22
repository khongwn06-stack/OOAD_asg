package service;

import java.util.ArrayList;

import enums.TicketStatus;
import model.Ticket;

public class ReportService
{
    public void generateTicketReport(ArrayList<Ticket> tickets)
    {
        int totalTickets = 0;
        int cancelledTickets = 0;
        double totalRevenue = 0.0;
        
        if (tickets == null)
        {
            System.out.println("No ticket data available.");
            return;
        }
        
        for (Ticket ticket : tickets)
        {
            if (ticket.getStatus() == TicketStatus.CANCELLED)
            {
                cancelledTickets++;
            }
            else
            {
            	totalTickets++;
            	totalRevenue = totalRevenue + ticket.getFare();
            }
        }
        System.out.println();
        System.out.println("============ Ticket Report ============");
        System.out.println("Total Tickets Sold : " + totalTickets);
        System.out.println("Total Revenue	   : RM" + String.format("%.2f", totalRevenue));
        System.out.println("Cancelled Tickets  : " + cancelledTickets);
    }
}