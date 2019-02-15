import java.util.Comparator;

public class compareIfActualPrizeIsZero implements Comparator<Stock> {

        @Override
        public int compare(Stock o1, Stock o2) {

            //TODO na rece musi byz i cena zero

            {
            if ((o1.getActualPrize()==0) &&(o2.getActualPrize()!=0)){
                return -1;
            }
            if ((o1.getActualPrize()==0) &&(o2.getActualPrize()==0)){
                return 0;
            }
            if ((o1.getActualPrize()!=0) &&(o2.getActualPrize()==0)){
                return 1;
            }
}
                return 0;


        }

    }





