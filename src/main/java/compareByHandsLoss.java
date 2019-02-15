import java.util.Comparator;

public class compareByHandsLoss implements Comparator<Stock> {



            @Override
        public int compare(Stock o1, Stock o2) {

                if (o1.getHandsProfit()< o2.getHandsProfit())  {return  1;}
                if (o1.getHandsProfit()> o2.getHandsProfit())  {return  -1;}
                //if (o1.getHandsProfit()== o2.getHandsProfit())  {return  0;}
       return 0;
            }

}

