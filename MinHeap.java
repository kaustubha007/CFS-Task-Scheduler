import java.util.Comparator;
import java.util.PriorityQueue;


public class MinHeap {
	private PriorityQueue<Process> prcHeap = new PriorityQueue<Process>(1, new Comparator<Process>() {
        @Override
        public int compare(Process o1, Process o2) {
        	if((o1.unfairnessScore - o2.unfairnessScore) < 0){
        		return -1;
        	}
        	else{
        		return 1;
        	}
        }
    }); 
	
	public void insertIntoHeap(Process prc)
	{
		this.prcHeap.add(prc);
	}
	
	public Process deleteFromHeap()
	{
		Process prcMin;
		
		prcMin = this.prcHeap.element();
		this.prcHeap.remove();
		return prcMin;
	}
	
	public Boolean isHeapEmpty()
	{
		if(this.prcHeap.size() == 0)
		{
			return true;
		}
		return false;
	}
	
	public int getHeapSize()
	{
		return this.prcHeap.size();
	}

}
