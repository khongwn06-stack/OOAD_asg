package service;

import exception.FileProcessingException;

//import app.UserModuleTest;
import model.Route;
import model.Station;
import repository.FileManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RouteService 
{
	//private static final UserModuleTest userModuleTest = new UserModuleTest();

    private ArrayList<Route> routes;
    private static final String FILE_NAME = "routes.txt";

    public RouteService(){
        routes = new ArrayList<>();
    }

    public void addRoute(Route route){
        routes.add(route);
        System.out.println("Route added successfully: " + route.getRouteId());
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

    public void viewRoutes() 
    {
        if (routes.isEmpty()) 
        {
            System.out.println("No routes available yet.");
            return;
        }
        for (Route r : routes) 
        {
            r.displayRoute();
            System.out.println("-----------------------------");
        }
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

    public void saveRoutes(FileManager fileManager) throws FileProcessingException 
    {
        try 
        {
            List<String> lines = new ArrayList<>();
            for (Route r : routes) 
            {
                lines.add(r.toFileString());
            }
            fileManager.saveData(lines, FILE_NAME);
            System.out.println("Routes saved to " + FILE_NAME);
        } catch (Exception e) {
            throw new FileProcessingException("Failed to save " + FILE_NAME + ": " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadRoutes(FileManager fileManager, ArrayList<Station> stationList) throws FileProcessingException 
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


    
/*    public Route selectRoute() {
        viewRoutes();
        String routeId = userModuleTest.readString("Enter Route ID: ");
        Route route = findRouteById(routeId);

        if (route == null) {
            System.out.println("Route not found.");
        }

        return route;
    }*/
}