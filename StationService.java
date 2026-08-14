package Service;


import java.util.ArrayList;
import java.util.Collections;
import Model.Station;

public class StationService
{
    private ArrayList<Station> stations;

    public StationService()
    {
        stations = new ArrayList<Station>();
    }

    public void addStation(Station station)
    {
        stations.add(station);
    }

    public void viewStations()
    {
        System.out.println("---------------------------------");
        System.out.println("Station List");
        System.out.println("---------------------------------");

        for(int i = 0; i < stations.size(); i++)
        {
            stations.get(i).displayInfo();
            System.out.println("---------------------------------");
        }
    }

    public Station searchStation(String name)
    {
        Station station = null;
        boolean found = false;
        int i = 0;

        while(i < stations.size() && !found)
        {
            station = stations.get(i);

            if(station.getName().equalsIgnoreCase(name))
            {
                found = true;
            }
            else
            {
                i++;
            }
        }

        if(found)
        {
            return station;
        }
        else
        {
            return null;
        }
    }

    
    public void sortStations()
    {
        Collections.sort(stations);
    }

    public ArrayList<Station> getStations()
    {
        return stations;
    }
    
}
