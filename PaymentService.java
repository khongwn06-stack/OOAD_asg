package service;

import payment.Payment;
import payment.PaymentRecord;
import payment.CashPayment;
import payment.CardPayment;
import file.TxtFileManager;

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
            else
            {
                return false;
            }

            String paymentId = String.format("P%04d", nextPaymentId++);

            PaymentRecord record =  new PaymentRecord(paymentId, paymentMethod, amount);

            TxtFileManager.savePayment(record);
        }

        return success;
    }
}
