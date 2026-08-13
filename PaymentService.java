package service;

import payment.Payment;

public class PaymentService
{
    public boolean processPayment(Payment payment, double amount)
  {
        if (payment == null)
        {
            System.out.println("Payment method cannot be null.");
            return false;
        }

        if (amount <= 0)
        {
            System.out.println("Payment amount must be greater than RM 0.");
            return false;
        }

        return payment.pay(amount);
    }
}
