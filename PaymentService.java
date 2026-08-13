package service;

import payment.Payment;
import payment.PaymentRecord;
import payment.CashPayment;
import payment.CardPayment;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class PaymentService
{
    private int nextPaymentId = 1001;

    public boolean processPayment(Payment payment, double amount)
    {
        if (payment == null)
        {
            System.out.println("Please select a payment me.");
            return false;
        }

        if (amount <= 0)
        {
            System.out.println("Payment failed: invalid amount.");
            return false;
        }

        boolean success = payment.pay(amount);

        if (success)
        {
            String paymentMethod;

            if (payment instanceof CashPayment)
            {
                paymentMethod = "Cash";
            }
            else if (payment instanceof CardPayment)
            {
                paymentMethod = "Card";
            }

            String paymentId = String.format("P%04d", nextPaymentId++);

            PaymentRecord record = new PaymentRecord(paymentId, paymentMethod, amount);

            savePayment(record);
        }

        return success;
    }

    private void savePayment(PaymentRecord record)
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
            System.out.println("Error writing to file.");
        }
    }
}
