package service;

import exception.FileProcessingException;
import model.Route;
import model.Station;
import repository.TXTFileManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RouteService 
{
	private ArrayList<Route> routes;
    private TXTFileManager fileManager;
    private static final String FILE_NAME = "routes.txt";

    public RouteService()
    {
        routes = new ArrayList<Route>();
        fileManager = new TXTFileManager();
    }

    public boolean addRoute(Route route){
    	if (!routeValidate(route))
        {
            return false;
        }

        routes.add(route);
        System.out.println("Route added successfully.");

        try
        {
        	System.out.println();
            saveRoutes();
        }
        catch (FileProcessingException e)
        {
            System.out.println("Could not save routes: " + e.getMessage());
        }
        return true;
    }
    
    //-------Routes Validation-------
    public boolean routeValidate(Route route)
    {
        boolean valid = true;

        if (!routeIdValidation(route))
        {
            valid = false;
        }

        if (!sourceValidation(route))
        {
            valid = false;
        }

        if (!destinationValidation(route))
        {
            valid = false;
        }

        if (!distanceValidation(route))
        {
            valid = false;
        }

        if (!valid)
        {
            System.out.println("Route add failed.");
            return false;
        }

        return true;
    }
    
    public boolean routeIdValidation(Route route){
        if (route.getRouteId() == null || route.getRouteId().trim().isEmpty()){
            System.out.println("Route ID cannot be empty.");
            return false;
        }

        if (route.getRouteId().contains(" ")){
            System.out.println("Route ID cannot contain spaces.");
            return false;
        }

        // Check duplicate Route ID
        if (findRouteById(route.getRouteId()) != null){
            System.out.println("Route ID add failed: Route ID '" + route.getRouteId() + "' already added.");
            return false;
        }
        return true;
    }
    
    public boolean sourceValidation(Route route)
    {
        if (route.getSource() == null)
        {
            System.out.println("Source station cannot be empty.");
            return false;
        }

        return true;
    }
    
    public boolean destinationValidation(Route route)
    {
        if (route.getDestination() == null)
        {
            System.out.println("Destination station cannot be empty.");
            return false;
        }

        if (route.getSource() != null &&
            route.getDestination() != null &&
            route.getSource().getStationId()
                .equalsIgnoreCase(route.getDestination().getStationId()))
        {
            System.out.println("Source and destination cannot be the same station.");

            return false;
        }

        return true;
    }

    public boolean distanceValidation(Route route)
    {
    	if (route.getDistanceKm() == 0){
            System.out.println("Distance cannot be empty.");
            return false;
        }
        if (route.getDistanceKm() <= 0){
            System.out.println("Invalid distance. Please enter numbers only, without units (e.g. 12.5).");
            return false;
        }
        return true;
    }
    
    

    public Route findRoute(Station source, Station destination){
        for (Route r : routes) 
        {
            if (r.getSource().getStationId().equalsIgnoreCase(source.getStationId())
                    && r.getDestination().getStationId().equalsIgnoreCase(destination.getStationId())) {
                return r;
            }
        }
        return null;
    }

    public ArrayList<Route> getAllRoutes() 
    {
        return routes;
    }

    public boolean viewRoutes() 
    {
    	System.out.println();
    	System.out.println("==================================");
        System.out.println("            Route List            ");
        System.out.println("==================================");
        
        if (routes.isEmpty()) 
        {
            System.out.println("No routes available yet.");
            return false;
        }
        for (Route r : routes) 
        {
            r.displayRoute();
            System.out.println("----------------------------------");
        }
        return true;
    }

    public Route findRouteById(String routeId) 
    {
        for (Route r : routes) 
        {
            if (r.getRouteId().equalsIgnoreCase(routeId)) 
            {
                return r;
            }
        }
        return null;
    }

    public void sortRoutesByDistance() 
    {
        Collections.sort(routes, new Comparator<Route>() 
        {
            @Override
            public int compare(Route r1, Route r2) 
            {
                return Double.compare(r1.getDistanceKm(), r2.getDistanceKm());
            }
        });
        System.out.println("Routes sorted by distance (shortest first).");
    }

    public void saveRoutes() throws FileProcessingException
    {
        try
        {
            fileManager.saveData(routes, FILE_NAME);
            System.out.println("Routes saved successfully.");
        }
        catch (FileProcessingException e)
        {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public void loadRoutes(ArrayList<Station> stationList) throws FileProcessingException 
    {
        try 
        {
            Object data = fileManager.loadData(FILE_NAME);
            if (!(data instanceof List)) 
            {
                return;
            }
            List<String> lines = (List<String>) data;

            for (String line : lines) 
            {
                if (line.trim().isEmpty()) 
                {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length < 4) 
                {
                    continue;
                }

                String routeId = parts[0].trim();
                Station source = findStationById(stationList, parts[1].trim());
                Station destination = findStationById(stationList, parts[2].trim());
                double distance = Double.parseDouble(parts[3].trim());

                if (source != null && destination != null) 
                {
                    routes.add(new Route(routeId, source, destination, distance));
                } else {
                    System.out.println("Skipped route " + routeId + ": station not found.");
                }
            }
            System.out.println("Routes loaded from " + FILE_NAME);
        } catch (NumberFormatException e) {
            throw new FileProcessingException("Invalid distance value in " + FILE_NAME + ": " + e.getMessage(), e);
        } catch (Exception e) {
            throw new FileProcessingException("Failed to load " + FILE_NAME + ": " + e.getMessage(), e);
        }
    }

    private Station findStationById(ArrayList<Station> stationList, String stationId) 
      {
        for (Station s : stationList) {
            if (s.getStationId().equalsIgnoreCase(stationId)) 
            {
                return s;
            }
        }
        return null;
    }

}
