import org.joda.time.LocalDateTime;

public class Split {



        private String stockName;
        private double ratio;
        private org.joda.time.LocalDateTime dateTime;

    public Split(String stockName, double ratio, LocalDateTime date) {
        this.stockName = stockName;
        this.ratio = ratio;
        this.dateTime = date;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
