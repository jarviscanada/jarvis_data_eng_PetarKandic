package ca.jrvs.apps.jdbc;

/**
 * This class is used to store "portfolio" info.
 * Users can buy and sell stocks.
 */
public class Position
{

    private int id;
    private String symbol;
    private int numOfShares;
    private double valuePaid;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    public int getNumOfShares()
    {
        return numOfShares;
    }

    public void setNumOfShares(int numOfShares)
    {
        this.numOfShares = numOfShares;
    }

    public double getValuePaid()
    {
        return valuePaid;
    }

    public void setValuePaid(double valuePaid)
    {
        this.valuePaid = valuePaid;
    }
}
