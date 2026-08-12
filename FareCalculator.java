package fare;

import enums.TicketType;
import model.Route;

public interface FareCalculator 
{
    double calculateFare(Route route, TicketType ticketType);
}
