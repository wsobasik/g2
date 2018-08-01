import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static java.lang.Integer.compare;

public class StockNew {


    private String stockName;
    private Double totalPaidValueInCash;
    private Double totalEarnValueInCash;
    private ArrayList<Transaction> transactionsList;// is it really needed here??
    private Double handsValueInCashWhenBought;
    private Double handsAvgPricePaid;
    private Double handsProfit;

    private Double actualValueAtHand;//duble
    private Double presentValueInCash;//duble zostawic jedno TODO
    private Integer actualVolumeAtHand;
    private Double actualPrize;
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
            if (transaction.getTransactionType().equals(TRANSACTION_TYPE.K)) {
                this.actualVolumeAtHand += transaction.getVolumeOfTransaction();
            } else {
                this.actualVolumeAtHand -= transaction.getVolumeOfTransaction();
            }
            actualValueAtHand;
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

    public Double getTotalPaidValueInCash() {
        return totalPaidValueInCash;
    }

    public void setTotalBuyerAmountOfCash(Double historicalBuyValue) {
        this.totalPaidValueInCash = historicalBuyValue;
    }

    public Double getTotalEarnValueInCash() {
        return totalEarnValueInCash;
    }

    public void setTotalSalesAmountOfCash(Double historicalSoldValue) {
        this.totalEarnValueInCash = historicalSoldValue;
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

    public void setPresentValueInCash(Double presentValueInCash) {
        this.presentValueInCash = presentValueInCash;
    }

    public Integer getActualVolumeAtHand() {
        return actualVolumeAtHand;
    }

    public void setActualVolumeAtHand(Integer actualVolumeAtHand) {
        this.actualVolumeAtHand = actualVolumeAtHand;
    }

    public Double getActualValueAtHand() {
        return actualValueAtHand;
    }

    public void setActualValueAtHand(Double actualValueAtHand) {
        this.actualValueAtHand = actualValueAtHand;
    }

    public Double getPresentValueInCash() {
        return presentValueInCash;
    }

    public void setTotalCashIfSellToday() {
        this.presentValueInCash = round(actualValueAtHand + totalEarnValueInCash - totalPaidValueInCash, 2);
    }//TODO zaokraglanie

    public ArrayList<SplitData> getSplitData() {
        return splitData;
    }

    public void setSplitData(ArrayList<SplitData> splitData) {
        this.splitData = splitData;
    }

    @Override
    public String toString() {
        if (actualVolumeAtHand == 0) {
            return stockName + " " + presentValueInCash + "\n";
        } else
            return stockName + " Ile: " + actualVolumeAtHand + " obcn: " + actualPrize + " Na Rece: " + actualValueAtHand + " cashIfSoldToday:" + presentValueInCash + "\n";
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public int compareTo(StockNew s2) {

        if (compare(this.getActualVolumeAtHand(), s2.getActualVolumeAtHand()) == 0) {
            return compare(this.totalEarnValueInCash.intValue() - this.getTotalPaidValueInCash().intValue(),
                    s2.totalEarnValueInCash.intValue() - s2.getTotalPaidValueInCash().intValue());
        }
        return compare(this.getActualVolumeAtHand(), s2.getActualVolumeAtHand());
    }


}
