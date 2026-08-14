package repository;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import model.Station;
import model.Train;

public class TXTFileManager implements FileManager
{
   
    @Override
    public void saveData(Object data, String fileName)
    {
        try
        {
            PrintWriter outputFile =
                    new PrintWriter(fileName);

            if(fileName.equalsIgnoreCase("stations.txt"))
            {
                ArrayList<Station> stations =
                        (ArrayList<Station>) data;

                for(int i = 0; i < stations.size(); i++)
                {
                    Station station = stations.get(i);

                    outputFile.println(
                            station.getStationId() + "|" +
                            station.getName() + "|" +
                            station.getLocation()
                    );
                }
            }
            else if(fileName.equalsIgnoreCase("trains.txt"))
            {
                ArrayList<Train> trains =
                        (ArrayList<Train>) data;

                for(int i = 0; i < trains.size(); i++)
                {
                    Train train = trains.get(i);

                    outputFile.println(
                            train.getTrainId() + "|" +
                            train.getTrainName() + "|" +
                            train.getCapacity()
                    );
                }
            }

            outputFile.close();

            System.out.println(
                    "Data saved successfully to " + fileName
            );
        }
        catch(FileNotFoundException e)
        {
            System.out.println(
                    "Unable to save data to " + fileName
            );
        }
    }

    @Override
    public Object loadData(String fileName)
    {
        try
        {
            Scanner inputFile =
                    new Scanner(new File(fileName));

            if(fileName.equalsIgnoreCase("stations.txt"))
            {
                ArrayList<Station> stations =
                        new ArrayList<Station>();

                while(inputFile.hasNextLine())
                {
                    String line = inputFile.nextLine();

                    String[] data = line.split("\\|");

                    Station station =
                            new Station(
                                    data[0],
                                    data[1],
                                    data[2]
                            );

                    stations.add(station);
                }

                inputFile.close();

                return stations;
            }
            else if(fileName.equalsIgnoreCase("trains.txt"))
            {
                ArrayList<Train> trains =
                        new ArrayList<Train>();

                while(inputFile.hasNextLine())
                {
                    String line = inputFile.nextLine();

                    String[] data = line.split("\\|");

                    Train train =
                            new Train(
                                    data[0],
                                    data[1],
                                    Integer.parseInt(data[2])
                            );

                    trains.add(train);
                }

                inputFile.close();

                return trains;
            }

            inputFile.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println(
                    "Unable to load data from " + fileName
            );
        }

        return null;
    }
}
