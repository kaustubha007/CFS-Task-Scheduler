
public class Process 
{
	public int prcId;
	public float spentTime;
	public float arrivalTime;
	public float totalExecTime; 
	public float unfairnessScore; //Calculating this as arrivalTime + spentTime
	
	public Process(int Id, float newArrivalTime, float newSpentTime, float newTotalExecTime)
	{
		
		prcId = Id;
		arrivalTime = newArrivalTime;
		spentTime = newSpentTime;
		unfairnessScore = arrivalTime + spentTime;
		totalExecTime = newTotalExecTime;
	}
	
}
