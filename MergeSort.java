import java.util.ArrayList;
import java.util.List;

class MergeSort
{
    private static void mergeSort(List<ArrayList<Integer>> processArray, int lPart, int midpoint, int rPart)
    {
        int n1 = midpoint - lPart + 1;
        int n2 = rPart - midpoint;
 
        /* Create temp arrays */
        List<ArrayList<Integer>> leftArray = new ArrayList<ArrayList<Integer>>();
        List<ArrayList<Integer>> rightArray = new ArrayList<ArrayList<Integer>>();
        
        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            leftArray.add(i, processArray.get(lPart + i));
        for (int j=0; j<n2; ++j)
            rightArray.add(j, processArray.get(midpoint + 1 + j));
 
 
        /* Merge the temp arrays */
 
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
 
        // Initial index of merged subarry array
        int k = lPart;
        while (i < n1 && j < n2)
        {
            if (leftArray.get(i).get(1) <= rightArray.get(j).get(1))
            {
                processArray.set(k, leftArray.get(i));
                i++;
            }
            else
            {
                processArray.set(k, rightArray.get(j));
                j++;
            }
            k++;
        }
 
        /* Copy remaining elements of leftArray if any */
        while (i < n1)
        {
        	processArray.set(k, leftArray.get(i));
            i++;
            k++;
        }
 
        /* Copy remaining elements of rightArray if any */
        while (j < n2)
        {
        	processArray.set(k, rightArray.get(j));
            j++;
            k++;
        }
    }
 
    // Main function that sorts processArray using
    // merge()
    public static List<ArrayList<Integer>> sortProcessInput(List<ArrayList<Integer>> processArray, int lPart, int rPart)
    {
        if (lPart < rPart)
        {
            // Find the middle point
            int midpoint = (lPart + rPart) / 2;
 
            // Sort first and second halves
            sortProcessInput(processArray, lPart, midpoint);
            sortProcessInput(processArray, midpoint + 1, rPart);
 
            // Merge the sorted halves
            mergeSort(processArray, lPart, midpoint, rPart);
            
        }
        return processArray;
    }
}