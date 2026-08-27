package model;

public class Station implements Comparable<Station>
{
    
    private String stationId;
    private String stationname;
    private String stationlocation;

    
    public Station(String aStationId, String aStationName, String aStationLocation)
    {
        stationId = aStationId;
        stationname = aStationName;
        stationlocation = aStationLocation;
    }

    
    public String getStationId()
    {
        return stationId;
    }

    
    public String getStationName()
    {
        return stationname;
    }

    
    public String getStationLocation()
    {
        return stationlocation;
    }

  
    public void displayInfo()
    {
        System.out.println("Station ID       : " + stationId);
        System.out.println("Station Name     : " + stationname);
        System.out.println("Station Location : " + stationlocation);
    }

    @Override
    public int compareTo(Station anotherStation)
    {
        return stationname.compareToIgnoreCase(anotherStation.getStationName());
    }
	
}