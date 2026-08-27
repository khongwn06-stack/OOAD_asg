package service;

import java.util.ArrayList;
//import java.util.Collections;

import exception.FileProcessingException;
import model.Train;
import repository.TXTFileManager;

public class TrainService
{
   
	private ArrayList<Train> trains;
    private TXTFileManager fileManager;
    private static final String FILE_NAME = "trains.txt";

    public TrainService()
    {
        trains = new ArrayList<Train>();
        fileManager = new TXTFileManager();
    }


    
    public boolean addTrain(Train train){
    	if (!trainValidate(train)){
            return false;
        }

        trains.add(train);
        System.out.println("Train added successfully.");
        System.out.println();
        saveTrains();
        return true;
    }
    
  //-------Trains Validation-------
    public boolean trainValidate(Train train){
        boolean valid = true;
        if (!trainIdValidation(train)){
            valid = false;
        }
        if (!trainNameValidation(train)){
            valid = false;
        }
        if (!trainCapacityValidation(train)){
            valid = false;
        }
        
        if (!valid){
            System.out.println("Train add failed.");
            return false;
        }
        return true;
    }
    
    public boolean trainIdValidation(Train train){
        if (train.getTrainId() == null || train.getTrainId().trim().isEmpty()){
            System.out.println("Train ID cannot be empty.");
            return false;
        }
        if (train.getTrainId().contains(" ")){
            System.out.println("Train ID cannot contain spaces.");
            return false;
        }
        // Check duplicate Train ID
        if (findTrainById(train.getTrainId()) != null){
            System.out.println("Train ID add failed: Train ID '" + train.getTrainId() + "' already added.");
            return false;
        }
        return true;
    }
    
    public boolean trainNameValidation(Train train){
        if (train.getTrainName() == null || train.getTrainName().trim().isEmpty()){
            System.out.println("Train name cannot be empty.");
            return false;
        }
        if (train.getTrainName().matches(".*\\d.*")){
            System.out.println("Train name cannot contain numbers.");
            return false;
        }
        // Check duplicate Train Name
        if (findTrainByName(train.getTrainName()) != null){
            System.out.println("Train name add failed: Train Name '" + train.getTrainName() + "' already added.");
            return false;
        }
        return true;
    }
    
    public boolean trainCapacityValidation(Train train){
        if (train.getCapacity() < 30) {
            System.out.println("Train capacity cannot be less than 30.");
            return false;
        }
        return true;
    }
    
    
    public void viewTrains()
    {
    	System.out.println();
    	System.out.println("==================================");
        System.out.println("            Train List            ");
        System.out.println("==================================");

        if (trains.isEmpty())
        {
            System.out.println("No trains available.");
            return;
        }
        
        for(int i = 0; i < trains.size(); i++)
        {
            trains.get(i).displayTrain();
            System.out.println("----------------------------------");
        }
    }

    
    public ArrayList<Train> getTrains()
    {
        return trains;
    }
    
    
    //Find Train Information
    public Train findTrainById(String trainId)
    {
        for (Train train : trains)
        {
            if (train.getTrainId().equalsIgnoreCase(trainId))
            {
                return train;
            }
        }
        return null;
    }

    public Train findTrainByName(String trainName)
    {
        for (Train train : trains)
        {
            if (train.getTrainName().equalsIgnoreCase(trainName))
            {
                return train;
            }
        }
        return null;
    }

    
    //Save Train
    public void saveTrains()
    {
        try
        {
            fileManager.saveData(trains, FILE_NAME);
            System.out.println("Trains saved successfully.");
        }
        catch (FileProcessingException e)
        {
            System.out.println(
                "Could not save trains: " + e.getMessage()
            );
        }
    }
    
    //Load Train
    @SuppressWarnings("unchecked")
    public void loadTrains()
    {
        try
        {
            trains = (ArrayList<Train>)
                fileManager.loadData(FILE_NAME);

            System.out.println("Trains loaded from " + FILE_NAME);
        }
        catch (FileProcessingException e)
        {
            System.out.println(
                "Could not load trains: " +
                e.getMessage()
            );
        }
    }
    
}
