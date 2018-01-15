import org.joda.time.LocalDate;

import java.util.Date;


public class Transaction {

    private LocalDate transactionDate;
    private TRANSACTION_TYPE transaction;
    private int volume;
    private double prize;
    private double value;

    public Transaction(String[] line) {

        String[] dataAndTime = line[0].split("-");
        String data = dataAndTime[0] + '-' + dataAndTime[1] + '-' + dataAndTime[2];
        LocalDate myDate = LocalDate.parse(data);
        this.transactionDate = myDate;
        //  this.stockName = line[1];
        this.volume = Integer.parseInt(line[3]);
        this.prize = Double.parseDouble(line[4]);// kurs
        this.value = volume * prize;
        if (line[2].equals(TRANSACTION_TYPE.KUPNO.toString())) {
            this.transaction = TRANSACTION_TYPE.KUPNO;
        } else {
            this.transaction = TRANSACTION_TYPE.SPRZEDAZ;
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
}
