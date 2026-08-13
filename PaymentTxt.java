package repository;

import payment.PaymentRecord;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class TxtFileManager
{
    public static void savePayment(PaymentRecord record)
    {
        try
        {
            FileWriter fw = new FileWriter("payments.txt", true);
            PrintWriter outputFile = new PrintWriter(fw);

            outputFile.println(record.toFileLine());

            outputFile.close();
        }
        catch (IOException e)
        {
            System.out.println("Error saving payment.");
        }
    }
}
