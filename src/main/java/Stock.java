import org.joda.time.LocalDateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Stock {


    private String stockName;
    private Double buys = 0.0;
    private Double sells = 0.0;
    private Double profit = 0.0;
    private Integer actualVolumeAtHand = 0;
    private Double actualPrize = 0.0;
    private Double actualValueAtHand = 0.0;//duble
    private Double profitPlusValueOnAHand = 0.0;//duble zostawic jedno TODO
    private Double handsProfit=0.0;
    private ArrayList<Transaction> transactionsList;// is it really needed here??
    private ArrayList<Split> splitData;
    private Double handsValueInCashWhenBought=0.0;
    private Double avaragePrizePerStockOnAHand = 0.0;



    public Double getAvaragePrizePerStockOnAHand() {
        return avaragePrizePerStockOnAHand;// handsAvgPricePaid
    }

    public void setAvaragePrizePerStockOnAHand(Double avaragePrizePerStockOnAHand) {
        this.avaragePrizePerStockOnAHand = avaragePrizePerStockOnAHand;
    }



    public Stock(String stockName, Double actualStockPrize, ArrayList<Transaction> transactions) {
        this.stockName = stockName;
        if (actualStockPrize != null) {
            this.actualPrize = actualStockPrize;
        } else {
            this.actualPrize = 0.0;
        }

        this.transactionsList = transactions;

        //TODO dodac inicjalizacje pozostalych pol
    }

    public Stock(Transaction t) {

        this.stockName = t.getStockName();
        this.transactionsList = new ArrayList<Transaction>();
        this.transactionsList.add(t);

    }

    public Stock(String stockName, Double actualPrize) {
        this.stockName = stockName;
        this.actualPrize = actualPrize;
    }


    public void setBuys(Double buys) {
        this.buys = buys;
    }

    public void setSells(Double sells) {
        this.sells = sells;
    }

    public void setProfit(Double profit) {

        this.profit = profit;
    }

    public Double getHandsProfit() {

        return handsProfit;



    }

    public void setHandsProfit() {
        this.handsProfit = getActualVolumeAtHand()*(getAvaragePrizePerStockOnAHand()-getActualPrize());
    }

    public Double getHandsValueInCashWhenBought() {
        return handsValueInCashWhenBought;
    }

    public void setHandsValueInCashWhenBought(Double handsValueInCashWhenBought) {
        this.handsValueInCashWhenBought = handsValueInCashWhenBought;
    }



    public void updateValueBeforeAfterRespectingSplit() {
        //manages the split with ration 100 => 10 stock with ratio 0.1
        ArrayList<Transaction> transactionsList = this.getTransactionsList();
        for (int i = 0; i < transactionsList.size() - 1; i++) {
            Transaction transaction = transactionsList.get(i);
            Transaction nextTransaction = transactionsList.get(i + 1);
            ArrayList<Split> splitList = this.getSplitData();

            if (null != splitList) {//can this be null
                for (int j = 0; j < splitList.size(); j++) {
                    Split split = splitList.get(j);
                    LocalDateTime splitDataDate = split.getDateTime();
                    LocalDateTime transactionDateTime = transaction.getTransactionDateTime();
                    LocalDateTime nextTransactionDate = nextTransaction.getTransactionDateTime();
                    if (transactionDateTime.isBefore(splitDataDate) && (splitDataDate.isBefore(nextTransactionDate) || splitDataDate.isEqual(nextTransactionDate))) {
                        nextTransaction.setVolumeBeforeTransaction((int) (nextTransaction.getVolumeBeforeTransaction() / split.getRatio()));
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
        ArrayList<Transaction> transactionsList = this.getTransactionsList();
        for (int i = 0; i < transactionsList.size(); i++) {
            Transaction transaction = transactionsList.get(i);
            transaction.setValueOfTransactionInCash(transaction.getVolumeOfTransaction() * transaction.getPriceOfStockInTransaction());
        }
    }

    public void countBuysValue() {
        ArrayList<Transaction> transactionsList = this.getTransactionsList();
        Double tempBuys = 0.0;
        for (int i = 0; i < transactionsList.size(); i++) {
            Transaction transaction = transactionsList.get(i);
            if (transaction.getTransactionType().equals(TRANSACTION_TYPE.K)) {
                tempBuys += transaction.getValueOfTransactionInCash();
            }
        }
        setBuys(tempBuys);
    }


    public void countSellsValue() {
        //ArrayList<Transaction> transactionsList = this.getTransactionsList();
        Double tmpSells = 0.0;
        for (int i = 0; i < transactionsList.size(); i++) {
            Transaction transaction = transactionsList.get(i);
            if (transaction.getTransactionType().equals(TRANSACTION_TYPE.S)) {
                tmpSells += transaction.getValueOfTransactionInCash();
            }
        }
        setSells(tmpSells);
    }

    public void countAHandValueIfSoldToday() {
        this.setActualValueAtHand(this.getActualVolumeAtHand() * this.getActualPrize());
    }


    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Double getBuys() {
        return buys;
    }

    public void setTotalBuyerAmountOfCash(Double historicalBuyValue) {
        this.buys = historicalBuyValue;
    }

    public Double getSells() {
        return sells;
    }

    public void setTotalSalesAmountOfCash(Double historicalSoldValue) {
        this.sells = historicalSoldValue;
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

    public void setProfitPlusValueOnAHand(Double profitPlusValueOnAHand) {
        this.profitPlusValueOnAHand = profitPlusValueOnAHand;
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

    public Double getProfitPlusValueOnAHand() {
        return profitPlusValueOnAHand;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(double v) {
        this.profit = round(sells - buys, 2);
    }

    public void countProfitInCashPlusValueAtHand() {
        setProfitPlusValueOnAHand(round(actualValueAtHand + sells - buys, 2));
//        this.profitPlusValueOnAHand = round(actualValueAtHand + sells - buys, 2);
    }//TODO zaokraglanie

    public ArrayList<Split> getSplitData() {
        return splitData;
    }

    public void setSplit(ArrayList<Split> splitData) {
        this.splitData = splitData;
    }

    @Override
    public String toString() {
        String output;
        if (getActualVolumeAtHand() == 0) { //wyswietlanie dla zera volume
            output = String.format("%-14s% -13.2f", stockName, this.getProfit());

        } else

            {
            output = String.format("%-14s % -13.2f %-13.2f % 14.2f %9s %12.2f %12.2f % 12.2f",
                    stockName,
                    getProfit(),
                    getProfitPlusValueOnAHand(),
                    getActualValueAtHand()-getActualVolumeAtHand()*getAvaragePrizePerStockOnAHand(),
                    getActualVolumeAtHand(),
                    getAvaragePrizePerStockOnAHand(),
                    getActualPrize(),
                    getActualValueAtHand());

        }
        return output;
    }

    public static void printHeader() {
        String output = String.format("%-14s %-13s %-13s %14s %9s %12s %12s %12s ",
                "name",
                "profit",
                "profit w.Hand",
                "Hand's profit",
                "volumen",
                "avg_prize",
                "Act prize",
                "On a hand $"

        );
        System.out.println();
        System.out.println(output);
        System.out.println();

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }




    public void addTransaction(Transaction t) {
        this.transactionsList.add(t);
    }

    public void updateValueAtHand() {
        setActualValueAtHand(getActualVolumeAtHand() * getActualPrize());
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
        if (stockName.equals("SITE-NC") || stockName.equals("SITE")|| (stockName.equals("FDGAMES"))) {
            return "NGGAMES";
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
        setActualVolumeAtHand(lastTransaction.getVolumeAfterTransaction());
    }

    public Double countAvaragePrizeOnStockOnAHand() {
        ArrayList<Transaction> transactionsList = this.getTransactionsList();
        Integer volumenOnAHand = getActualVolumeAtHand();
        Double avgValueOfLastBuyTransactions = 0.0;
        Transaction transaction;
        int i = transactionsList.size()-1;

        if (getActualVolumeAtHand()==0){
            return 0.0;
        }
        while (volumenOnAHand>0) {
            transaction = transactionsList.get(i);
            if (transaction.getTransactionType().equals(TRANSACTION_TYPE.K)) {
                volumenOnAHand -=transaction.getVolumeOfTransaction();
                if (volumenOnAHand>0) {
                    avgValueOfLastBuyTransactions += transaction.getValueOfTransactionInCash();
                }
                else
                {
                    avgValueOfLastBuyTransactions+= (transaction.getVolumeOfTransaction()+volumenOnAHand)
                            * transaction.getPriceOfStockInTransaction();
                }
            }
            i--;
        }
        return avgValueOfLastBuyTransactions / getActualVolumeAtHand();
    }

}
