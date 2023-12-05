package ca.jrvs.apps.trading.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class MarketOrderDto
{
    private String ticker;

    private String option;
    private int size;

    private int traderId;

    public String getTicker()
    {
        return ticker;
    }

    public void setTicker(String ticker)
    {
        this.ticker = ticker;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public int getTraderId() {
        return traderId;
    }

    public void setTraderId(int traderId) {
        this.traderId = traderId;
    }
}
