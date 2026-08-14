package Model;

public class Station implements Comparable<Station>
{
    
    private String stationId;
    private String name;
    private String location;

    
    public Station(String aStationId, String aName, String aLocation)
    {
        stationId = aStationId;
        name = aName;
        location = aLocation;
    }

    
    public String getStationId()
    {
        return stationId;
    }

    
    public String getName()
    {
        return name;
    }

    
    public String getLocation()
    {
        return location;
    }

  
    public void displayInfo()
    {
        System.out.println("Station ID : " + stationId);
        System.out.println("Name       : " + name);
        System.out.println("Location   : " + location);
    }

    @Override
    public int compareTo(Station anotherStation)
    {
        return name.compareToIgnoreCase(anotherStation.getName());
    }
	
}
