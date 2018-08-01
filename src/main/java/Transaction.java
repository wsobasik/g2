import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;


public class Transaction {

    private String stockName;
    private LocalDate transactionDate;
    private TRANSACTION_TYPE transactionType;
    private int volumeOfTransaction;
    private Integer volumeBeforeTransaction;
    private Integer volumeAfterTransaction;
    private double priceOfStockInTransaction;
    private double valueOfTransactionInCash;


    public Transaction(String[] line) {
        this.transactionDate =  LocalDateTime.parse(line[0], DateTimeFormat.forPattern("DD.MM.YYY HH:mm:ss")).toLocalDate();
        this.stockName = correctStockNameIfHasChanged(line[1]);
        this.transactionType = (line[2].equals("K") ? TRANSACTION_TYPE.K : TRANSACTION_TYPE.S);
        this.volumeOfTransaction = Integer.parseInt(line[3].replace(" ", ""));
        this.priceOfStockInTransaction = Double.parseDouble(line[4].replace(" ", "").replace(",", "."));
        this.valueOfTransactionInCash = volumeOfTransaction * priceOfStockInTransaction;
    }


    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
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

    public void setTransactionType(TRANSACTION_TYPE transactionType) {
        this.transactionType = transactionType;
    }

    public int getVolumeOfTransaction() {
        return volumeOfTransaction;
    }

    public void setVolumeOfTransaction(int volumeOfTransaction) {
        this.volumeOfTransaction = volumeOfTransaction;
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

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    private String correctStockNameIfHasChanged(String stockName) {

        if (stockName.equals("VELTO-NC")) {
            return "VELTO";
        }

        if (stockName.equals("JUJUBEE-PDA")) {
            return "JUJUBEE";
        }
        if (stockName.equals("SITE-NC") || (stockName.equals("SITE") )) {
            return "FDGAMES";
        }

        if (stockName.equals("01CYBATON-NC")) {
            return "01CYBATON";
        }
        if (stockName.equals("EKIOSK-NC")) {
            return "EKIOSK";
        }
        if (stockName.equals("PGSSOFT-NC")) {
            return "PGSSOFT";
        }
        if (stockName.equals("HARPER1")) {
            return "HARPER";
        }
        if (stockName.equals("PLANETINN-NC")) {
            return "PLANETINN";
        }
        if (stockName.equals("MGAMES-NC")) {
            return "MGAMES";
        }
        if (stockName.equals("VIVID-NC")) {
            return "VIVID";
        }
        if (stockName.equals("FARM51-NC")) {
            return "FARM51";
        }
        if (stockName.equals("BIOMAX-NC")) {
            return "BIOMAX";
        }
        return stockName;
    }


}
