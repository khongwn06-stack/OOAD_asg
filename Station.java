package model;

public class Station 
{
	private String stationId;
	private String stationName;
	private String location;

public Station(String aStationId, String aStationName, String aLocation)
	{
		stationId = aStationId;
		stationName = aStationName;
		location = aLocaltion;
	}

public String getStationId()
	{
		return stationId;
	}

public String getStationName()
	{ 
		return stationName;
	}

public String getLocation()
	{
		return location;
	}

public void setStationName(String newStationName)
	{
		stationName = newStationName;
	}

public void setLocation(String newLocation)
	{
		location = newLocation;
	}

public void displayInfo()
	{
		System.out.println("Station ID   : " + stationId);
		System.out.println("Station Name : " + stationName);
		System.out.println("Location     : " + location);
	}
}

	
		
	
	
