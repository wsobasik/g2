

import java.util.Comparator;

public class compareByLossOnAHand implements Comparator<Stock> {

        @Override
        public int compare(Stock o1, Stock o2) {
            Double o1Buy = o1.getVolumeAtHand()*o1.getAvaragePrizePerStockOnAHand();
            Double o2Buy =o2.getVolumeAtHand()*o1.getAvaragePrizePerStockOnAHand();

                return o1Buy.compareTo(o2Buy);


        }

    }





