import org.joda.time.LocalDate;

import java.util.Date;


public class Transaction {

    private LocalDate transactionDate;
    private TRANSACTION_TYPE transaction;
    private int volume;
    private double prize;
    private double value;
    private int volumeAfterTransaction;

    public Transaction(String[] line) {

        String dataAndTime = line[0].substring(0, 10);//.split(".");//line[0].split(".");
        String[] dateAsArray = dataAndTime.split("\\.");
        int year = Integer.parseInt(dateAsArray[2]);
        int month = Integer.parseInt(dateAsArray[1]);
        int day = Integer.parseInt(dateAsArray[0]);
        LocalDate myDate = new LocalDate(year,month, day);
        this.transactionDate = myDate;
        //  this.stockName = line[1];
        String volumeAsString = line[3].replace(" ","");
        this.volume = Integer.parseInt(volumeAsString);
        String prizeAsString = line[4].replace(" ","").replace(",",".");
        this.prize = Double.parseDouble(prizeAsString);// kurs
        this.value = volume * prize;
        if (line[2].equals(TRANSACTION_TYPE.K.toString())) {
            this.transaction = TRANSACTION_TYPE.K;
        } else {
            this.transaction = TRANSACTION_TYPE.S;
        }
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
}
