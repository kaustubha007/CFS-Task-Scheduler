import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FileReaderCFS
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args)
	{
		try 
		{
			Map dictOutput = new HashMap();
			Scanner in = new Scanner(System.in);
			String inputFile = in.nextLine();
			Integer snapShotTimeUnit = (Integer)in.nextInt();
			in.close();
			List<ArrayList<Integer>> prcArrayRBT = new ArrayList<ArrayList<Integer>>();
			File file = new File(inputFile);
//			File file = new File("flat4.txt");
//			Integer snapShotTimeUnit = 0;
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			Boolean flagQuantum = false;
			int quantumSlice = 5;
			String line;
			while ((line = bufferedReader.readLine()) != null) 
			{
				ArrayList<Integer> prcDetails = new ArrayList<Integer>();
				if(!flagQuantum)
				{
					String[] tmp = line.split( " " );
					quantumSlice = Integer.parseInt(tmp[0]);
					flagQuantum = true;
				}
				else
				{
					String[] tmp = line.split( " " );
					prcDetails.add(Integer.parseInt(tmp[0]));
					prcDetails.add(Integer.parseInt(tmp[1]));
					prcDetails.add(Integer.parseInt(tmp[2]));
					
					prcArrayRBT.add(prcDetails);
					
					stringBuffer.append("\n");
				}
			}
			fileReader.close();
			prcArrayRBT = MergeSort.sortProcessInput(prcArrayRBT, 0, prcArrayRBT.size() - 1);
			
			List<ArrayList<Integer>> prcArrayHeap = new ArrayList<ArrayList<Integer>>(prcArrayRBT);
			
			if(prcArrayRBT.size() > 50)
			{
				snapShotTimeUnit = 0;
			}
			CFSScheduler schedulerObj = new CFSScheduler(); 
//			System.out.println("************Executing scheduler with red black tree************\n");
			long startTimeRBT = System.nanoTime();
			dictOutput = schedulerObj.runCFSSchedulerRBT(prcArrayRBT, quantumSlice, snapShotTimeUnit, dictOutput);
			long endTimeRBT = System.nanoTime();
			double durationRBT = (endTimeRBT - startTimeRBT) / 1000000.0;
			dictOutput.put("'RBTExecutionTime'", "'" + durationRBT + "'");
//			System.out.println("\n-------------------------------------\n");
//			System.out.println("************Executing scheduler with Heap************\n");
			long startTimeHeap = System.nanoTime();
			schedulerObj.runCFSSchedulerMinHeap(prcArrayHeap, quantumSlice, snapShotTimeUnit, dictOutput);
			long endTimeHeap = System.nanoTime();
			double durationHeap = (endTimeHeap - startTimeHeap) / 1000000.0;
			dictOutput.put("'HeapExecutionTime'", "'" + durationHeap + "'");
			System.out.println(dictOutput);
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	

}
