package fare;

import model.Route;
import enums.TicketType;

public class StandardFareCalculator implements FareCalculator {

	//The standard fare rate is set at RM0.50 per kilometre, with a minimum fare of RM1.00.
	private double baseRatePerKm = 0.50;
	private double minimumFare = 1.00;
   
	public double getBaseRatePerKm()
	{
		return baseRatePerKm;
	}

	public void setBaseRatePerKm(double baseRatePerKm)
	{
		this.baseRatePerKm = baseRatePerKm;
	}
	
	public double getMinimumFare()
	{
		return minimumFare;
	}

	public void setMinimumFare(double minimumFare)
	{
		this.minimumFare = minimumFare;
	}
    
	@Override
	public double calculateFare(Route route, TicketType ticketType)
	{
	    if (route == null || ticketType == null)
	    {
	        return 0.0;
	    }

	    if (ticketType == TicketType.DAILY)
	    {
	        return 10.00;// DAILY: Fixed fare of RM10.00.
	    }

	    if (ticketType == TicketType.MONTHLY)
	    {
	        return 60.00;// MONTHLY: Fixed fare of RM60.00.
	    }

	    double fare =  route.calculateDistance() * baseRatePerKm;
	    
	    if (fare < minimumFare)
	    {
	        fare = minimumFare;
	    }

	    return fare;
	}
}
