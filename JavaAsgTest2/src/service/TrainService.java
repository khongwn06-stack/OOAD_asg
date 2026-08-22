package service;

import java.util.ArrayList;
import model.Train;

public class TrainService
{
   
    private ArrayList<Train> trains;

    public TrainService()
    {
        trains = new ArrayList<Train>();
    }

    
    public boolean addTrain(Train train)
    {
    	for (Train existingTrain : trains)
        {
            if (existingTrain.getTrainId().equals(train.getTrainId()))
            {
                return false;
            }
        }

        trains.add(train);
        return true;
    }

   
    public void viewTrains()
    {
        System.out.println("----------------------------------");
        System.out.println("Train List");
        System.out.println("----------------------------------");

        /*for(int i = 0; i < trains.size(); i++)
        {
            trains.get(i).displayTrain();
            System.out.println("----------------------------------");
        }*/
        if (trains.isEmpty())
        {
            System.out.println("No trains available.");
        }
        else
        {
            for (Train train : trains)
            {
                train.displayTrain();
                System.out.println("----------------------------------");
            }
        }
    }

    
    public ArrayList<Train> getTrains()
    {
        return trains;
    }
}
