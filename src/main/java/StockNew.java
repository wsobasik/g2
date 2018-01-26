import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static java.lang.Integer.compare;

public class StockNew {


    private String stockName;
    private Double historicalBuyValue;
    private Double historicalSoldValue;
    private Double actualPrize;
    private Integer actualVolume;
    private Double actualValue;
    private ArrayList<Transaction> transactionsList;
    private Double totalCashIfSellToday;
    private ArrayList<SplitData> splitData;

    public StockNew(String stockName, Double actualStockPrize, ArrayList<Transaction> transactions) {
        this.stockName = stockName;
        if (actualStockPrize != null) {
            this.actualPrize = actualStockPrize;
        } else {
            this.actualPrize = 0.0;
        }

        this.transactionsList = transactions;
/*
        for (Transaction transaction : transactions) {
            if (transaction.getTransaction().equals(TRANSACTION_TYPE.K)) {
                this.actualVolume += transaction.getVolume();
            } else {
                this.actualVolume -= transaction.getVolume();
            }
            actualValue;
        }
*/
        //TODO dodac inicjalizacje pozostalych pol
    }


    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Double getHistoricalBuyValue() {
        return historicalBuyValue;
    }

    public void setHistoricalBuyValue(Double historicalBuyValue) {
        this.historicalBuyValue = historicalBuyValue;
    }

    public Double getHistoricalSoldValue() {
        return historicalSoldValue;
    }

    public void setHistoricalSoldValue(Double historicalSoldValue) {
        this.historicalSoldValue = historicalSoldValue;
    }

    public ArrayList<Transaction> getTransactionsList() {
        return transactionsList;
    }

    public void setTransactionsList(ArrayList<Transaction> transactionsList) {
        this.transactionsList = transactionsList;
    }

    public Double getActualPrize() {
        return actualPrize;
    }

    public void setActualPrize(Double actualPrize) {
        this.actualPrize = actualPrize;
    }

    public void setTotalCashIfSellToday(Double totalCashIfSellToday) {
        this.totalCashIfSellToday = totalCashIfSellToday;
    }

    public Integer getActualVolume() {
        return actualVolume;
    }

    public void setActualVolume(Integer actualVolume) {
        this.actualVolume = actualVolume;
    }

    public Double getActualValue() {
        return actualValue;
    }

    public void setActualValue(Double actualValue) {
        this.actualValue = actualValue;
    }

    public Double getTotalCashIfSellToday() {
        return totalCashIfSellToday;
    }

    public void setTotalCashIfSellToday() {
        this.totalCashIfSellToday = round(actualValue + historicalSoldValue - historicalBuyValue, 2);
    }//TODO zaokraglanie

    public ArrayList<SplitData> getSplitData() {
        return splitData;
    }

    public void setSplitData(ArrayList<SplitData> splitData) {
        this.splitData = splitData;
    }

    @Override
    public String toString() {
        if (actualVolume == 0) {
            return stockName + " " + totalCashIfSellToday + "\n";
        } else
            return stockName + " Ile: " + actualVolume + " obcn: " + actualPrize + " Na Rece: " + actualValue + " cashIfSoldToday:" + totalCashIfSellToday + "\n";
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public int compareTo(StockNew s2) {

        if (compare(this.getActualVolume(), s2.getActualVolume()) == 0) {
            return compare(this.historicalSoldValue.intValue() - this.getHistoricalBuyValue().intValue(),
                    s2.historicalSoldValue.intValue() - s2.getHistoricalBuyValue().intValue());
        }
        return compare(this.getActualVolume(), s2.getActualVolume());
    }


}
