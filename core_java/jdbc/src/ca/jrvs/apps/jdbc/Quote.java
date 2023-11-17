package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * The fields of each quote object correspond to the columns of the quote database.
 * id is a serial primary key, and is assigned a value by SQL.
 * All values except id and timestamp are retrieved in QuoteHTTPHelper.
 * Please see that class for additional documentation.
 * Getters and setters are included for all fields.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Quote
{
    private int id; //unique identifier
    @JsonProperty("01. symbol")
    private String symbol;
    @JsonProperty("02. open")
    private double open;
    @JsonProperty("03. high")
    private double high;
    @JsonProperty("04. low")
    private double low;
    @JsonProperty("05. price")
    private double price;
    @JsonProperty("06. volume")
    private int volume;
    @JsonProperty("07. latest trading day")
    private Date latestTradingDay;
    @JsonProperty("08. previous close")
    private double previousClose;
    @JsonProperty("09. change")
    private double change;
    @JsonProperty("10. change percent")
    private String changePercent;
    private Timestamp timestamp; //time when the info was pulled

    public int getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    public Date getLatestTradingDay() {
        return latestTradingDay;
    }

    public double getPreviousClose() {
        return previousClose;
    }

    public double getChange() {
        return change;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setLatestTradingDay(Date latestTradingDay) {
        this.latestTradingDay = latestTradingDay;
    }

    public void setPreviousClose(double previousClose) {
        this.previousClose = previousClose;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }



}
