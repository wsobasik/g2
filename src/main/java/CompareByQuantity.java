import java.util.Comparator;
import java.util.Map;

 class CompareByQuantity implements Comparator<Map.Entry<String, Integer>>
	{
	    // Used for sorting in ascending order of
	    // roll name List<Map.Entry<String,Integer>>
	
	
	
	    public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b)
	    {

			//System.out.println();
	        return (a.getValue()).compareTo(b.getValue());
	    }
	}