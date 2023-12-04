package ca.jrvs.apps.trading.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="quote", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class IexQuote
{

    @Id
    @JsonProperty("symbol")
    @Column(name = "ticker", columnDefinition = "VARCHAR(255)", unique = true, nullable = false)
    private String ticker;

    @JsonProperty("latestPrice")
    @Column(name = "last_price")
    private double lastPrice;

    @JsonProperty("iexBidPrice")
    @Column(name = "bid_price")
    private double bidPrice;

    @JsonProperty("iexBidSize")
    @Column(name = "bid_size")
    private int bidSize;

    @JsonProperty("iexAskPrice")
    @Column(name = "ask_price")
    private double askPrice;

    @JsonProperty("iexAskSize")
    @Column(name = "ask_size")
    private int askSize;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public int getBidSize() {
        return bidSize;
    }

    public void setBidSize(int bidSize) {
        this.bidSize = bidSize;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }

    public int getAskSize() {
        return askSize;
    }

    public void setAskSize(int askSize) {
        this.askSize = askSize;
    }

}
