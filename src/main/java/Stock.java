import org.joda.time.LocalDateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Stock {

    private String stockName;
    private Double actualPrize = 0.0;
    private ArrayList<SplitData> splitData;

    private Double totalValueOfTransactionsTypeBuy = 0.0;
    private Double totalValueOfTransactionsTypeSell = 0.0;
    private Double totalSumOfTransactionsTypeBuyAndSell = 0.0;
    private Integer volumenAtHand = 0;
    private Double valueAtHand = 0.0;//double
    private Double profit = 0.0;//double zostawic jedno TODO
    private Double handsProfit = 0.0;
    private ArrayList<Transaction> transactionsList;// is it really needed here??
    private Double avaragePrizePerStockOnAHand = 0.0;


    public Double getAvaragePrizePerStockOnAHand() {
        return avaragePrizePerStockOnAHand;// handsAvgPricePaid
    }

    public void setAvaragePrizePerStockOnAHand(Double avaragePrizePerStockOnAHand) {
        this.avaragePrizePerStockOnAHand = avaragePrizePerStockOnAHand;
    }

    public Stock(Transaction t) {

        this.stockName = t.getStockName();
        this.transactionsList = new ArrayList<>();
        this.transactionsList.add(t);
    }

    public Stock(String stockName, Double actualPrize) {
        this.stockName = stockName;
        this.actualPrize = actualPrize;
    }

    public Double getHandsProfit() {
        return handsProfit;
    }

    public void setHandsProfit() {
        this.handsProfit = getVolumeAtHand() * (getAvaragePrizePerStockOnAHand() - getActualPrize());
    }

    public void updateValueBeforeAfterRespectingSplit() {
        //manages the split with ration 100 => 10 stock with ratio 0.1
        for (int i = 0; i < transactionsList.size() - 1; i++) {
            Transaction transaction = transactionsList.get(i);
            Transaction nextTransaction = transactionsList.get(i + 1);
            ArrayList<SplitData> splitDataList = this.getSplitData();

            if (null != splitDataList) {//can this be null
                for (int j = 0; j < splitDataList.size(); j++) {
                    SplitData splitData = splitDataList.get(j);
                    LocalDateTime splitDataDate = splitData.getDateTime();
                    LocalDateTime transactionDateTime = transaction.getTransactionDateTime();
                    LocalDateTime nextTransactionDate = nextTransaction.getTransactionDateTime();
                    if (transactionDateTime.isBefore(splitDataDate) && (splitDataDate.isBefore(nextTransactionDate) || splitDataDate.isEqual(nextTransactionDate))) {
                        nextTransaction.setVolumeBeforeTransaction((int) (nextTransaction.getVolumeBeforeTransaction() / splitData.getRatio()));
                        updateValueBeforAfterTransactionAndOnAHand(i + 1);
                        //countValueOfTransaction(i+1);

                    }
                }
            }
        }
    }

    private void updateValueBeforAfterTransactionAndOnAHand(int index) {
        ArrayList<Transaction> transactionsList = this.getTransactionsList();
        for (int i = index; i < transactionsList.size(); i++) {
            Transaction transaction = transactionsList.get(i);//TODO czy trzeba tworzyc nowy obiekt?!
            int volumeOfTransactionWithSign = transaction.getVolumeOfTransaction() * (transaction.getTransactionType().equals(TRANSACTION_TYPE.K) ? 1 : -1);
            transaction.setVolumeAfterTransaction(transaction.getVolumeBeforeTransaction() + volumeOfTransactionWithSign);

            if (i < transactionsList.size() - 1) {
                Transaction nextTransaction = transactionsList.get(i + 1);
                nextTransaction.setVolumeBeforeTransaction(transaction.getVolumeAfterTransaction());
            }
        }
    }

    public void updateValueBeforAfterTransactionAndOnAHand() {
        //counts number of stock on hand
        ArrayList<Transaction> transactionsList = this.getTransactionsList();
        for (int i = 0; i < transactionsList.size(); i++) {
            Transaction transaction = transactionsList.get(i);
            int volumeOfTransactionWithSign = transaction.getVolumeOfTransaction() * (transaction.getTransactionType().equals(TRANSACTION_TYPE.K) ? 1 : -1);

            transaction.setVolumeAfterTransaction(transaction.getVolumeBeforeTransaction() + volumeOfTransactionWithSign);
            if (i < transactionsList.size() - 1) {
                Transaction nextTransaction = transactionsList.get(i + 1);
                nextTransaction.setVolumeBeforeTransaction(transaction.getVolumeAfterTransaction());
            }
        }
    }

    public void updateCashValueOfTransactions() {
        //updates value of transaction
        for (Transaction transaction : transactionsList) {
            transaction.setValueOfTransactionInCash(transaction.getVolumeOfTransaction() * transaction.getPriceOfStockInTransaction());
        }
    }

    public void setTotalValueOfTransactionsTypeBuy() {
        Double totalBuyValue = 0.0;
        for (Transaction transaction : transactionsList) {
            if (transaction.getTransactionType().equals(TRANSACTION_TYPE.K)) {
                totalBuyValue += transaction.getValueOfTransactionInCash();
            }
        }
        this.totalValueOfTransactionsTypeBuy = totalBuyValue;
    }


    public void setTotalValueOfTransactionsTypeSell() {
        Double totalSellValue = 0.0;
        for (Transaction transaction : transactionsList) {
            if (transaction.getTransactionType().equals(TRANSACTION_TYPE.S)) {
                totalSellValue += transaction.getValueOfTransactionInCash();
            }
        }
        this.totalValueOfTransactionsTypeSell = totalSellValue;
    }

    public void volumeAtHantTimesActualPrice() {
        setStockAtHandValue(getVolumeAtHand() * getActualPrize());
    }


    public String getStockName() {
        return stockName;
    }

    public Double getTotalValueOfTransactionsTypeBuy() {
        return totalValueOfTransactionsTypeBuy;
    }


    public Double getTotalValueOfTransactionsTypeSell() {
        return totalValueOfTransactionsTypeSell;
    }


    public ArrayList<Transaction> getTransactionsList() {
        return transactionsList;
    }


    public Double getActualPrize() {
        return actualPrize;
    }

    public void setActualPrize(Double actualPrize) {
        this.actualPrize = actualPrize;
    }

    public Integer getVolumeAtHand() {
        return volumenAtHand;
    }

    public void setVolumenAtHand(Integer volumenAtHand) {
        this.volumenAtHand = volumenAtHand;
    }

    public Double getValueAtHand() {
        return valueAtHand;
    }

    public void setStockAtHandValue(Double actualValueAtHand) {
        this.valueAtHand = actualValueAtHand;
    }

    public Double getProfit() {
        return profit;
    }

    public Double getTotalSumOfTransactionsTypeBuyAndSell() {
        return totalSumOfTransactionsTypeBuyAndSell;
    }

    public void setTotalSumOfTransactionsTypeBuyAndSell(double v) {
        this.totalSumOfTransactionsTypeBuyAndSell = round(totalValueOfTransactionsTypeSell - totalValueOfTransactionsTypeBuy, 2);
    }

    public void profit() {
        this.profit = round(valueAtHand + totalValueOfTransactionsTypeSell - totalValueOfTransactionsTypeBuy, 2);
//        this.profit = round(valueAtHand + totalValueOfTransactionsTypeSell - setTotalValueOfTransactionsTypeBuy, 2);
    }//TODO zaokraglanie

    private ArrayList<SplitData> getSplitData() {
        return splitData;
    }

    public void setSplitData(ArrayList<SplitData> splitData) {
        this.splitData = splitData;
    }

    @Override
    public String toString() {
        String output;
        if (getVolumeAtHand() == 0) { //wyswietlanie dla zera volume
            output = String.format("%-18s % -13.2f", stockName, this.getTotalSumOfTransactionsTypeBuyAndSell());

        } else

        {
            output = String.format("%-18s % -13.2f %-13.2f % 14.2f %9s %12.2f %12.2f % 12.2f",
                    stockName,
                    getTotalSumOfTransactionsTypeBuyAndSell(),
                    profit,
                    getValueAtHand() - getVolumeAtHand() * getAvaragePrizePerStockOnAHand(),
                    getVolumeAtHand(),
                    getAvaragePrizePerStockOnAHand(),
                    getActualPrize(),
                    getValueAtHand());

        }
        return output;
    }

    public static void printHeader() {
        String output = String.format("%-18s %-13s %-13s %14s %9s %12s %12s %12s ",
                "name",
                "totalSumOfTransactionsTypeBuyAndSell",
                "totalSumOfTransactionsTypeBuyAndSell w.Hand",
                "Hand's totalSumOfTransactionsTypeBuyAndSell",
                "volumen",
                "usr. cena kupna",
                "kurs",
                "On a hand $"

        );
        System.out.println();
        System.out.println(output);
        System.out.println();
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void addTransaction(Transaction t) {
        this.transactionsList.add(t);
    }

    public static String correctStockNameIfHasChanged(String stockName) {
        //wywolywane z konstruktora Transaction
        // that should be done after downloading the file from the internet
        //in Program class there are two methods that are doing the same!! correctstockNamesIfHasChanged, poprawNazwyAkcji

        if (stockName.equals("FOREVEREN-NC")) {
            return "FOREVEREN";
        }
        if (stockName.equals("VELTO-NC")) {
            return "VELTO";
        }
        if ((stockName.equals("JUJUBEE-PDA")) || (stockName.equals("JUJUBEE-NC"))) {
            return "JUJUBEE";
        }
        if (stockName.equals("SITE-NC") || (stockName.equals("SITE"))) {
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
        if (stockName.equals("THEDUST-NC")) {
            return "THEDUST";
        }
        if (stockName.equals("EFENERGII")) {
            return "EFENERGII-NC";
        }

        return stockName;
    }

    public void updateVolumeAtHand() {
        int sizeOfTransactionList = this.getTransactionsList().size() - 1;
        Transaction lastTransaction = this.getTransactionsList().get(sizeOfTransactionList);
        setVolumenAtHand(lastTransaction.getVolumeAfterTransaction());
    }

    public Double countAvaragePrizeOnStockOnAHand() {
        ArrayList<Transaction> transactionsList = this.getTransactionsList();
        Integer volumenOnAHand = getVolumeAtHand();
        Double avgValueOfLastBuyTransactions = 0.0;
        Transaction transaction;
        int i = transactionsList.size() - 1;

        if (getVolumeAtHand() == 0) {
            return 0.0;
        }
        while (volumenOnAHand > 0) {
            transaction = transactionsList.get(i);
            if (transaction.getTransactionType().equals(TRANSACTION_TYPE.K)) {
                volumenOnAHand -= transaction.getVolumeOfTransaction();
                if (volumenOnAHand > 0) {
                    avgValueOfLastBuyTransactions += transaction.getValueOfTransactionInCash();
                } else {
                    avgValueOfLastBuyTransactions += (transaction.getVolumeOfTransaction() + volumenOnAHand)
                            * transaction.getPriceOfStockInTransaction();
                }
            }
            i--;
        }
        return avgValueOfLastBuyTransactions / getVolumeAtHand();
    }

}
