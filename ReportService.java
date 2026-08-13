package service;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ReportService
{
    public double calculateTotalRevenue()
    {
        double totalRevenue = 0.0;

        try
        {
            File file = new File("payments.txt");
            Scanner inputFile = new Scanner(file);

            while (inputFile.hasNext())
            {
                String paymentId = inputFile.next();
                String paymentMethod = inputFile.next();
                double amount = inputFile.nextDouble();

                totalRevenue += amount;
            }

            inputFile.close();
        }
        catch (IOException e)
        {
            System.out.println("Error reading payment file.");
        }

        return totalRevenue;
    }

    public void displayTotalRevenue()
    {
        double totalRevenue = calculateTotalRevenue();

        System.out.printf("Total Revenue: RM %.2f%n", totalRevenue);
    }
}
