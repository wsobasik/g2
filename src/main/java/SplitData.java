import org.joda.time.LocalDate;

public class SplitData {



        private String stockName;
        private double ratio;
        private LocalDate date;

    public SplitData(String stockName, double ratio, LocalDate date) {
        this.stockName = stockName;
        this.ratio = ratio;
        this.date = date;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
