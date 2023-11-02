import java.util.Date;

public class Rate {

    private Date date;
    private String fromSymbol;
    private String toSymbol;
    private double open;
    private double high;
    private double low;
    private double close;

    // Constructor
    public Rate() {
        // You can initialize the fields if necessary
    }

    // Getters and setters for each field
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFromSymbol() {
        return fromSymbol;
    }

    public void setFromSymbol(String fromSymbol) {
        this.fromSymbol = fromSymbol;
    }

    public String getToSymbol() {
        return toSymbol;
    }

    public void setToSymbol(String toSymbol) {
        this.toSymbol = toSymbol;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    // You may add toString() method if you need to represent Rate object as a String
    @Override
    public String toString() {
        return "Rate{" +
                "date=" + date +
                ", fromSymbol='" + fromSymbol + '\'' +
                ", toSymbol='" + toSymbol + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                '}';
    }
}