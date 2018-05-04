import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CFSScheduler 
{
	Process currPrc;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map runCFSSchedulerRBT(List<ArrayList<Integer>> prcArray, int quantumSlice, int snapShotTimeUnit, Map dictOutput)
	{
		ArrayList<Integer> prcExec = new ArrayList<Integer>();
		Boolean snapshotFlag = false, getExecProcFlag = false;
		RBTree processRBT = new RBTree();
		float totalTime = 0;
		int pID; 
		float pArrTime, pTotalExecTime;
		float pSpentTime = 0;
		float maxExecutionTime = 0;
		List<String> snapshotRBTree = new ArrayList<String>();
		
		if(prcArray.size() <= 50)
		{
			getExecProcFlag = true;
		}
		
		
		while(true)
		{
			if(totalTime >= snapShotTimeUnit && !snapshotFlag && snapShotTimeUnit != 0)
			{
				snapshotRBTree = processRBT.printRBtree();
				snapshotFlag = true;
			}
			
			if (processRBT.isTreeEmpty(processRBT))
			{
				ArrayList<Integer> prc = getNextProcess(prcArray);
				Process tempPrc = null;
				prcArray.remove(0);
				pID = prc.get(0);
				pArrTime = prc.get(1);
				pTotalExecTime = prc.get(2);
				pSpentTime = Math.min(quantumSlice, pTotalExecTime);
				if(pSpentTime < pTotalExecTime)
				{
					tempPrc = new Process(pID, pArrTime, pSpentTime, pTotalExecTime);
					processRBT.insertNode(tempPrc);
				}
				totalTime = pSpentTime + pArrTime;
				prcExec.add(pID);
//				System.out.println("Process ID: "+ pID +"\nRemaining Time: " + (pTotalExecTime - pSpentTime));
//				System.out.println("Spent Time: " + pSpentTime);
//				System.out.println("Total Execution Time: " + pTotalExecTime);
//				System.out.println("Waiting Time: " + (0));
//				System.out.println("Unfairness score: " + pSpentTime + "\n");
				
			}
			else
			{
				if(prcArray.size() != 0)
				{
					while(prcArray.get(0).get(1) <= totalTime)
					{
						ArrayList<Integer> prc = getNextProcess(prcArray);
						Process tempPrc = null;
						prcArray.remove(0);
						pID = prc.get(0);
						pArrTime = prc.get(1);
						pTotalExecTime = prc.get(2);
						pSpentTime = 0;
						tempPrc = new Process(pID, pArrTime, pSpentTime, pTotalExecTime);
						processRBT.insertNode(tempPrc);
						if(prcArray.size() == 0)
						{
							break;
						}
					}
				}
				
				
				
				currPrc = processRBT.deleteNode().process;
				maxExecutionTime = Math.min((totalTime - currPrc.unfairnessScore) / (RBTree.nodeCount + 1), quantumSlice);
				if(maxExecutionTime == 0)
				{
					maxExecutionTime = quantumSlice;
				}
					
				if(maxExecutionTime>(currPrc.totalExecTime - currPrc.spentTime))
				{
					maxExecutionTime = (currPrc.totalExecTime - currPrc.spentTime);
				}
				currPrc.spentTime += maxExecutionTime;
				currPrc.unfairnessScore += maxExecutionTime;
				totalTime += maxExecutionTime;
				prcExec.add(currPrc.prcId);
//				System.out.println("Process ID: "+ currPrc.prcId +"\nRemaining Time: " + (currPrc.totalExecTime - currPrc.spentTime));
//				System.out.println("Spent Time: " + currPrc.spentTime);
//				System.out.println("Total Execution Time: " + currPrc.totalExecTime);
//				System.out.println("Waiting Time: " + (totalTime - currPrc.unfairnessScore));
//				System.out.println("Unfairness score: " + currPrc.unfairnessScore + "\n");
				
				if(currPrc.spentTime < currPrc.totalExecTime)
				{
					processRBT.insertNode(currPrc);
				}
			}
			
			if((prcArray.size() == 0) && processRBT.isTreeEmpty(processRBT))
			{
				break;
			}
			
		}
		dictOutput.put("'RBTreeSnapShot'", snapshotRBTree);
		if(getExecProcFlag)
		{
			dictOutput.put("'RBTExecutedProcesses'", prcExec);
		}
		return dictOutput;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map runCFSSchedulerMinHeap(List<ArrayList<Integer>> prcArray, int quantumSlice, int snapShotTimeUnit, Map dictOutput)
	{
		ArrayList<Integer> prcExec = new ArrayList<Integer>();
		Boolean getExecProcFlag = false;
		MinHeap processHeap = new MinHeap();
		float totalTime = 0;
		int pID; 
		float pArrTime, pTotalExecTime;
		float pSpentTime = 0;
		float maxExecutionTime = 0;
		
		if(prcArray.size() <= 50)
		{
			getExecProcFlag = true;
		}
		
		while(true)
		{
			if (processHeap.isHeapEmpty())
			{
				ArrayList<Integer> prc = getNextProcess(prcArray);
				Process tempPrc = null;
				prcArray.remove(0);
				pID = prc.get(0);
				pArrTime = prc.get(1);
				pTotalExecTime = prc.get(2);
				pSpentTime = Math.min(quantumSlice, pTotalExecTime);
				if(pSpentTime < pTotalExecTime)
				{
					tempPrc = new Process(pID, pArrTime, pSpentTime, pTotalExecTime);
					processHeap.insertIntoHeap(tempPrc);
				}
				totalTime = quantumSlice + pArrTime;
				prcExec.add(pID);
				
			}
			else
			{
				if(prcArray.size() != 0)
				{
					while(prcArray.get(0).get(1) <= totalTime)
					{
						ArrayList<Integer> prc = getNextProcess(prcArray);
						Process tempPrc = null;
						prcArray.remove(0);
						pID = prc.get(0);
						pArrTime = prc.get(1);
						pTotalExecTime = prc.get(2);
						pSpentTime = 0;
						tempPrc = new Process(pID, pArrTime, pSpentTime, pTotalExecTime);
						processHeap.insertIntoHeap(tempPrc);
						if(prcArray.size() == 0)
						{
							break;
						}
					}
				}
				
				currPrc = processHeap.deleteFromHeap();
				maxExecutionTime = Math.min((totalTime - currPrc.unfairnessScore) / (processHeap.getHeapSize() + 1), quantumSlice);
				if(maxExecutionTime == 0)
				{
					maxExecutionTime = quantumSlice;
				}
				if(maxExecutionTime>(currPrc.totalExecTime - currPrc.spentTime))
				{
					maxExecutionTime = (currPrc.totalExecTime - currPrc.spentTime);
				}
				currPrc.spentTime += maxExecutionTime;
				currPrc.unfairnessScore += maxExecutionTime;
				totalTime += maxExecutionTime;
				prcExec.add(currPrc.prcId);
//				System.out.println("Process ID: "+ currPrc.prcId +"\nRemaining Time: " + (currPrc.totalExecTime - currPrc.spentTime));
//				System.out.println("Spent Time: " + currPrc.spentTime);
//				System.out.println("Total Execution Time: " + currPrc.totalExecTime);
//				System.out.println("Waiting Time: " + (totalTime - currPrc.unfairnessScore));
//				System.out.println("Unfairness score: " + currPrc.unfairnessScore + "\n");
				
				if(currPrc.spentTime < currPrc.totalExecTime)
				{
					processHeap.insertIntoHeap(currPrc);
				}
			}
			
			if((prcArray.size() == 0) && processHeap.isHeapEmpty())
			{
				break;
			}
			
		}
		
//		System.out.println("Completed execution for all the processes.");
		if(getExecProcFlag)
		{
			dictOutput.put("'HeapExecutedProcesses'", prcExec);
		}
		return dictOutput;
	}
	
	private ArrayList<Integer> getNextProcess(List<ArrayList<Integer>> prcArray)
	{
		return prcArray.get(0);
	}
}
