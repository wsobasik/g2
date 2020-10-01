import org.joda.time.LocalDateTime;
//import utils.compareByHandsLoss;


import java.util.*;

public class Vallet {

    public ArrayList<Stock> valletsStock = new ArrayList<>();

    public Vallet() {

    }

    public void countValletStock() {
        //count all the numbers

        for (Stock stock : valletsStock) {
            stock.updateValueBeforAfterTransactionAndOnAHand();
            stock.updateValueBeforeAfterRespectingSplit();
            stock.updateVolumeAtHand();
            stock.updateCashValueOfTransactions();
            stock.countBuysValue();
            stock.countSellsValue();
            stock.setProfit(stock.getBuys() - stock.getSells());
            stock.volumeAtHantTimesActualPrice();
            stock.countProfitInCashPlusValueAtHand();
            stock.setAvaragePrizePerStockOnAHand(stock.countAvaragePrizeOnStockOnAHand());
            stock.setHandsProfit();
        }
    }


    public void addSSplitData() {
        //TODO wczytac z pliku
        //this is to be rebuild and replaced
        int index;
        String stockName;
        stockName = "DREWEX";

        index = isStockInVallet(stockName); // returns StockIndex
        if (index > 0) {
            valletsStock.get(index).setSplitData((new ArrayList<SplitData>() {{
                add(new SplitData("DREWEX", 10, new LocalDateTime(2015, 6, 29, 0, 1)));
            }}));
        }

        stockName = "CIGAMES";

        index = isStockInVallet(stockName); // returns StockIndex
        if (index > 0) {
            valletsStock.get(index).setSplitData(new ArrayList<SplitData>() {{
                add(new SplitData("CIGAMES", 0.1, new LocalDateTime(2017, 2, 23, 0, 1))); //odwrocic dzielnik
            }});
        }

        stockName = "RESBUD";

        index = isStockInVallet(stockName); // returns StockIndex
        if (index > 0) {
            valletsStock.get(index).setSplitData(new ArrayList<SplitData>() {{
                add(new SplitData("RESBUD", 5, new LocalDateTime(2017, 1, 13, 0, 1)));
            }});
        }


        stockName = "01CYBATON";

        index = isStockInVallet(stockName); // returns StockIndex
        if (index > 0) {
            valletsStock.get(index).setSplitData(new ArrayList<SplitData>() {{
                add(new SplitData("01CYBATON", 0.05, new LocalDateTime(2015, 11, 25, 0, 1)));
            }});
        }

        stockName = "EFENERGII-NC";

        index = isStockInVallet(stockName); // returns StockIndex
        if (index > 0) {
            valletsStock.get(index).setSplitData(new ArrayList<SplitData>() {{
                add(new SplitData("EFENERGII-NC", 10, new LocalDateTime(2019, 1, 11, 0, 1)));
            }});
        }

        stockName = "HERKULES";

        index = isStockInVallet(stockName); // returns StockIndex
        if (index > 0) {
            valletsStock.get(index).setSplitData(new ArrayList<SplitData>() {{
                add(new SplitData("HERKULES", 5, new LocalDateTime(2012, 9, 19, 0, 1)));
            }});
        }
    }

    public void sortTransactionsByTime() {
        for (Stock stock : valletsStock) {
            ArrayList<Transaction> transactionsList = stock.getTransactionsList();
            Collections.sort(transactionsList, (t1, t2) -> t1.getTransactionDateTime().compareTo(t2.getTransactionDateTime()));
        }
    }

    public void sortValletsStockIfActualPrizeIsZeroAndByLossAndByLossOnAHand() {
        Collections.sort(valletsStock, new compareIfActualPrizeIsZero().thenComparing(new compareByLossWithoutHand()).thenComparing(new compareByLossOnAHand()));
    }

    public void sortValletsStockByLossAndIfActualPrizeIsZero() {
        Collections.sort(valletsStock, new compareByLossWithoutHand().thenComparing(new compareIfActualPrizeIsZero()));
    }

    public void sortByHandsProfit() {
        Collections.sort(valletsStock, new compareByHandsProfit());
    }

    public void sortByHandsLoss() {
        Collections.sort(valletsStock, new compareByHandsLoss());
    }
    public void sortByLossWithoutHand() {
        Collections.sort(valletsStock, new compareByLossWithoutHand());
    }

