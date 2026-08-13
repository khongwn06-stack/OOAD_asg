package payment;

public class PaymentRecord
{
    private String paymentId;
    private String paymentMethod;
    private double amount;

    public String getPaymentId()
    {
        return paymentId;
    }

    public String getPaymentMethod()
    {
        return paymentMethod;
    }

    public double getAmount()
    {
        return amount;
    }

    public PaymentRecord(String paymentId, String paymentMethod, double amount)
    {
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }

    public String toFileLine()
    {
        return paymentId + " " + paymentMethod + " " + amount;
    }

    @Override
    public String toString()
    {
        return paymentId + " | " + paymentMethod + " | RM" + String.format("%.2f", amount);
    }
}
