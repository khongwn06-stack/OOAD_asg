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

    
    public void addTrain(Train train)
    {
        trains.add(train);
    }

   
    public void viewTrains()
    {
        System.out.println("----------------------------------");
        System.out.println("Train List");
        System.out.println("----------------------------------");

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
}
