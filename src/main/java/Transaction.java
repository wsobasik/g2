import org.joda.time.LocalDate;


public class Transaction {

    private String stockName;
    private LocalDate transactionDate;
    private TRANSACTION_TYPE transaction;
    private int volume;
    private Integer volumeBeforeTransaction;
    private Integer volumeAfterTransaction;
    private double prize;
    private double value;

    public Transaction(String[] line) {

        String dataAndTime = line[0].substring(0, 10);//.split(".");//line[0].split(".");
        String[] dateAsArray = dataAndTime.split("\\.");
        int year = Integer.parseInt(dateAsArray[2]);
        int month = Integer.parseInt(dateAsArray[1]);
        int day = Integer.parseInt(dateAsArray[0]);
        LocalDate myDate = new LocalDate(year, month, day);
        this.transactionDate = myDate;
        this.stockName = correctStockNameIfHasChanged(line[1]);
        if (line[2].equals(TRANSACTION_TYPE.K.toString())) {
            this.transaction = TRANSACTION_TYPE.K;
        } else {
            this.transaction = TRANSACTION_TYPE.S;
        }
        String volumeAsString = line[3].replace(" ", "");
        this.volume = Integer.parseInt(volumeAsString);
        //this.volumeBeforeTransaction = 0;  it is null
       // this.volumeAfterTransaction = volume;
        String prizeAsString = line[4].replace(" ", "").replace(",", ".");
        this.prize = Double.parseDouble(prizeAsString);// kurs
        this.value = volume * prize;
    }


    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public TRANSACTION_TYPE getTransaction() {
        return transaction;
    }

    public void setTransaction(TRANSACTION_TYPE transaction) {
        this.transaction = transaction;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Integer getVolumeBeforeTransaction() {
        return volumeBeforeTransaction;
    }

    public void setVolumeBeforeTransaction(Integer beforeTransactionVolume) {
        this.volumeBeforeTransaction = beforeTransactionVolume;
    }

    public double getPrize() {
        return prize;
    }

    public void setPrize(double prize) {
        this.prize = prize;
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
