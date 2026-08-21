package payment;

public class CashPayment implements Payment
{
    @Override
    public boolean pay(double amount) 
    {
        if (amount <= 0)
        {
            System.out.println("Cash payment failed: invalid amount.");
            return false;
        }
        else
        {
            System.out.printf("Cash payment successful: RM %.2f received.%n", amount);
            return true;
        }
    }
}
