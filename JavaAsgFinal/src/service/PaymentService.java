package service;

import payment.Payment;
import payment.PaymentRecord;
import payment.CashPayment;
import payment.CardPayment;
import repository.TXTFileManager;

public class PaymentService
{
    private int nextPaymentId = 1001;

    public boolean processPayment(Payment payment, double amount)
    {
        if (payment == null)
        {
            System.out.println("Please select a payment method.");
            return false;
        }

        if (amount <= 0)
        {
            System.out.println("Payment failed: invalid amount.");
            return false;
        }

        boolean success = payment.pay(amount);

        if (!success)
        {
        	return false;
        }
        
        // Determine payment method
        String paymentMethod;

        if (payment instanceof CashPayment)
        {
            paymentMethod = "Cash";
        }
        else if (payment instanceof CardPayment)
        {
            paymentMethod = "Card";
        }
        else
        {
            System.out.println("Unsupported payment method.");
            return false;
        }
        
        // Generate payment ID
        String paymentId = String.format("P%04d", nextPaymentId++);
        
        // Create payment record
        PaymentRecord record =  new PaymentRecord(paymentId, paymentMethod, amount);
        
        // Save payment record
        TXTFileManager.savePayment(record);

        return true;
    }
}