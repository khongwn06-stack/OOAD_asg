package app;

public class main 
{
	public static void main(String[] args) 
	{
		System.out.println("================================");
		System.out.println("  SMART METRO TICKETING SYSTEM  ");
		System.out.println("================================\n");
		
		Station station1 = new Station("ST001", "KL Sentral", "Kuala Lumpur");
		Station station2 = new Station("ST002", "Central Market", "Kuala Lumpur");
		
		Train train1 = new Train("TR001", "KTM", 300);
		
		
	}

}
