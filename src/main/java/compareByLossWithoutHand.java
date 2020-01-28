

import java.util.Comparator;

public class compareByLossWithoutHand implements Comparator<Stock> {



            @Override
        public int compare(Stock o1, Stock o2) {
            return o1.getProfit().compareTo(o2.getProfit());
       }

}

