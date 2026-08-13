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
