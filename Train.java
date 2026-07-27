package model;

public class Train 
{
	private String trainId;
	private String trainName;
	private int capacity;
	

	public Train(String aTrainId, String aTrainName, int aCapacity) 
		{
			trainId = aTrainId;
			trainName = aTrainName;
			capacity = aCapacity;
		}
		
	public String getTrainId()
		{
			return trainId;
		}
		
	public String getTrainName()
		{
			return trainNmae;
		}
		
	public int getCapacity()
		{
			return capacity;
		}
	
	public void setTrainName(String newTrainName)
		{
			trainName = newTrainName;
		}

	public void setCapacity(String newCapacity)
		{
			capacity = newCapacity;
		}

	public void displayInfo()
		{
			System.out.println("Train ID       : " + trainId);
			System.out.println("Train Name     : " + trainName);
			System.out.println("Train Capacity : " + capacity);
		}
}
	
