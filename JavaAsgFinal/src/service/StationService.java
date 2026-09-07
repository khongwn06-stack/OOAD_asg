package service;

import java.util.ArrayList;
import java.util.Collections;

import exception.FileProcessingException;
import model.Station;
import repository.TXTFileManager;

public class StationService
{   
    private ArrayList<Station> stations;
    private TXTFileManager fileManager;
    private static final String FILE_NAME = "stations.txt";
   
    public StationService(){
        stations = new ArrayList<Station>();
        fileManager = new TXTFileManager();
    }
    
    public boolean addStation(Station station){
    	if (!stationValidate(station)){
            return false;
        }

        stations.add(station);
        System.out.println("Station added successfully.");
        System.out.println();
        saveStations();       
        return true;
    }
    
    //-------Stations Validation-------
    public boolean stationValidate(Station station) {
        boolean valid = true;
        if (!stationIdValidation(station)){
            valid = false;
        }
        if (!stationNameValidation(station)){
            valid = false;
        }
        if (!locationValidation(station)){
            valid = false;
        }

        // Stop adding if any validation failed
        if (!valid){
            System.out.println("Station add failed.");
            return false;
        }
        
        return true;
    }
    
    public boolean stationIdValidation(Station station){
        if (station.getStationId() == null || station.getStationId().trim().isEmpty()){
            System.out.println("Station ID cannot be empty.");
            return false;
        }
        if (station.getStationId().contains(" ")){
            System.out.println("Station ID cannot contain spaces.");
            return false;
        }
        // Check duplicate Station ID
        if (findStationById(station.getStationId()) != null){
            System.out.println("Station ID add failed: Station ID '" + station.getStationId() + "' already added.");
            return false;
        }
        return true;
    }
    
    public boolean stationNameValidation(Station station){
        if (station.getStationName() == null || station.getStationName().trim().isEmpty()){
            System.out.println("Station name cannot be empty.");
            return false;
        }
        if (station.getStationName().matches(".*\\d.*")){
            System.out.println("Station name cannot contain numbers.");
            return false;
        }
        // Check duplicate Station Name
        if (findStationByName(station.getStationName()) != null){
            System.out.println("Station name add failed: Station Name '" + station.getStationName() + "' already added.");
            return false;
        }
        return true;
    }
    
    public boolean locationValidation(Station station){
        if (station.getStationLocation() == null || station.getStationLocation().trim().isEmpty()){
            System.out.println("Location cannot be empty.");
            return false;
        }
        // Check duplicate Station Location
        if (findStationByLocation(station.getStationLocation()) != null){
            System.out.println("Location add failed: Location '" + station.getStationLocation() + "' already added.");
            return false;
        }
        return true;
    }
  
    
    
    public Station searchStation(String name){
        Station station = null;
        boolean found = false;
        int i = 0;

        while(i < stations.size() && !found){
            station = stations.get(i);

            if(station.getStationName().equalsIgnoreCase(name))
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
   
    public void viewStations(){
    	System.out.println();
        System.out.println("==================================");
        System.out.println("           Station List           ");
        System.out.println("==================================");

        if (stations.isEmpty())
        {
            System.out.println("No stations available.");
            return;
        }

        for(int i = 0; i < stations.size(); i++)
        {
            stations.get(i).displayInfo();
            System.out.println("----------------------------------");
        }
    }

   
    public void sortStations(){
        Collections.sort(stations);
    }

    
    public ArrayList<Station> getStations(){
        return stations;
    }
    
    
    //Find Station Information
    public Station findStationById(String stationId){
        for (Station station : stations){
            if (station.getStationId().equalsIgnoreCase(stationId)){
                return station;
            }
        }
        return null;
    }
    
    public Station findStationByName(String stationName){
        for (Station station : stations){
            if (station.getStationName().equalsIgnoreCase(stationName)){
                return station;
            }
        }
        return null;
    }
    
    public Station findStationByLocation(String location){
        for (Station station : stations){
            if (station.getStationLocation().equalsIgnoreCase(location)){
                return station;
            }
        }
        return null;
    }
    
    
    // Save Stations
    public void saveStations(){
        try{
            fileManager.saveData(stations, FILE_NAME);
            System.out.println("Stations saved successfully.");
        }
        catch (FileProcessingException e){
            System.out.println("Could not save stations: " + e.getMessage());
        }
    }
    
    // Load Stations
    @SuppressWarnings("unchecked")
    public void loadStations(){
        try{
            stations = (ArrayList<Station>) fileManager.loadData(FILE_NAME);
            System.out.println("Stations loaded from " + FILE_NAME);
        }
        catch (FileProcessingException e){
            System.out.println("Could not load stations: " + e.getMessage());
        }
    }
}