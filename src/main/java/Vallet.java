import org.joda.time.LocalDateTime;
//import utils.compareByHandsLoss;


import java.util.*;

public class Vallet {

    private ArrayList<Stock> valletsStock = new ArrayList<>();

    Vallet() {

    }

    public void countValletStock() {
        //count all the numbers

        for (Stock stock : valletsStock) {
            stock.updateValueBeforAfterTransactionAndOnAHand();
            stock.updateValueBeforeAfterRespectingSplit();
            stock.updateVolumeAtHand();
            stock.updateCashValueOfTransactions();
            stock.setTotalValueOfTransactionsTypeBuy();
            stock.setTotalValueOfTransactionsTypeSell();
            stock.setTotalSumOfTransactionsTypeBuyAndSell();
            stock.volumeAtHantTimesActualPrice();
            stock.profit();
            stock.setAvaragePrizePerStockOnAHand(stock.countAvaragePrizeOnStockOnAHand());
            stock.setHandsProfit();
            stock.setPriceToGetProfit();

        }
    }


    public void addSplit() {
        //TODO wczytac z pliku
        //this is to be rebuild and replaced
        int index;
        String stockName;
        stockName = "DREWEX";

        index = isStockInValet(stockName); // returns StockIndex
        if (index > 0) {
            valletsStock.get(index).setSplit((new ArrayList<Split>() {{
                add(new Split("DREWEX", 10, new LocalDateTime(2015, 6, 29, 0, 1)));
            }}));
        }

        stockName = "CIGAMES";

        index = isStockInValet(stockName); // returns StockIndex
        if (index > 0) {
            valletsStock.get(index).setSplit(new ArrayList<Split>() {{
                add(new Split("CIGAMES", 0.1, new LocalDateTime(2017, 2, 23, 0, 1))); //odwrocic dzielnik
            }});
        }

        stockName = "RESBUD";

        index = isStockInValet(stockName); // returns StockIndex
        if (index > 0) {
            valletsStock.get(index).setSplit(new ArrayList<Split>() {{
                add(new Split("RESBUD", 5, new LocalDateTime(2017, 1, 13, 0, 1)));
            }});
        }


        stockName = "01CYBATON";

        index = isStockInValet(stockName); // returns StockIndex
        if (index > 0) {
            valletsStock.get(index).setSplit(new ArrayList<Split>() {{
                add(new Split("01CYBATON", 0.05, new LocalDateTime(2015, 11, 25, 0, 1)));
            }});
        }

        stockName = "EFENERGII-NC";

        index = isStockInValet(stockName); // returns StockIndex
        if (index > 0) {
            valletsStock.get(index).setSplit(new ArrayList<Split>() {{
                add(new Split("EFENERGII-NC", 10, new LocalDateTime(2019, 1, 11, 0, 1)));
            }});
        }

        stockName = "HERKULES";

        index = isStockInValet(stockName); // returns StockIndex
        if (index > 0) {
            valletsStock.get(index).setSplit(new ArrayList<Split>() {{
                add(new Split("HERKULES", 5, new LocalDateTime(2012, 9, 19, 0, 1)));
            }});
        }
    }

    public void sortTransactionsByTime() {
        for (Stock stock : valletsStock) {
            ArrayList<Transaction> transactionsList = stock.getTransactionsList();
            transactionsList.sort(Comparator.comparing(Transaction::getTransactionDateTime));
        }
    }

    public void sortByLossWithoutHand() {
        valletsStock.sort(new compareByLossWithoutHand());
    }

    public void sortByLossWitHand() {
        valletsStock.sort((t1, t2) -> t2.getValueAtHand().compareTo(t1.getValueAtHand()));
    }

    private int isStockInValet(String stockName) {

        for (int i = 0; i < valletsStock.size(); i++) {
            if (valletsStock.get(i).getStockName().equals(stockName)) {
                return i;
            }
        }

        return -1;
    }


    public void addTransactionsToTheValetsStocks(ArrayList<Transaction> listOfTransactions) {

        for (Transaction t : listOfTransactions) {
            String stockName = t.getStockName();
            int index = isStockInTheValet(stockName);
            if (index != -1) {
                valletsStock.get(index).addTransaction(t);
            } else {
                valletsStock.add(new Stock(t));
            }
        }
    }


    private int isStockInTheValet(String stockName) {
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
            totalProfit += stock.getTotalSumOfTransactionsTypeBuyAndSell();
            totalValueOnAHand += stock.getValueAtHand();
            totalProfitPlusValueOnAHand += stock.getProfit();
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
            index = isStockInValet(stock.getStockName()); // returns StockIndex
            if (index != -1) {
                valletsStock.get(index).setActualPrize(stock.getActualPrize());
            }

        }
    }
}
