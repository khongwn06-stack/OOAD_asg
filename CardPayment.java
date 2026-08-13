package payment;

public class CardPayment implements Payment 
{
    private String cardNumber;
    
    public String getCardNumber() 
    {
        return cardNumber;
    }
    
    public CardPayment(String cardNumber)
    {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean pay(double amount)
    {

        if (amount <= 0)
        {
            System.out.println("Card payment failed: invalid amount.");
            return false;
        }

        else
        {
            System.out.printf("Card payment successful: RM %.2f received%n", amount);
            return true;
        }
        
        //System.out.printf("Card payment successful using card ending in %s: RM %.2f%n", getMaskedCardNumber(), amount);
    }