    public void sortByHandsLossAndIfActualPrizeIsZero() {
        Collections.sort(valletsStock, new compareByHandsLoss().thenComparing(new compareIfActualPrizeIsZero()));
    }


    public void sortValletsStockByProfitIncludesHandANDStockActualPrize() {
        Collections.sort(valletsStock, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                int profitCmp = o1.getProfitPlusValueOnAHand().compareTo(o2.getProfitPlusValueOnAHand());

                if ((o2.getActualPrize() == 0) && (o2.getVolumeAtHand() != 0)) return -1;
                if ((o1.getActualPrize() == 0) && (o1.getVolumeAtHand() != 0)) return 1;
                return profitCmp * (-1);

            }
        });

    }

    public void sortValletsStockByLossIncludesHandANDStockActualPrize() {
        Collections.sort(valletsStock, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                int profitCmp = o1.getProfitPlusValueOnAHand().compareTo(o2.getProfitPlusValueOnAHand());

                if ((o1.getProfitPlusValueOnAHand() < 0) || (o2.getProfitPlusValueOnAHand() < 0)) {

                    if ((o1.getActualPrize() == 0) && (o2.getActualPrize() != 0)) {
                        return -1;
                    }
                    if ((o1.getActualPrize() != 0) && (o2.getActualPrize() == 0)) {
                        return 1;
                    }


                    return o1.getProfitPlusValueOnAHand().compareTo(o2.getProfitPlusValueOnAHand());

                }

                if ((o1.getProfitPlusValueOnAHand() < 0) && (o2.getProfitPlusValueOnAHand() > 0)) {
                    return -1;
                }

                if ((o1.getProfitPlusValueOnAHand() > 0) && (o2.getProfitPlusValueOnAHand() < 0)) {
                    return 1;
                }

                if ((o2.getActualPrize() == 0) && (o2.getVolumeAtHand() != 0)) return -1;
                if ((o1.getActualPrize() == 0) && (o1.getVolumeAtHand() != 0)) return 1;
                return profitCmp * (-1);

            }
        });

    }


    private int isStockInVallet(String stockName) {

        for (int i = 0; i < valletsStock.size(); i++) {
            if (valletsStock.get(i).getStockName().equals(stockName)) {
                return i;
            }
        }

        return -1;
    }


    public void addTransactionsToTheValletsStocks(ArrayList<Transaction> listOfTransactions) {

        for (Transaction t : listOfTransactions) {
            String stockName = t.getStockName();
            int index = isStockInTheVallet(stockName);
            if (index != -1) {
                valletsStock.get(index).addTransaction(t);
            } else {
                valletsStock.add(new Stock(t));
            }
        }
    }


    private int isStockInTheVallet(String stockName) {
        for (int i = 0; i < valletsStock.size(); i++) {
            if (valletsStock.get(i).getStockName().equals(stockName)) {
                return i;
            }
        }
        return -1;
    }

    //TODO zmienic na tablice ?

    public void printItAllOut() {

        Double totalProfit = 0.0;
        Double totalValueOnAHand = 0.0;
        Double totalProfitPlusValueOnAHand = 0.0;
        // Stock.printHeader();
        for (int i = 0; i < valletsStock.size(); i++) {
            if (i % 10 == 0) {
                Stock.printHeader();
            }
            Stock stock = valletsStock.get(i);
            totalProfit += stock.getProfit();
            totalValueOnAHand += stock.getActualValueAtHand();
            totalProfitPlusValueOnAHand += stock.getProfitPlusValueOnAHand();
            System.out.println(stock.toString());
        }
        System.out.println();
        System.out.print("Total profit: ");
        System.out.printf("%8.2f", totalProfit);
        System.out.println();
        System.out.print("Wartość ręki: ");
        System.out.printf("%8.2f", totalValueOnAHand);
        System.out.println();
        System.out.print("Profit plus wartość ręki: ");
        System.out.printf("%8.2f", totalProfitPlusValueOnAHand);
        System.out.println();


    }


    public void addActualPrices(ArrayList<Stock> actualPrices) {
        int index;
        for (Stock stock : actualPrices) {
            if (stock.getActualPrize() == 0) {
                System.out.println(stock.getStockName() + "cena zero, sprawdzic nazwe cen aktualnych z nazwa w pliku tranzakcje");
            }
            index = isStockInVallet(stock.getStockName()); // returns StockIndex
            if (index != -1) {
                valletsStock.get(index).setActualPrize(stock.getActualPrize());
            }

        }
    }
}
