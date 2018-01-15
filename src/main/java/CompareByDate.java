import java.util.Comparator;

class CompareByDate implements Comparator<Stock>
	{
	    // Used for sorting in ascending order of
	    // roll nam  hgde
	    public int compare(Stock a, Stock b)
	    {
	        return a.getDataWykonaniaTransackji().compareTo(b.getDataWykonaniaTransackji()); 
	    }
	}