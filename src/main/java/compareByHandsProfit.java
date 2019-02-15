import java.util.Comparator;

public class compareByHandsProfit implements Comparator<Stock> {



            @Override
        public int compare(Stock o1, Stock o2) {
            return o1.getHandsProfit().compareTo(o2.getHandsProfit());
       }

}

