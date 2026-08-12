package fare;

import enums.TicketType;
import model.Route;

public class StandardFareCalculator implements FareCalculator 
{

    private static final double BASE_FARE = 1.00;
    private static final double RATE_PER_KM = 0.20;

    @Override
    public double calculateFare(Route route, TicketType ticketType) 
    {
        double distance = route.getDistanceKm();
        double fare = BASE_FARE + (distance * RATE_PER_KM);

        switch (ticketType) 
        {
            case SINGLE:
                break;
            case DAILY:
                fare = fare * 3;
                break;
            case MONTHLY:
                fare = fare * 20;
                break;
            default:
                break;
        }

        return Math.round(fare * 100.0) / 100.0;
    }
}
