import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class StockNew {


    private String stockName;
    private Double historicalBuyValue;
    private Double historicalSoldValue;

    private Double actualPrize;
    private Integer actualVolume;
    private Double actualValue;
    private ArrayList<Transaction> transactionsList;
    private Double totalCashIfSellToday;

    public StockNew(String stockName, Double actualStockPrize, ArrayList<Transaction> transactions) {
        this.stockName = stockName;
        if (actualStockPrize != null) {
            this.actualPrize = actualStockPrize;
        } else {
            this.actualPrize = 0.0;
        }
        this.transactionsList = transactions;
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
        this.totalCashIfSellToday = round(actualValue + historicalSoldValue - historicalBuyValue,2);
    }//TODO zaokraglanie

    @Override
    public String toString() {
        if (actualVolume==0) {return stockName +" " + totalCashIfSellToday+"\n";}
        else
        return stockName + " Ile: "+ actualVolume +" obcn: " +actualPrize+ " Na Rece: " + actualValue +" cashIfSoldToday:"+ totalCashIfSellToday +"\n";
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
