import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Objects;


public class Transaction {

    private String stockName;
    //   private LocalDate transactionDate;
    private LocalDateTime transactionDateTime;
    private TRANSACTION_TYPE transactionType;
    private int volumeOfTransaction;
    private double priceOfStockInTransaction;
    private double valueOfTransactionInCash;
    private Integer volumeBeforeTransaction;
    private Integer volumeAfterTransaction;


    public Transaction(String[] line) {
        stockName = line[1];
        transactionDateTime = LocalDateTime.parse(line[0], DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss"));
        transactionType = (line[3].equals("K") ? TRANSACTION_TYPE.K : TRANSACTION_TYPE.S);
        volumeOfTransaction = Integer.parseInt(line[4].replace(" ", ""));
        priceOfStockInTransaction = Double.parseDouble(line[5].replace(" ", "").replace(",", "."));
        valueOfTransactionInCash = volumeOfTransaction * priceOfStockInTransaction;
        volumeBeforeTransaction = 0;
        volumeAfterTransaction = 0;
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getValueOfTransactionInCash() {
        return valueOfTransactionInCash;
    }

    public void setValueOfTransactionInCash(double valueOfTransactionInCash) {
        this.valueOfTransactionInCash = valueOfTransactionInCash;
    }

    public TRANSACTION_TYPE getTransactionType() {
        return transactionType;
    }

    public int getVolumeOfTransaction() {
        return volumeOfTransaction;
    }


    public Integer getVolumeBeforeTransaction() {
        return volumeBeforeTransaction;
    }

    public void setVolumeBeforeTransaction(Integer beforeTransactionVolume) {
        this.volumeBeforeTransaction = beforeTransactionVolume;
    }

    public double getPriceOfStockInTransaction() {
        return priceOfStockInTransaction;
    }

    public void setPriceOfStockInTransaction(double priceOfStockInTransaction) {
        this.priceOfStockInTransaction = priceOfStockInTransaction;
    }

    public int getVolumeAfterTransaction() {
        return volumeAfterTransaction;
    }

    public void setVolumeAfterTransaction(int volumeAfterTransaction) {
        this.volumeAfterTransaction = volumeAfterTransaction;
    }

    public String getStockName() {
        return stockName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.priceOfStockInTransaction, priceOfStockInTransaction) == 0 &&
                Objects.equals(stockName, that.stockName) &&
                Objects.equals(transactionDateTime, that.transactionDateTime) && //czy to porownuje z czasem czy tylko date?
                transactionType == that.transactionType;
    }

    @Override
    public int hashCode() {

        return Objects.hash(stockName, transactionDateTime, transactionType, priceOfStockInTransaction);
    }

    public static void correctTheNameOfStock(ArrayList<Transaction> transactions) {

        for (Transaction transaction :
                transactions) {
            transaction.setStockName(Stock.correctStockNameIfHasChanged(transaction.getStockName()));
        }


    }
}
