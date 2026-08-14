package repository;

import model.Ticket;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TxtFileManager
{
    public static void saveTicket(Ticket ticket)
    {
        try
        {
            FileWriter fw = new FileWriter("tickets.txt", true);
            PrintWriter outputFile = new PrintWriter(fw);

            outputFile.println(ticket.toFileString());

            outputFile.close();
        }
        catch (IOException e)
        {
            System.out.println("Error saving ticket.");
        }
    }

    public static ArrayList<String> loadTickets()
    {
        ArrayList<String> lines = new ArrayList<String>();

        try
        {
            Scanner inputFile = new Scanner(new File("tickets.txt"));

            while (inputFile.hasNextLine())
            {
                lines.add(inputFile.nextLine());
            }

            inputFile.close();
        }
        catch (IOException e)
        {
            System.out.println("Error loading tickets.");
        }

        return lines;
    }
}
